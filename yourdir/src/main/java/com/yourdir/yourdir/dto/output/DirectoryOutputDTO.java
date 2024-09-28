package com.yourdir.yourdir.dto.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.yourdir.yourdir.model.Directory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirectoryOutputDTO {
    private Long id;
    private String name;
    private Long parentId;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<FileOutputDto> files;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updatedAt;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<DirectoryOutputDTO> subDirectories;


    public static DirectoryOutputDTO mapDirectoryToDTO(Directory rootDir) {
        LinkedList<Directory> stack = new LinkedList<>();
        Map<Long, DirectoryOutputDTO> dtoMap = new HashMap<>();

        stack.push(rootDir);

        while (!stack.isEmpty()) {
            Directory dir = stack.pop();
            if (dir == null) {
                continue;
            }

            List<FileOutputDto> files = dir.getFiles() == null ? Collections.emptyList() :
                    dir.getFiles().stream()
                            .map(file -> new FileOutputDto(
                                    file.getId(),
                                    file.getName(),
                                    file.getContent(),
                                    file.getDirectory() != null ? file.getDirectory().getId() : null,
                                    file.getCreatedAt(),
                                    file.getUpdatedAt()))
                            .collect(Collectors.toList());

            DirectoryOutputDTO dirDto = new DirectoryOutputDTO(
                    dir.getId(),
                    dir.getName(),
                    dir.getParentDirectory() != null ? dir.getParentDirectory().getId() : null,
                    files,
                    dir.getCreatedAt(),
                    dir.getUpdatedAt(),
                    new ArrayList<>()
            );

            dtoMap.put(dir.getId(), dirDto);
            dir.getSubDirectories().forEach(stack::push);
        }

        dtoMap.values().forEach(dirDto -> {
            if (dirDto.getParentId() != null) {
                DirectoryOutputDTO parentDto = dtoMap.get(dirDto.getParentId());
                if (parentDto != null) {
                    parentDto.getSubDirectories().add(dirDto);
                }
            }
        });

        return dtoMap.get(rootDir.getId());
    }

}
