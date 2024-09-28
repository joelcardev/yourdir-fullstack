import React, { useCallback, useEffect, useState } from "react";
import {
  Container,
  Typography,
  Paper,
  CircularProgress,
  Button,
  Box,
} from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import DirectoryList from "./components/DirectoryList";
import { CreateDirectoryDTO, Directory } from "./interfaces/diretory.inteface";
import {
  createDirectory,
  deleteDirectory,
  fetchDirectories,
} from "./services/diretory.service";
import "./app.css";
import FolderIcon from "@mui/icons-material/Folder";

import CreateDirectoryModal from "./components/CreateDirectoryModalProps";
import { useDirectory } from "./context/DirectoryContext";
import { File } from "./interfaces/file.interface";

const YourdirApp: React.FC = () => {
  const { setDirectories, setSelectedFile } = useDirectory();

  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [openCreateModal, setOpenCreateModal] = useState<boolean>(false);
  const [newDirectoryName, setNewDirectoryName] = useState<string>("");

  useEffect(() => {
    const getDirectories = async () => {
      setLoading(true);
      try {
        const data = await fetchDirectories();
        setDirectories(data);
      } catch {
        setError("Failed to load directories.");
      } finally {
        setLoading(false);
      }
    };

    getDirectories();
  }, [setDirectories]);

  const handleCreateDirectory = useCallback(async (): Promise<Directory> => {
    if (!newDirectoryName)
      return {
        id: -1,
        name: "Default directory",
        subDirectories: [],
        files: [],
        parentId: null,
      };

    const newDirectory: CreateDirectoryDTO = { name: newDirectoryName };

    try {
      const createdDirectory = await createDirectory(newDirectory);
      setDirectories((prev) => [...prev, createdDirectory]);
      resetModal();
      return createdDirectory;
    } catch {
      setError("Failed to create directory.");
      return {
        id: -1,
        name: "Default Subdirectory",
        subDirectories: [],
        files: [],
        parentId: null,
      };
    }
  }, [newDirectoryName, setDirectories]);

  const handleDeleteDirectory = useCallback(
    async (id: number) => {
      try {
        await deleteDirectory(id);
        setDirectories((prev) => {
          const filterDirectories = (dirs: Directory[]): Directory[] =>
            dirs.reduce((acc: Directory[], dir: Directory) => {
              if (dir.id !== id) {
                const updatedSubDirs = filterDirectories(
                  dir.subDirectories || []
                );
                acc.push({
                  ...dir,
                  subDirectories:
                    updatedSubDirs.length > 0 ? updatedSubDirs : undefined,
                });
              }
              return acc;
            }, []);
          return filterDirectories(prev);
        });
      } catch {
        setError("Failed to delete directory.");
      }
    },
    [setDirectories]
  );

  const handleCreateSubdirectory = useCallback(
    async (parentId: number, name: string): Promise<Directory> => {
      try {
        const createdSubdirectory: Directory = await createDirectory({
          name,
          parentId,
        });

        setDirectories((prev) =>
          prev.map((directory) =>
            directory.id === parentId
              ? {
                  ...directory,
                  subDirectories: [
                    ...(directory.subDirectories || []),
                    createdSubdirectory,
                  ],
                }
              : directory
          )
        );

        return createdSubdirectory;
      } catch (error) {
        setError("Failed to create subdirectory.");
        return {
          id: -1,
          name: "Default Subdirectory",
          subDirectories: [],
          files: [],
          parentId: null,
        };
      }
    },
    [setDirectories]
  );

  const handleOpenFileModal = useCallback(
    (file: File | null) => {
      setSelectedFile(file);
    },
    [setSelectedFile]
  );

  const resetModal = useCallback(() => {
    setOpenCreateModal(false);
    setNewDirectoryName("");
  }, []);
  return (
    <Container
      maxWidth={false}
      disableGutters
      sx={{
        padding: 10,
        backgroundColor: "#ffbbff",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
      }}
    >
      <Paper
        elevation={3}
        sx={{
          width: "80vw",
          padding: 8,
          borderRadius: 4,
          backgroundColor: "#7549f8",
          boxSizing: "border-box",
        }}
      >
        <Typography
          variant="h3"
          sx={{
            marginBottom: 5,
            textAlign: "center",
            color: "#ffffff",
            display: "flex",
            flexDirection: "row", 
            alignItems: "center",
            justifyContent: "center",
            flexWrap: "wrap",

          }}
        >
          Yourdir.com
          <FolderIcon
            sx={{
              marginLeft: "0.1em",
              fontSize: 100,
              '@media (max-width: 600px)': {
                fontSize: '75px', 
              },
              '@media (max-width: 400px)': {
                fontSize: '50px', 
              },
              color: "#FBC02D",
            }}
          />
        </Typography>

        <Typography
          variant="h6"
          sx={{ marginBottom: 3, textAlign: "center", color: "#e0e0e0" }}
        >
          Your go-to solution for managing and organizing your files
          effortlessly.
        </Typography>
        <Typography
          variant="body1"
          sx={{ textAlign: "center", color: "#e0e0e0" }}
        >
          Our platform allows you to create, manage, and organize directories
          and files with ease. Whether for personal or professional use,
          Yourdir.com provides an intuitive and streamlined experience for all
          your file management needs.
        </Typography>
        <Box height={50}></Box>

        <Button
          variant="contained"
          startIcon={<AddIcon />}
          onClick={() => setOpenCreateModal(true)}
          sx={{
            marginBottom: 2,
            backgroundColor: "#4f2df4",
            borderRadius: 2,
            color: "white",
          }}
        >
          Create Directory
        </Button>

        {loading ? (
          <Box sx={{ textAlign: "center" }}>
            <CircularProgress />
          </Box>
        ) : error ? (
          <Typography color="error">{error}</Typography>
        ) : (
          <DirectoryList
            onDeleteDirectory={handleDeleteDirectory}
            onOpenFileModal={handleOpenFileModal}
            onCreateSubdirectory={handleCreateSubdirectory}
          />
        )}
      </Paper>

      <CreateDirectoryModal
        open={openCreateModal}
        onClose={resetModal}
        newDirectoryName={newDirectoryName}
        setNewDirectoryName={setNewDirectoryName}
        onCreateDirectory={handleCreateDirectory}
        parentId={null}
      />
    </Container>
  );
};

export default React.memo(YourdirApp);
