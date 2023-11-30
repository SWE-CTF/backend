package sogong.ctf.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sogong.ctf.domain.*;
import sogong.ctf.dto.response.AttemptDTO;
import sogong.ctf.dto.request.CodeRequestDTO;
import sogong.ctf.dto.response.AttemptSuccessDTO;
import sogong.ctf.repository.AttemptRepository;
import sogong.ctf.repository.ChallengeRepository;
import sogong.ctf.repository.MemberRepository;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@EnableAsync
public class AttemptService {

    private final AttemptRepository attemptRepository;
    private final ChallengeRepository challengeRepository;
    private final MemberRepository memberRepository;

    @Value("${user.docker.id}")
    String dockerId;
    @Value("${user.docker.pw}")
    String dockerPw;
    @Value("${server.docker.url}")
    String dockerUrl;
    @Value("${server.docker.apiVersion}")
    String apiVersion;
    @Value("${server.docker.host}")
    String host;

    @Value("${code.storage.path}")
    private String codeStoragePath;

    public List<AttemptDTO> getChallengeAttempt(int id) {
        Optional<Challenge> challenge = challengeRepository.findById((long) id);
        return getAttemptListDTO(challenge.get().getAttempts());
    }

    private List<AttemptDTO> getAttemptListDTO(List<Attempt> attempts) {
        return attempts.stream()
                .map(attempt -> AttemptDTO.builder()
                        .code(attempt.getCode())
                        .codeStatus(attempt.getCodeStatus())
                        .challengeId(attempt.getChallengeId().getId())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public Long saveAttempt(CodeRequestDTO codeRequestDTO, Member member) {

        Optional<Challenge> challenge = challengeRepository.findById(codeRequestDTO.getChallengeId());

        Attempt attempt = Attempt.builder()
                .member(member)
                .code(codeRequestDTO.getCode())
                .codeStatus(CodeStatus.READY)
                .challenge(challenge.get())
                .build();

        return attemptRepository.save(attempt).getId();
    }

    @Transactional
    @Async
    public void compileAndRun(CodeRequestDTO codeRequestDTO, Member member) throws UnsupportedEncodingException {


        String userCode = URLDecoder.decode(codeRequestDTO.getCode(),"UTF-8");
        codeRequestDTO.setCode(userCode);
        Long attemptId = saveAttempt(codeRequestDTO, member);
        String image = null;
        int exitCode = 0;
        CodeStatus codeStatus = CodeStatus.ERROR;
        //첫 시도 입력

        Optional<Challenge> challenge = challengeRepository.findById(codeRequestDTO.getChallengeId());
        List<TestCase> testCaseList = challenge.get().getTestcases();
        //해당 문제에서 testcase
        Optional<Member> member1 = memberRepository.findByUsername(member.getUsername());

        float memory = challenge.get().getMemory();
        float time = challenge.get().getTime();

        float memoryLimit = memory * 1024 * 1024; //MB
        float timeLimit = time; //s

        //이미지 선택
        image = getDockerImage(codeRequestDTO.getLanguage());
        //이미지 선택

        try(DockerClient docker = DockerClientBuilder.getInstance(DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(host)
                .withDockerTlsVerify(false)
                .withApiVersion(apiVersion)
                .withRegistryUrl(dockerUrl)
                .withRegistryUsername(dockerId)
                .withRegistryPassword(dockerPw)
                .build()).build()) {

            //이미지 종류 선택 필요
            try {
                docker.pullImageCmd(image)
                        .exec(new PullImageResultCallback()).awaitCompletion();
            } catch (InterruptedException e) {
                System.out.println("image pull fail");
                throw new RuntimeException(e);
            }
            //이미지 종류 선택 필요

            for (int i = 0; i < 3; i++) {

                String arg = testCaseList.get(i).getInput();
                String expOutput = testCaseList.get(i).getOutput();
                CreateContainerCmd createContainerCmd = null;

                //testcase 실행
                if (codeRequestDTO.getLanguage().equalsIgnoreCase("java")) {
                    createContainerCmd = docker.createContainerCmd(image)
                            .withAttachStderr(true)
                            .withAttachStdout(true)
                            .withCmd("sh", "-c", "echo '" + userCode + "' > /usr/src/app/Main.java && javac /usr/src/app/Main.java && timeout " + timeLimit + "s java -classpath /usr/src/app Main " + arg)
                            .withHostConfig(new HostConfig()
                                    .withMemory((long) memoryLimit)
                                    .withMemorySwap((long) memoryLimit*2)
                            );

                } else if(codeRequestDTO.getLanguage().equalsIgnoreCase("python")){
                    createContainerCmd = docker.createContainerCmd(image)
                            .withAttachStderr(true)
                            .withAttachStdout(true)
                            .withCmd("sh", "-c", "echo '" + userCode + "' > /usr/src/app/main.py && timeout " + timeLimit + "s python3 /usr/src/app/main.py " + arg)
                            .withHostConfig(new HostConfig()
                                    .withMemory((long) memoryLimit)
                                    .withMemorySwap((long) memoryLimit*2)
                            );
                } else if(codeRequestDTO.getLanguage().equalsIgnoreCase("c")){
                    createContainerCmd = docker.createContainerCmd(image)
                            .withAttachStderr(true)
                            .withAttachStdout(true)
                            .withCmd("sh", "-c", "echo '" + userCode + "' > /usr/src/app/main.c && gcc -o /usr/src/app/main /usr/src/app/main.c && timeout " + timeLimit + "s /usr/src/app/main " + arg)
                            .withHostConfig(new HostConfig()
                                    .withMemory((long) memoryLimit)
                                    .withMemorySwap((long) memoryLimit*2)
                            );
                }
                    // 컨테이너 실행
                    String containerId = createContainerCmd.exec().getId();
                    docker.startContainerCmd(containerId).exec();

                    // 컨테이너가 실행 완료될 때까지 대기
                    docker.waitContainerCmd(containerId).exec(new WaitContainerResultCallback()).awaitStatusCode();

                    // 컨테이너의 표준 출력을 저장할 StringBuilder
                    StringBuffer outputResult = new StringBuffer();
                    StringBuffer errResult = new StringBuffer();

                    // 컨테이너 로그를 얻기 위한 코드
                    LogContainerCmd logContainerCmd = docker.logContainerCmd(containerId)
                            .withStdErr(true)
                            .withStdOut(true);

                    LogContainerResultCallback callback = new LogContainerResultCallback() {
                        @Override
                        public void onNext(Frame item) {
                            String log = item.toString();
                            if (log.startsWith("STDOUT:"))
                                outputResult.append(log.substring(8));
                            else
                                errResult.append(log);
                            super.onNext(item);
                        }

                    };

                    // 컨테이너 로그 가져오기
                    logContainerCmd.exec(callback);

                    InspectContainerResponse containerInfo = docker.inspectContainerCmd(containerId).exec();

                    exitCode = containerInfo.getState().getExitCode();
                    // 컨테이너가 종료되었으므로 결과와 표준 출력을 확인할 수 있음
                    System.out.println("Container execution completed. Exit code: " + containerInfo.getState().getExitCode());

                    // 표준 출력 확인
                    System.out.println("Container output: " + outputResult.toString());
                    System.out.println("Container errOutput : " + errResult.toString());


                    if (exitCode == 0) { //정상 종료시
                        if (expOutput.equalsIgnoreCase(outputResult.toString())) {
                            codeStatus = CodeStatus.SUCCESS;
                        } else { //정상 종료시 결과값이 다르면 fail
                            codeStatus = CodeStatus.FAIL;
                            break;
                        }
                    } else { //에러 발생시
                        if(exitCode == 137){
                            codeStatus = CodeStatus.MEMORY;
                            break;
                        } else if(exitCode == 124){
                            codeStatus = CodeStatus.TIME;
                            break;
                        }
                        else{
                            codeStatus = CodeStatus.ERROR;
                            break;
                        }
                    }
                    docker.removeContainerCmd(containerId).exec();
            }
            //testcase 실행

            //codeStatusList의 상태를 보고 attempt 수정

            Optional<Attempt> attempt = attemptRepository.findById(attemptId);



            //codeStatus 상태가 성공일 경우
            if(codeStatus == CodeStatus.SUCCESS) {
                member1.get().addCount();
                challenge.get().addCorrectCnt();
            }
            //사용자 +1
            //codeStatus 상태가 성공일 경우

            attempt.get().updateStatus(codeStatus);
            //codeStatusList의 상태를 보고 attempt 수정

            //시도한 내력 추가, 사용자/문제
            member1.get().addAttempt(attempt.get());
            challenge.get().addAttempt(attempt.get());

            ///test
            } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @Async
    public void compileAndRun1(CodeRequestDTO codeRequestDTO, Member member) throws UnsupportedEncodingException {


        String userCode = URLDecoder.decode(codeRequestDTO.getCode(), "UTF-8");
        codeRequestDTO.setCode(userCode);
        //String userCode = codeRequestDTO.getCode();
        //code 파일 저장
        String fileName = "Main." + getExtension(codeRequestDTO.getLanguage());
        String filePath = codeStoragePath + "/" + fileName;

        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(userCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //code 파일 저장


        //사용자 입력 DB 저장
        Long attemptId = saveAttempt(codeRequestDTO, member);
        CodeStatus codeStatus = CodeStatus.ERROR;
        //사용자 입력 DB 저장

        //해당 문제 testcase get
        Optional<Challenge> challenge = challengeRepository.findById(codeRequestDTO.getChallengeId());
        List<TestCase> testCaseList = challenge.get().getTestcases();
        //해당 문제 testcase get

        Optional<Member> member1 = memberRepository.findByUsername(member.getUsername());

        float memory = challenge.get().getMemory();
        float time = challenge.get().getTime();

        float memoryLimit = memory * 1024 * 1024; //MB
        float timeLimit = time; //s


        for (int i = 0; i < 3; i++) {
            String arg = testCaseList.get(i).getInput();
            String expOutput = testCaseList.get(i).getOutput();

            ExecutorService executor = Executors.newSingleThreadExecutor();

            // Future 객체를 사용하여 프로세스 실행 및 결과 수신
            Future<String> future = executor.submit(() -> {
                String output = null;
                switch (codeRequestDTO.getLanguage().toLowerCase()) {
                    case "java":
                        executeCommand("javac " + filePath);
                        output = executeCommand("java -cp " + codeStoragePath + " Main " + arg);
                        break;
                    case "python":
                        output = executeCommand("python " + filePath + " " + arg);
                        break;
                    case "c":
                        executeCommand("gcc " + filePath + " -o " + codeStoragePath + "/Main");
                        output = executeCommand(codeStoragePath + "/Main " + arg);
                        break;
                }
                return output;
            });

            try {
                String output = future.get((long) timeLimit, TimeUnit.SECONDS);  // 시간 제한 설정
                output = output.trim();
                if (output.equals(expOutput)) {
                    System.out.println("Output : " + output);
                    codeStatus = CodeStatus.SUCCESS;
                } else {
                    System.out.println("Output : " + output);
                    codeStatus = CodeStatus.FAIL;
                    break;
                }
            } catch (TimeoutException e) {
                codeStatus = CodeStatus.TIME;
                break;
            } catch (Exception e) {
                e.printStackTrace();
                codeStatus = CodeStatus.ERROR;
                break;
            } finally {
                executor.shutdownNow();
            }
        }

            //codeStatusList의 상태를 보고 attempt 수정

            Optional<Attempt> attempt = attemptRepository.findById(attemptId);


            //codeStatus 상태가 성공일 경우
            if (codeStatus == CodeStatus.SUCCESS) {
                member1.get().addCount();
                challenge.get().addCorrectCnt();
            }
            //사용자 +1
            //codeStatus 상태가 성공일 경우

            attempt.get().updateStatus(codeStatus);
            //codeStatusList의 상태를 보고 attempt 수정

            //시도한 내력 추가, 사용자/문제
            member1.get().addAttempt(attempt.get());
            challenge.get().addAttempt(attempt.get());
    }

    public String getDockerImage(String language) {

        if("c".equalsIgnoreCase(language))
            return "dbwogur36/swe:c";
        else if("python".equalsIgnoreCase(language))
            return "dbwogur36/swe:python";
        else if("java".equalsIgnoreCase(language))
            return "dbwogur36/swe:latest";

        return "";
    }

    public String getExtension(String language){
        switch (language.toLowerCase()) {
            case "python":
                return "py";
            case "java":
                return "java";
            case "c":
                return "c";
            default:
                return "";
        }
    }

    private String executeCommand(String command) {
        try {

            Process process = Runtime.getRuntime().exec(command);

            // 프로세스의 출력을 읽어옴
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder outputResult = new StringBuilder();

            // 프로세스의 에러를 읽어옴
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8));
            StringBuilder errorResult = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                outputResult.append(line).append("\n");
            }

            while ((line = errorReader.readLine()) != null) {
                errorResult.append(line).append("\n");
            }

            // 프로세스가 종료될 때까지 대기
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                return outputResult.toString();
            } else {
                System.out.println(errorResult);
                // 실행 실패 시 표준 에러 출력 추가
                return "Execution failed. Exit code: " + exitCode + "\nError output:\n" + errorResult;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }

    public List<AttemptDTO> getMemberAttempt(Member member) {
        Optional<Member> member1 = memberRepository.findByUsername(member.getUsername());
        return getAttemptListDTO(member1.get().getAttempts());
    }

    public AttemptSuccessDTO getChallengeSuccess(Member member){
        Optional<Member> byUsername = memberRepository.findById(member.getId());

        Set<Long> correctChallengeId = byUsername.get().getAttempts().stream()
                .filter(attempt -> CodeStatus.SUCCESS.equals(attempt.getCodeStatus()))
                .map(attempt -> attempt.getChallengeId().getId())
                .collect(Collectors.toCollection(TreeSet::new));

        return AttemptSuccessDTO.builder()
                .correctChallengeId(correctChallengeId)
                .build();
    }

}
