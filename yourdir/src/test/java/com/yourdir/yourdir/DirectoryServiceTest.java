package com.yourdir.yourdir;

import com.yourdir.yourdir.dto.input.CreateDirectoryDTO;
import com.yourdir.yourdir.dto.output.DirectoryOutputDTO;
import com.yourdir.yourdir.model.Directory;
import com.yourdir.yourdir.repository.DirectoryRepository;
import com.yourdir.yourdir.service.DirectoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DirectoryServiceTest {

    private static final Long ROOT_DIRECTORY_ID = 1L;
    private static final Long CHILD_DIRECTORY_ID = 2L;
    private static final String ROOT_DIRECTORY_NAME = "Root Directory";
    private static final String CHILD_DIRECTORY_NAME = "Child Directory";
    private static final String NEW_DIRECTORY_NAME = "New Directory";

    private Directory rootDirectory;
    private Directory childDirectory;
    private CreateDirectoryDTO createDirectoryDTOWithoutParent;
    private CreateDirectoryDTO createDirectoryDTOWithParent;

    @InjectMocks
    private DirectoryService directoryService;

    @Mock
    private DirectoryRepository directoryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        setupDirectories();
        setupDTOs();
    }

    private void setupDirectories() {
        rootDirectory = new Directory();
        rootDirectory.setId(ROOT_DIRECTORY_ID);
        rootDirectory.setName(ROOT_DIRECTORY_NAME);
        rootDirectory.setParentDirectory(null);

        childDirectory = new Directory();
        childDirectory.setId(CHILD_DIRECTORY_ID);
        childDirectory.setName(CHILD_DIRECTORY_NAME);
        childDirectory.setParentDirectory(rootDirectory);
    }

    private void setupDTOs() {
        createDirectoryDTOWithoutParent = mock(CreateDirectoryDTO.class);
        when(createDirectoryDTOWithoutParent.name()).thenReturn(NEW_DIRECTORY_NAME);
        when(createDirectoryDTOWithoutParent.parentId()).thenReturn(null);

        createDirectoryDTOWithParent = mock(CreateDirectoryDTO.class);
        when(createDirectoryDTOWithParent.name()).thenReturn(CHILD_DIRECTORY_NAME);
        when(createDirectoryDTOWithParent.parentId()).thenReturn(ROOT_DIRECTORY_ID);
    }

    @Test
    void testGetAllDirectories() {
        // Criar o objeto rootDirectory e inicializar as listas
        Directory rootDirectory = new Directory();
        rootDirectory.setId(ROOT_DIRECTORY_ID);
        rootDirectory.setName(ROOT_DIRECTORY_NAME);
        rootDirectory.setFiles(Collections.emptyList()); // Inicializando a lista de arquivos

        // Inicializa a lista de subDiretórios como vazia
        rootDirectory.setSubDirectories(new ArrayList<>()); // Garantindo que a lista de subDiretórios não é nula

        when(directoryRepository.findAll()).thenReturn(List.of(rootDirectory));

        List<DirectoryOutputDTO> directories = directoryService.getAllDirectories();

        assertNotNull(directories);
        assertEquals(1, directories.size());
        assertEquals(ROOT_DIRECTORY_NAME, directories.getFirst().getName());
        verify(directoryRepository, times(1)).findAll();
    }


    @Test
    void testGetDirectoryById() {
        when(directoryRepository.findById(ROOT_DIRECTORY_ID)).thenReturn(Optional.of(rootDirectory));

        Optional<DirectoryOutputDTO> result = directoryService.getDirectoryById(ROOT_DIRECTORY_ID);

        assertTrue(result.isPresent());
        assertEquals(ROOT_DIRECTORY_NAME, result.get().getName());
        verify(directoryRepository, times(1)).findById(ROOT_DIRECTORY_ID);
    }

    @Test
    void testCreateDirectoryWithoutParent() {
        when(directoryRepository.save(any(Directory.class))).thenReturn(rootDirectory);

        DirectoryOutputDTO result = directoryService.createDirectory(createDirectoryDTOWithoutParent);

        assertNotNull(result);
        assertEquals(NEW_DIRECTORY_NAME, result.getName());
        assertNull(result.getParentId());
        verify(directoryRepository, times(1)).save(any(Directory.class));
    }

    @Test
    void testCreateDirectoryWithParent() {
        when(directoryRepository.findById(ROOT_DIRECTORY_ID)).thenReturn(Optional.of(rootDirectory));
        when(directoryRepository.save(any(Directory.class))).thenReturn(childDirectory);

        DirectoryOutputDTO result = directoryService.createDirectory(createDirectoryDTOWithParent);

        assertNotNull(result);
        assertEquals(CHILD_DIRECTORY_NAME, result.getName());
        assertEquals(ROOT_DIRECTORY_ID, result.getParentId());
        verify(directoryRepository, times(1)).findById(ROOT_DIRECTORY_ID);
        verify(directoryRepository, times(1)).save(any(Directory.class));
    }

    @Test
    void testCreateDirectoryThrowsExceptionWhenParentNotFound() {
        when(directoryRepository.findById(ROOT_DIRECTORY_ID)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            directoryService.createDirectory(createDirectoryDTOWithParent);
        });

        assertEquals("Diretório pai com ID 1 não encontrado.", exception.getMessage());
        verify(directoryRepository, times(1)).findById(ROOT_DIRECTORY_ID);
    }

    @Test
    void testDeleteDirectory() {
        doNothing().when(directoryRepository).deleteById(ROOT_DIRECTORY_ID);

        directoryService.deleteDirectory(ROOT_DIRECTORY_ID);

        verify(directoryRepository, times(1)).deleteById(ROOT_DIRECTORY_ID);
    }
}

