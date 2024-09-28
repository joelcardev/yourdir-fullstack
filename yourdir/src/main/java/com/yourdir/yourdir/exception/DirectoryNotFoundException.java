package com.yourdir.yourdir.exception;

public class DirectoryNotFoundException extends RuntimeException
{

    public DirectoryNotFoundException() {
        super("Diretorio não encontrado.");
    }

}