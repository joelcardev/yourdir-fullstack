package com.yourdir.yourdir.controller;

import com.yourdir.yourdir.dto.input.CreateFileDTO;
import com.yourdir.yourdir.dto.input.EditFileDTO;
import com.yourdir.yourdir.dto.output.FileOutputDto;
import com.yourdir.yourdir.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping("/directory/{directoryId}")
    public List<FileOutputDto> getAllFiles(@PathVariable Long directoryId) {
        return fileService.getAllFilesByDirectoryId(directoryId);
    }

    @GetMapping("/{fileId}")
    public Optional<FileOutputDto> getFileById(@PathVariable Long fileId) {
        return fileService.getFileById(fileId);
    }

    @PostMapping("/directory/{directoryId}")
    public FileOutputDto createFile(@PathVariable Long directoryId, @RequestBody CreateFileDTO dto) {
        return fileService.createFile(directoryId, dto);
    }

    @PutMapping("/{fileId}")
    public FileOutputDto editFile(@PathVariable Long fileId, @RequestBody EditFileDTO dto) {
        return fileService.editFile(fileId, dto);
    }

    @DeleteMapping("/{fileId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFile(@PathVariable Long fileId) {
        fileService.deleteFile(fileId);
    }
}
