package com.lms.controllers;

import com.lms.models.FileEntity;
import com.lms.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "*")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("courseId") Long courseId) {

        try {
            // Save file locally or to DB
            var savedFile = fileService.saveFile(file, courseId);

            // RAG service removed — only handle file persistence now
            return ResponseEntity.ok(Map.of(
                    "status", "ok",
                    "message", "File uploaded successfully.",
                    "courseId", courseId,
                    "fileName", savedFile.getFileName()
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/{courseId}/files")
    public List<FileEntity> getFiles(@PathVariable Long courseId) {
        return fileService.getFilesByCourse(courseId);
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<?> deleteAllFiles() {
        try {
            fileService.deleteAllFiles();
            return ResponseEntity.ok(Map.of(
                    "status", "ok",
                    "message", "All files deleted successfully from database and uploads folder."
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                    "status", "error",
                    "message", "Failed to delete files: " + e.getMessage()
            ));
        }
    }
}
