import React, { useEffect } from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  TextField,
  Box,
} from "@mui/material";
import { File } from "../interfaces/file.interface";
import { editFile, uploadFile } from "../services/file.service";
import { useFileContext } from "../context/FileContext";
import { useDirectory } from "../context/DirectoryContext";
import { Directory } from "../interfaces/diretory.inteface";

interface FileInfoModalProps {
  open: boolean;
  onClose: () => void;
}

const FileInfoModal: React.FC<FileInfoModalProps> = ({ open, onClose }) => {
  const { isEditing, setIsEditing, directoryId, setFiles, modeFile } =
    useFileContext();
  const { selectedFile, setSelectedFile, setDirectories, directories } =
    useDirectory();

  const [content, setContent] = React.useState<string>("");
  const [name, setName] = React.useState<string>("");

  useEffect(() => {
    if (selectedFile) {
      setName(selectedFile.name || "");
      setContent(selectedFile.content || "");
    } else {
      setName("");
      setContent("");
    }
  }, [selectedFile]);

  const updateFilesInDirectory = (
    directories: Directory[],
    directoryId: number,
    updatedFile: File
  ): Directory[] => {
    return directories.map((directory: Directory) => {
      if (directory.id === directoryId) {
        const updatedFiles = directory.files
          ? [
              ...directory.files.filter((file) => file.id !== updatedFile.id),
              updatedFile,
            ]
          : [updatedFile];
        return { ...directory, files: updatedFiles };
      }

      if (directory.subDirectories) {
        return {
          ...directory,
          subDirectories: updateFilesInDirectory(
            directory.subDirectories,
            directoryId,
            updatedFile
          ),
        };
      }

      return directory;
    });
  };

  const handleSave = async () => {
    const fileId = selectedFile ? selectedFile.id : null;

    if (modeFile === "create" && !selectedFile?.directoryId) {
      return;
    }

    const newFile: File = {
      id: fileId || Date.now(),
      name,
      content,
      directoryId: selectedFile?.directoryId ?? null,
    };

    try {
      if (modeFile === "edit" && selectedFile?.id) {
        await editFile(selectedFile.id, newFile);
        setFiles((prevFiles: File[]) =>
          prevFiles.map((file) =>
            file.id === selectedFile.id ? newFile : file
          )
        );
        setDirectories((prevDirectories) =>
          prevDirectories
            ? updateFilesInDirectory(
                prevDirectories,
                selectedFile.directoryId!,
                newFile
              )
            : []
        );
      } else if (modeFile === "create") {
        const uploadedFile = await uploadFile(newFile);
        setFiles((prevFiles: File[]) => [...prevFiles, uploadedFile]);

        if (uploadedFile.directoryId !== null) {
          setDirectories((prevDirectories) =>
            prevDirectories
              ? updateFilesInDirectory(
                  prevDirectories,
                  uploadedFile.directoryId!,
                  uploadedFile
                )
              : []
          );
        }
      }

      setSelectedFile(null);
      setIsEditing(false);
    } catch (error) {
      console.error("Error uploading file:", error);
    }
  };

  const handleClose = () => {
    setIsEditing(false);
    setSelectedFile(null);
  };

  return (
    <Dialog open={isEditing} onClose={handleClose}>
      <DialogTitle>
        {modeFile === "create" ? "Create File" : selectedFile?.name}
      </DialogTitle>
      <DialogContent>
        <TextField
          label="File Name"
          fullWidth
          value={name}
          onChange={(e) => setName(e.target.value)}
          disabled={!isEditing}
        />
        <Box height={10}></Box>
        <TextField
          multiline
          fullWidth
          value={content}
          onChange={(e) => setContent(e.target.value)}
          disabled={!isEditing}
        />
      </DialogContent>
      <DialogActions>
        {modeFile === "create" ? (
          <>
            <Button onClick={handleSave}>Save</Button>
            <Button onClick={handleClose}>Cancel</Button>
          </>
        ) : (
          <>
            <Button onClick={handleSave}>Save Changes</Button>
            <Button onClick={handleClose}>Close</Button>
          </>
        )}
      </DialogActions>
    </Dialog>
  );
};

export default React.memo(FileInfoModal);
