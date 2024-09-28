import React, { createContext, useState, useContext, ReactNode } from 'react';
import { Directory } from '../interfaces/diretory.inteface';
import { File } from '../interfaces/file.interface';

interface DirectoryContextType {
  directories: Directory[];
  setDirectories: React.Dispatch<React.SetStateAction<Directory[]>>;
  selectedFile: File | null;
  setSelectedFile: React.Dispatch<React.SetStateAction<File | null>>;
}

const DirectoryContext = createContext<DirectoryContextType | undefined>(undefined);

export const useDirectory = () => {
  const context = useContext(DirectoryContext);
  if (!context) {
    throw new Error('useDirectory deve ser usado dentro de um DirectoryProvider');
  }
  return context;
};

export const DirectoryProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [directories, setDirectories] = useState<Directory[]>([]);
  const [selectedFile, setSelectedFile] = useState<File | null>(null);

  return (
    <DirectoryContext.Provider value={{ directories, setDirectories, selectedFile, setSelectedFile }}>
      {children}
    </DirectoryContext.Provider>
  );
};
