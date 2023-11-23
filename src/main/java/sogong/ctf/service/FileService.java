package sogong.ctf.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sogong.ctf.domain.Challenge;
import sogong.ctf.domain.ChallengeFile;
import sogong.ctf.repository.ChallengeFileRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class FileService {
    private final ChallengeFileRepository challengeFileRepository;
    @Value("${user.file.path}")
    private  String path;

    public void save(List<MultipartFile> files, Challenge challenge) {
        System.out.println(path);
        System.out.println(files.size());
        for (MultipartFile file : files) {
            String storedFileName = createStoredFileName(file);
            ChallengeFile f = ChallengeFile.builder().originalFileName(file.getOriginalFilename())
                    .storedFileName(storedFileName)
                    .challengeId(challenge)
                    .build();
            try {
                file.transferTo(new File(path +"//"+ storedFileName));
            } catch (IOException e) {
                log.error("파일 저장 실패");
            }
            challengeFileRepository.save(f);
        }
    }

    private static String createStoredFileName(MultipartFile file) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(file);
        return uuid + ext;
    }

    private static String extractExt(MultipartFile file) {
        return file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
    }

    public List<byte[]> getFiles(Challenge challengeId){
        List<ChallengeFile> allByChallengeId = challengeFileRepository.findAllByChallengeId(challengeId);
        List<byte[]> files = new ArrayList<>();
        for (ChallengeFile challengeFile : allByChallengeId) {
            try(InputStream inputStream = new FileInputStream(path+"//"+challengeFile.getStoredFileName())){
                byte[] byteArr = inputStream.readAllBytes();
                files.add(byteArr);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return files;
    }
}
