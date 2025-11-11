package com.lms.controllers;

import com.lms.models.FileEntity;
import com.lms.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.lms.services.RagService;
import java.io.File;
import org.springframework.http.ResponseEntity;



@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "*")
public class FileController {

    private final RagService ragService;

    public FileController(FileService fileService, RagService ragService) {
        this.fileService = fileService;
        this.ragService = ragService;
    }

    @Autowired
    private FileService fileService;

   @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("courseId") Long courseId) {

        try {
            // Save file locally first
            var savedFile = fileService.saveFile(file, courseId);

            // Trigger Flask ingestion + indexing
            String ragResponse = ragService.sendToRag(new File(savedFile.getFilePath()), courseId);

            return ResponseEntity.ok(Map.of(
                    "status", "ok",
                    "message", "File uploaded and sent to Flask RAG successfully.",
                    "courseId", courseId,
                    "ragResponse", ragResponse
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
