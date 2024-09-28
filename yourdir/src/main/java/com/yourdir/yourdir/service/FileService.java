package com.yourdir.yourdir.service;

import com.yourdir.yourdir.dto.input.CreateFileDTO;
import com.yourdir.yourdir.dto.input.EditFileDTO;
import com.yourdir.yourdir.dto.output.FileOutputDto;
import com.yourdir.yourdir.exception.DirectoryNotFoundException;
import com.yourdir.yourdir.exception.FileNotFoundException;
import com.yourdir.yourdir.model.Directory;
import com.yourdir.yourdir.model.File;
import com.yourdir.yourdir.repository.DirectoryRepository;
import com.yourdir.yourdir.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private DirectoryRepository directoryRepository;

    @Cacheable(value = "files", key = "#directoryId")
    public List<FileOutputDto> getAllFilesByDirectoryId(Long directoryId) {
        return fileRepository.findByDirectoryId(directoryId).stream()
                .map(file -> new FileOutputDto(file.getId(), file.getName(), file.getContent(), file.getDirectory().getId(), file.getCreatedAt(), file.getUpdatedAt()))
                .collect(Collectors.toList());
    }

    @Cacheable(value = "files", key = "#id")
    public Optional<FileOutputDto> getFileById(Long id) {
        return fileRepository.findById(id)
                .map(file -> new FileOutputDto(file.getId(), file.getName(), file.getContent(), file.getDirectory().getId(), file.getCreatedAt(), file.getUpdatedAt()));
    }

    @Caching(evict = {
            @CacheEvict(value = "files", key = "#directoryId"),
            @CacheEvict(value = "directories", allEntries = true)
    })
    public FileOutputDto createFile(Long directoryId, CreateFileDTO dto) {
        Directory directory = directoryRepository.findById(directoryId)
                .orElseThrow(DirectoryNotFoundException::new);

        File file = new File();
        file.setName(dto.name());
        file.setContent(dto.content());
        file.setDirectory(directory);

        File savedFile = fileRepository.save(file);
        return new FileOutputDto(savedFile.getId(), savedFile.getName(), savedFile.getContent(),
                savedFile.getDirectory().getId(), savedFile.getCreatedAt(), savedFile.getUpdatedAt());
    }

    @CacheEvict(value = "files", key = "#id")
    public FileOutputDto editFile(Long id, EditFileDTO dto) {
        File file = fileRepository.findById(id)
                .orElseThrow(() -> new FileNotFoundException(id));

        file.setName(dto.name());
        file.setContent(dto.content());

        File updatedFile = fileRepository.save(file);
        return new FileOutputDto(updatedFile.getId(), updatedFile.getName(), updatedFile.getContent(),
                updatedFile.getDirectory().getId(), updatedFile.getCreatedAt(), updatedFile.getUpdatedAt());
    }

    @CacheEvict(value = "files", key = "#id")
    public void deleteFile(Long id) {
        if (!fileRepository.existsById(id)) {
            throw new FileNotFoundException(id);
        }
        fileRepository.deleteById(id);
    }
}
