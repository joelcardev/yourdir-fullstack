package com.yourdir.yourdir.repository;

import com.yourdir.yourdir.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    List<File> findByDirectoryId(Long directoryId);
}