import React, { useState, useCallback } from "react";
import {
  Paper,
  Typography,
  List,
  ListItem,
  ListItemText,
  IconButton,
  Button,
  Collapse,
  Box,
} from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import FileInfoModal from "./FileInfoModal";
import CreateDirectoryModal from "./CreateDirectoryModalProps";
import { useDirectory } from "../context/DirectoryContext";
import { useFileContext } from "../context/FileContext";
import { File } from "../interfaces/file.interface";
import { Directory } from "../interfaces/diretory.inteface";
import AddIcon from "@mui/icons-material/Add";

interface DirectoryListProps {
  onDeleteDirectory: (id: number) => void;
  onOpenFileModal?: (file: File | null) => void;
  onCreateSubdirectory: (parentId: number, name: string) => Promise<Directory>;
}

const DirectoryList: React.FC<DirectoryListProps> = ({
  onDeleteDirectory,
  onCreateSubdirectory,
}) => {
  const { directories, setSelectedFile } = useDirectory();
  const { setIsEditing, setModeFile } = useFileContext();
  const [expandedDirs, setExpandedDirs] = useState<number[]>([]);
  const [modals, setModals] = useState({ file: false, dir: false });
  const [newDirName, setNewDirName] = useState("");
  const [parentId, setParentId] = useState<number | null>(null);

  const toggleModal = useCallback((type: string, state: boolean) => {
    setModals((prev) => ({ ...prev, [type]: state }));
  }, []);

  const toggleDirectory = useCallback((id: number) => {
    setExpandedDirs((prev) =>
      prev.includes(id) ? prev.filter((d) => d !== id) : [...prev, id]
    );
  }, []);

  const handleFileAction = useCallback(
    (
      directoryId: number,
      mode: "create" | "edit",
      file: File | null = null
    ) => {
      setSelectedFile(file || { directoryId, name: "", content: "" });
      setModeFile(mode);
      setIsEditing(true);
      toggleModal("file", true);
    },
    [setSelectedFile, setIsEditing, setModeFile, toggleModal]
  );

  const handleCreateDirectory = useCallback(async (): Promise<Directory> => {
    if (!parentId) {
      throw new Error("parentId is required to create a directory");
    }

    const subDir = await onCreateSubdirectory(parentId, newDirName);
    setNewDirName("");
    toggleModal("dir", false);
    setExpandedDirs((prev) =>
      prev.includes(parentId) ? prev : [...prev, parentId]
    );
    return subDir;
  }, [parentId, newDirName, onCreateSubdirectory, toggleModal]);

  const renderDirectory = (directory: Directory | null) => {
    if (!directory || !directory.id) {
      return null;
    }

    const { id, name, files, subDirectories } = directory;
    const isExpanded = expandedDirs.includes(id);

    return (
      <Paper
        key={id}
        elevation={2}
        sx={{ mb: 2, p: 2, backgroundColor: "#bbdefb", borderRadius: 4 }}
      >
        <Typography
          variant="h6"
          onClick={() => toggleDirectory(id)}
          sx={{
            display: "flex",
            cursor: "pointer",
            flexWrap: "wrap",
            gap: 1,
            alignItems: "center",
          }}
        >
          <IconButton
            onClick={(e) => {
              e.stopPropagation();
              toggleDirectory(id);
            }}
            sx={{
              transform: isExpanded ? "rotate(180deg)" : "rotate(0deg)",
              transition: "transform 0.3s ease-in-out",
            }}
          >
            <ExpandMoreIcon />
          </IconButton>
          {name}

          <Box
            sx={{
              ml: "auto",
              display: "flex",
              gap: 1,
              flexWrap: "wrap",

              justifyContent: "flex-end",
            }}
          >
            <Button
              variant="outlined"
              onClick={(e) => (
                e.stopPropagation(), handleFileAction(id, "create")
              )}
              sx={{
                borderRadius: 4,
                fontSize: "clamp(10px, 1.5vw, 16px)",
              }}
              startIcon={<AddIcon />}
            >
              File
            </Button>
            <Button
              variant="outlined"
              onClick={(e) => (
                e.stopPropagation(), setParentId(id), toggleModal("dir", true)
              )}
              sx={{
                borderRadius: 4,
                fontSize: "clamp(10px, 1.5vw, 16px)",
              }}
              startIcon={<AddIcon />}
            >
              Subdirectory
            </Button>

            <IconButton
              onClick={(e) => (e.stopPropagation(), onDeleteDirectory(id))}
              sx={{
                borderRadius: 4,
                fontSize: "clamp(10px, 1.5vw, 16px)",
              }}
            >
              <DeleteIcon />
            </IconButton>
          </Box>
        </Typography>

        <Collapse in={isExpanded} timeout="auto" unmountOnExit>
          <List>
            {!files?.length && !subDirectories?.length ? (
              <Typography align="center" sx={{ mt: 2, color: "black" }}>
                No files or subdirectories available.
              </Typography>
            ) : (
              <>
                {files?.map((file: File) => (
                  <ListItem
                    key={file?.id}
                    onClick={() => handleFileAction(id, "edit", file)}
                    sx={{
                      borderRadius: 2,
                      bgcolor: "#7549f8",
                      color: "whitesmoke",
                      mb: 1,
                    }}
                  >
                    <ListItemText primary={file?.name} />
                  </ListItem>
                ))}
                {subDirectories?.map(renderDirectory)}
              </>
            )}
          </List>
        </Collapse>
      </Paper>
    );
  };

  return (
    <>
      <CreateDirectoryModal
        open={modals.dir}
        onClose={() => toggleModal("dir", false)}
        newDirectoryName={newDirName}
        setNewDirectoryName={setNewDirName}
        onCreateDirectory={handleCreateDirectory}
        parentId={parentId}
      />
      <FileInfoModal
        open={modals.file}
        onClose={() => toggleModal("file", false)}
      />

      {directories.length ? (
        directories.filter(Boolean).map(renderDirectory)
      ) : (
        <Typography
          variant="h6"
          align="center"
          sx={{ mt: 4, color: "#e0e0e0" }}
        >
          No directories or files available.
        </Typography>
      )}
    </>
  );
};

export default React.memo(DirectoryList);
