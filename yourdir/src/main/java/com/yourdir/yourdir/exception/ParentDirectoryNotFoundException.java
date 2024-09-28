package com.yourdir.yourdir.exception;


public class ParentDirectoryNotFoundException extends RuntimeException {

    public ParentDirectoryNotFoundException(Long parentId) {
        super("Diretório pai com ID " + parentId + " não encontrado.");
    }
}
