package com.yourdir.yourdir.exception;

public class FileNotFoundException extends RuntimeException {

    public FileNotFoundException(Long fileId) {
        super("Arquivo com ID " + fileId + " não encontrado.");
    }

    public FileNotFoundException(String fileName) {
        super("Arquivo com o nome '" + fileName + "' não encontrado.");
    }
}
