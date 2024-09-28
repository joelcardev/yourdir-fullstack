package com.yourdir.yourdir.service;

import com.yourdir.yourdir.dto.input.CreateDirectoryDTO;
import com.yourdir.yourdir.dto.output.DirectoryOutputDTO;
import com.yourdir.yourdir.exception.ParentDirectoryNotFoundException;
import com.yourdir.yourdir.model.Directory;
import com.yourdir.yourdir.repository.DirectoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DirectoryService {

    @Autowired
    private DirectoryRepository directoryRepository;

    @Cacheable(value = "directories")
    public List<DirectoryOutputDTO> getAllDirectories() {
        List<Directory> allDirectories = directoryRepository.findAll();

        return allDirectories.stream()
                .filter(dir -> dir.getParentDirectory() == null)
                .map(DirectoryOutputDTO::mapDirectoryToDTO)
                .collect(Collectors.toList());
    }


    @Cacheable(value = "directory", key = "#id")
    public Optional<DirectoryOutputDTO> getDirectoryById(Long id) {
        return directoryRepository.findById(id)
                .map(DirectoryOutputDTO::mapDirectoryToDTO);
    }


    @CacheEvict(value = "directories", allEntries = true)
    public DirectoryOutputDTO createDirectory(CreateDirectoryDTO dto) {
        Directory directory = new Directory();
        directory.setName(dto.name());

        if (dto.parentId() != null) {
            Directory parentDirectory = directoryRepository.findById(dto.parentId())
                    .orElseThrow(() -> new ParentDirectoryNotFoundException(dto.parentId()));
            directory.setParentDirectory(parentDirectory);
        }

        return new DirectoryOutputDTO(
                directoryRepository.save(directory).getId(),
                directory.getName(),
                directory.getParentDirectory() != null ? directory.getParentDirectory().getId() : null,
                Collections.emptyList(),
                directory.getCreatedAt(),
                directory.getUpdatedAt(),
                Collections.emptyList()
        );
    }

    @CacheEvict(value = "directories", allEntries = true)
    public void deleteDirectory(Long id) {
        directoryRepository.deleteById(id);
    }
}

