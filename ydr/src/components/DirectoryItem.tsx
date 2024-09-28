import React from "react";
import { ListItem, ListItemText } from "@mui/material";
import { lightBlue } from "@mui/material/colors";

interface Directory {
  name: string;
}

interface DirectoryItemProps {
  directory: Directory;
}

const DirectoryItem: React.FC<DirectoryItemProps> = ({ directory }) => (
  <ListItem
    sx={{
      bgcolor: lightBlue[400],
      mb: 1,
      borderRadius: 1,
      "&:hover": { bgcolor: lightBlue[700], transition: "0.3s" },
    }}
  >
    <ListItemText primary={directory.name} sx={{ color: "white" }} />
  </ListItem>
);

export default React.memo(DirectoryItem);
