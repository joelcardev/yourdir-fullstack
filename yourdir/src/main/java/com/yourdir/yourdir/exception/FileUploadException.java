package com.yourdir.yourdir.exception;


public class FileUploadException extends RuntimeException {

    public FileUploadException() {
        super("Erro durante o upload do arquivo.");
    }

    public FileUploadException(String message) {
        super("Erro durante o upload do arquivo: " + message);
    }
}
