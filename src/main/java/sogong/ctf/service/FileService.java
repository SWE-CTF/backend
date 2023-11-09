package sogong.ctf.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sogong.ctf.domain.File;
import sogong.ctf.repository.FileRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FileService {
    private final FileRepository fileRepository;
    public void save(List<MultipartFile> files){
        fileRepository.save(new File());
    }

}
