package com.yourdir.yourdir;

import com.yourdir.yourdir.dto.input.CreateFileDTO;
import com.yourdir.yourdir.dto.input.EditFileDTO;
import com.yourdir.yourdir.dto.output.FileOutputDto;
import com.yourdir.yourdir.model.Directory;
import com.yourdir.yourdir.model.File;
import com.yourdir.yourdir.repository.DirectoryRepository;
import com.yourdir.yourdir.repository.FileRepository;
import com.yourdir.yourdir.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.CacheManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FileServiceTest {

    private static final Long DIRECTORY_ID = 1L;
    private static final Long FILE_ID = 1L;
    private static final String FILE_NAME = "Test File";
    private static final String FILE_CONTENT = "Test Content";
    private static final String NEW_FILE_NAME = "New File";
    private static final String NEW_FILE_CONTENT = "File Content";
    private static final String UPDATED_FILE_NAME = "Updated File";
    private static final String UPDATED_FILE_CONTENT = "Updated Content";
    private static final String DIRECTORY_NOT_FOUND_MESSAGE = "Diretorio não encontrado.";
    private static final String FILE_NOT_FOUND_MESSAGE = "Arquivo com ID " + FILE_ID + " não encontrado.";

    @InjectMocks
    private FileService fileService;

    @Mock
    private FileRepository fileRepository;

    @Mock
    private DirectoryRepository directoryRepository;

    @Mock
    private CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllFilesByDirectoryId() {
        File file = new File();
        file.setId(FILE_ID);
        file.setName(FILE_NAME);
        file.setContent(FILE_CONTENT);
        file.setDirectory(new Directory());

        when(fileRepository.findByDirectoryId(DIRECTORY_ID)).thenReturn(List.of(file));

        List<FileOutputDto> files = fileService.getAllFilesByDirectoryId(DIRECTORY_ID);

        assertNotNull(files);
        assertEquals(1, files.size());
        assertEquals(FILE_NAME, files.getFirst().name());
        verify(fileRepository, times(1)).findByDirectoryId(DIRECTORY_ID);
    }

    @Test
    void testGetFileById() {
        Directory directory = new Directory();
        directory.setId(DIRECTORY_ID);

        File file = new File();
        file.setId(FILE_ID);
        file.setName(FILE_NAME);
        file.setContent(FILE_CONTENT);
        file.setDirectory(directory);

        when(fileRepository.findById(FILE_ID)).thenReturn(Optional.of(file));

        Optional<FileOutputDto> result = fileService.getFileById(FILE_ID);

        assertTrue(result.isPresent());
        assertEquals(FILE_NAME, result.get().name());
        assertEquals(DIRECTORY_ID, result.get().directoryId());
        verify(fileRepository, times(1)).findById(FILE_ID);
    }

    @Test
    void testCreateFile() {
        CreateFileDTO dto = mock(CreateFileDTO.class);
        when(dto.name()).thenReturn(NEW_FILE_NAME);
        when(dto.content()).thenReturn(NEW_FILE_CONTENT);

        Directory directory = new Directory();
        directory.setId(DIRECTORY_ID);
        when(directoryRepository.findById(DIRECTORY_ID)).thenReturn(Optional.of(directory));

        File file = new File();
        file.setId(FILE_ID);
        file.setName(NEW_FILE_NAME);
        file.setContent(NEW_FILE_CONTENT);
        file.setDirectory(directory);

        when(fileRepository.save(any(File.class))).thenReturn(file);

        FileOutputDto result = fileService.createFile(DIRECTORY_ID, dto);

        assertNotNull(result);
        assertEquals(NEW_FILE_NAME, result.name());
        assertEquals(DIRECTORY_ID, result.directoryId());
        verify(fileRepository, times(1)).save(any(File.class));
        verify(directoryRepository, times(1)).findById(DIRECTORY_ID);
    }

    @Test
    void testCreateFileThrowsExceptionWhenDirectoryNotFound() {
        CreateFileDTO dto = mock(CreateFileDTO.class);
        when(directoryRepository.findById(DIRECTORY_ID)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            fileService.createFile(DIRECTORY_ID, dto);
        });

        assertEquals(DIRECTORY_NOT_FOUND_MESSAGE, exception.getMessage());
        verify(directoryRepository, times(1)).findById(DIRECTORY_ID);
        verify(fileRepository, times(0)).save(any(File.class));
    }

    @Test
    void testEditFile() {
        Directory directory = new Directory();
        directory.setId(DIRECTORY_ID);

        File file = new File();
        file.setId(FILE_ID);
        file.setName("Old File");
        file.setContent("Old Content");
        file.setDirectory(directory);

        EditFileDTO dto = mock(EditFileDTO.class);
        when(dto.name()).thenReturn(UPDATED_FILE_NAME);
        when(dto.content()).thenReturn(UPDATED_FILE_CONTENT);

        when(fileRepository.findById(FILE_ID)).thenReturn(Optional.of(file));
        when(fileRepository.save(any(File.class))).thenReturn(file);

        FileOutputDto result = fileService.editFile(FILE_ID, dto);

        assertNotNull(result);
        assertEquals(UPDATED_FILE_NAME, result.name());
        assertEquals(UPDATED_FILE_CONTENT, result.content());
        assertEquals(DIRECTORY_ID, result.directoryId());
        verify(fileRepository, times(1)).findById(FILE_ID);
        verify(fileRepository, times(1)).save(file);
    }

    @Test
    void testEditFileThrowsExceptionWhenFileNotFound() {
        EditFileDTO dto = mock(EditFileDTO.class);
        when(fileRepository.findById(FILE_ID)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            fileService.editFile(FILE_ID, dto);
        });

        assertEquals(FILE_NOT_FOUND_MESSAGE, exception.getMessage());
        verify(fileRepository, times(1)).findById(FILE_ID);
        verify(fileRepository, times(0)).save(any(File.class));
    }

    @Test
    void testDeleteFile() {
        when(fileRepository.existsById(FILE_ID)).thenReturn(true);
        doNothing().when(fileRepository).deleteById(FILE_ID);

        fileService.deleteFile(FILE_ID);

        verify(fileRepository, times(1)).deleteById(FILE_ID);
    }

    @Test
    void testDeleteFileThrowsExceptionWhenFileNotFound() {
        when(fileRepository.existsById(FILE_ID)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            fileService.deleteFile(FILE_ID);
        });

        assertEquals(FILE_NOT_FOUND_MESSAGE, exception.getMessage());
        verify(fileRepository, times(1)).existsById(FILE_ID);
        verify(fileRepository, times(0)).deleteById(FILE_ID);
    }
}
