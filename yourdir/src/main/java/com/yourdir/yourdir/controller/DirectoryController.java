package com.yourdir.yourdir.controller;

import com.yourdir.yourdir.dto.input.CreateDirectoryDTO;
import com.yourdir.yourdir.dto.output.DirectoryOutputDTO;
import com.yourdir.yourdir.service.DirectoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/directories")
public class DirectoryController {

    @Autowired
    private DirectoryService directoryService;

    @GetMapping
    public List<DirectoryOutputDTO> getAllDirectories() {
        return directoryService.getAllDirectories();
    }

    @PostMapping
    public DirectoryOutputDTO createDirectory(@RequestBody CreateDirectoryDTO dto) {
        return directoryService.createDirectory(dto);
    }

    @DeleteMapping("/{id}")
    public void deleteDirectory(@PathVariable Long id) {
        directoryService.deleteDirectory(id);
    }
}
