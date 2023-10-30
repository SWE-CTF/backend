package sogong.ctf.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sogong.ctf.domain.ChallengeFile;
import sogong.ctf.repository.ChallengeFileRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FileService {
    private final ChallengeFileRepository challengeFileRepository;
    public void save(List<MultipartFile> files){
        challengeFileRepository.save(new ChallengeFile());
    }

}
