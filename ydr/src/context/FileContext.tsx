import React, { createContext, useContext, useState, ReactNode } from "react";
import { File } from "../interfaces/file.interface";

type ModeFile = "create" | "edit" | "show";

interface FileContextType {
  files: File[];
  directoryId: number | null;
  isEditing: boolean;
  modeFile: ModeFile;
  setFiles: (files: File[] | ((prevFiles: File[]) => File[])) => void;
  setDirectoryId: (id: number | null) => void;
  setIsEditing: (isEditing: boolean) => void;
  setModeFile: (mode: ModeFile) => void;
}

const FileContext = createContext<FileContextType | undefined>(undefined);

export const FileProvider: React.FC<{ children: ReactNode }> = ({
  children,
}) => {
  const [files, setFiles] = useState<File[]>([]);
  const [directoryId, setDirectoryId] = useState<number | null>(null);
  const [isEditing, setIsEditing] = useState<boolean>(false);
  const [modeFile, setModeFile] = useState<ModeFile>("show");

  return (
    <FileContext.Provider
      value={{
        files,
        setFiles,
        directoryId,
        setDirectoryId,
        isEditing,
        setIsEditing,
        modeFile,
        setModeFile,
      }}
    >
      {children}
    </FileContext.Provider>
  );
};

export const useFileContext = () => {
  const context = useContext(FileContext);
  if (context === undefined) {
    throw new Error("useFileContext must be used within a FileProvider");
  }
  return context;
};
