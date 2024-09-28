import React from 'react';
import { Typography } from '@mui/material';
import { blue } from '@mui/material/colors';

const Header = () => (
  <Typography variant="h4" gutterBottom sx={{ color: blue[700], textAlign: 'center' }}>
    Yourdir - File Manager
  </Typography>
);

export default React.memo(Header);
