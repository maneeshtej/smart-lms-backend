package com.lms.services;

import com.lms.models.FileEntity;
import com.lms.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;


@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

   public FileEntity saveFile(MultipartFile multipartFile, Long courseId) throws Exception {
        Path dir = Paths.get(uploadDir);
        if (!Files.exists(dir)) Files.createDirectories(dir);

        String fileName = multipartFile.getOriginalFilename();
        Path filePath = dir.resolve(fileName);
        Files.copy(multipartFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName(fileName);
        fileEntity.setFilePath(filePath.toString());
        fileEntity.setCourseId(courseId);
        fileEntity.setUploadedBy("system"); // temporary value

        return fileRepository.save(fileEntity);
    }




    public List<FileEntity> getFilesByCourse(Long courseId) {
        return fileRepository.findByCourseId(courseId);
    }

    public void deleteAllFiles() throws IOException {
        // 1. Delete all file records from DB
        fileRepository.deleteAll();

        // 2. Delete all files from the upload directory
        Path dir = Paths.get(uploadDir);
        if (Files.exists(dir)) {
            Files.walk(dir)
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }
    }

}
