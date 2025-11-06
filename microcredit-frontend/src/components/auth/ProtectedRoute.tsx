import { useAuth } from '../../context/AuthContext'
import { Navigate } from 'react-router-dom'
import { CircularProgress, Box } from '@mui/material'
import type {JSX} from "react";

export default function ProtectedRoute({ children }: { children: JSX.Element }) {
    const { user, isLoading } = useAuth()

    if (isLoading) {
        return (
            <Box display="flex" justifyContent="center" mt={8}>
                <CircularProgress />
            </Box>
        )
    }

    return user ? children : <Navigate to="/login" />
}