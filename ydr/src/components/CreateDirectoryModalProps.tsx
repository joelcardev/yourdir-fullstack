import React, { useCallback } from "react";
import { Modal, Box, Typography, TextField, Button } from "@mui/material";
import { useDirectory } from "../context/DirectoryContext";
import { Directory } from "../interfaces/diretory.inteface";

interface CreateDirectoryModalProps {
  open: boolean;
  onClose: () => void;
  newDirectoryName: string;
  setNewDirectoryName: (name: string) => void;
  onCreateDirectory: (
    parentId: number | null,
    name: string
  ) => Promise<Directory>;
  parentId: number | null;
}

const CreateDirectoryModal: React.FC<CreateDirectoryModalProps> = ({
  open,
  onClose,
  newDirectoryName,
  setNewDirectoryName,
  onCreateDirectory,
  parentId,
}) => {
  const { setDirectories } = useDirectory();

  const updateDirectories = useCallback(
    (
      dirs: Directory[],
      parentId: number | null,
      newDir: Directory
    ): Directory[] => {
      return dirs.map((dir) => {
        if (dir.id === parentId) {
          const subDirAlreadyExists = dir.subDirectories?.some(
            (subDir) => subDir.id === newDir.id
          );

          if (subDirAlreadyExists) {
            return dir;
          }

          return {
            ...dir,
            subDirectories: [...(dir.subDirectories || []), newDir],
          };
        } else if (dir.subDirectories && dir.subDirectories.length > 0) {
          return {
            ...dir,
            subDirectories: updateDirectories(
              dir.subDirectories,
              parentId,
              newDir
            ),
          };
        }
        return dir;
      });
    },
    []
  );

  const handleCreate = useCallback(async () => {
    if (newDirectoryName.trim() === "") return;
    const newDirectory = await onCreateDirectory(parentId, newDirectoryName);

    setDirectories((prevDirectories) =>
      updateDirectories(prevDirectories, parentId, newDirectory)
    );
    setNewDirectoryName("");
    onClose();
  }, [
    newDirectoryName,
    onCreateDirectory,
    parentId,
    setDirectories,
    setNewDirectoryName,
    onClose,
    updateDirectories,
  ]);

  return (
    <Modal open={open} onClose={onClose}>
      <Box
        sx={{
          position: "absolute",
          top: "50%",
          left: "50%",
          transform: "translate(-50%, -50%)",
          bgcolor: "background.paper",
          border: "2px solid #000",
          boxShadow: 24,
          p: 4,
          borderRadius: 2,
        }}
      >
        <Typography variant="h6" component="h2">
          Create New Directory
        </Typography>
        <TextField
          label="Directory Name"
          value={newDirectoryName}
          onChange={(e) => setNewDirectoryName(e.target.value)}
          fullWidth
          sx={{ marginTop: 2 }}
        />
        <Box sx={{ marginTop: 2, textAlign: "right" }}>
          <Button variant="outlined" onClick={onClose} sx={{ marginRight: 1 }}>
            Cancel
          </Button>
          <Button variant="contained" onClick={handleCreate}>
            Create
          </Button>
        </Box>
      </Box>
    </Modal>
  );
};

export default React.memo(CreateDirectoryModal);
