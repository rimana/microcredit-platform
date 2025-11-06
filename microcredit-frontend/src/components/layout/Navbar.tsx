import { AppBar, Toolbar, Typography, Button, Box } from '@mui/material'
import { useAuth } from '../../context/AuthContext'
import { useNavigate } from 'react-router-dom'

export default function Navbar() {
    const { user, logout } = useAuth()
    const navigate = useNavigate()

    const handleLogout = () => {
        logout()
        navigate('/login')
    }

    return (
        <AppBar position="static">
            <Toolbar>
                <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                    MicroCrédit Pro
                </Typography>
                {user ? (
                    <Box>
                        <Button color="inherit" onClick={() => navigate('/dashboard')}>
                            Tableau de bord
                        </Button>
                        <Button color="inherit" onClick={handleLogout}>
                            Déconnexion
                        </Button>
                    </Box>
                ) : (
                    <Box>
                        <Button color="inherit" onClick={() => navigate('/login')}>
                            Connexion
                        </Button>
                        <Button color="inherit" onClick={() => navigate('/signup')}>
                            Inscription
                        </Button>
                    </Box>
                )}
            </Toolbar>
        </AppBar>
    )
}