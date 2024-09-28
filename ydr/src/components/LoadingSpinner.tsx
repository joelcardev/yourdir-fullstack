import { Box, CircularProgress } from '@mui/material';
import { blue } from '@mui/material/colors';

const LoadingSpinner = () => (
  <Box sx={{ display: 'flex', justifyContent: 'center' }}>
    <CircularProgress sx={{ color: blue[700] }} />
  </Box>
);

export default LoadingSpinner;
