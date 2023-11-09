package sogong.ctf.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sogong.ctf.domain.*;
import sogong.ctf.dto.CodeRequestDTO;
import sogong.ctf.repository.AttemptRepository;
import sogong.ctf.repository.ChallengeRepository;
import sogong.ctf.repository.TestCaseRepository;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttemptService {

    private final AttemptRepository attemptRepository;
    private final ChallengeRepository challengeRepository;

    public List<Attempt> getChallengeAttempt(Long id) {
        Optional<Challenge> challenge = challengeRepository.findById(id);
        return challenge.get().getAttempts();
    }

    @Transactional
    public Long saveAttempt(CodeRequestDTO codeRequestDTO, Member member) {

        Optional<Challenge> challenge = challengeRepository.findById(codeRequestDTO.getChallengeID());

        Attempt attempt = Attempt.builder()
                .member(member)
                .code(codeRequestDTO.getCode())
                .time(0)
                .memory(0)
                .codeStatus(CodeStatus.READY)
                .challenge(challenge.get())
                .build();

        return attemptRepository.save(attempt).getId();
    }

    @Transactional
    @Async
    public void compileAndRun(CodeRequestDTO codeRequestDTO, Member member) {

        /*
        Long attemptId = saveAttempt(codeRequestDTO, member); //attempt 초기 저장
        Optional<Challenge> challenge = challengeRepository.findById(codeRequestDTO.getChallengeID());

        List<TestCase> testCaseList = challenge.get().getTestCaseList();
        List<CodeStatus> codeStatusList = new ArrayList<>();

        DockerClient dockerClient = DockerClientBuilder.getInstance().build();
        String image = getDockerImage(codeRequestDTO.getLanguage());

        CreateContainerResponse container = dockerClient.createContainerCmd(image).withHostConfig(HostConfig.newHostConfig().withPortBindings()).exec();

        dockerClient.startContainerCmd(container.getId()).exec();
        String containerId = container.getId();


        for(int i = 0; i < testCaseList.size(); i++){
            String command = codeRequestDTO.getCode() + " " + testCaseList.get(i).getInput();
            String expectOutput = testCaseList.get(i).getOutput();

            ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(containerId).withAttachStdout(true).withAttachStderr(true).withCmd("sh","-c",command).exec();

            ByteArrayOutputStream stdout = new ByteArrayOutputStream();
            ByteArrayOutputStream stderr = new ByteArrayOutputStream();

            try{
                dockerClient.execStartCmd(execCreateCmdResponse.getId())
                        .withTty(true)
                        .exec(new ExecStartResultCallback(stdout,stderr))
                        .awaitCompletion();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            String output = stdout.toString(StandardCharsets.UTF_8);
            String errorOutput = stderr.toString(StandardCharsets.UTF_8);

            if(output.equalsIgnoreCase(expectOutput)){
                //memory , time 조건 확인
            } else{
                codeStatusList.add(CodeStatus.FAIL);
            }

        }

        dockerClient.stopContainerCmd(containerId).exec();
        dockerClient.removeContainerCmd(containerId).exec();

        Optional<Attempt> attempt = attemptRepository.findById(attemptId);

        for(int i = 0; i < codeStatusList.size(); i++){
            //확인해보고는 상태 선정

        }
        attempt.get().updateStatus();
        challenge.get().addAttempt(attempt.get()); //해당 문제에 시도이력 추가
        member.addAttempt(attempt.get()); //해당 사용자에게 시도이력 추가
*/
    }

    private String getDockerImage(String language) {

        if("c".equalsIgnoreCase(language))
            return "c-image";
        else if("python".equalsIgnoreCase(language))
            return "python-image";
        else if("java".equalsIgnoreCase(language))
            return "java-image";

        return "";
    }


    public List<Attempt> getMemberAttempt(Member member) {
        return member.getAttempts();
    }
}
