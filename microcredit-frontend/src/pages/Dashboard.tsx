import { Container, Typography, Paper, Box } from '@mui/material'
import { useAuth } from '../context/AuthContext'

export default function Dashboard() {
    const { user } = useAuth()

    return (
        <Container maxWidth="md" sx={{ mt: 4 }}>
            <Paper sx={{ p: 4, borderRadius: 2 }}>
                <Typography variant="h4" gutterBottom>
                    Bienvenue, {user?.email || 'Utilisateur'} !
                </Typography>
                <Typography variant="body1" color="text.secondary">
                    Votre tableau de bord est prêt.
                </Typography>

                <Box sx={{ mt: 4, display: 'grid', gap: 2 }}>
                    <Typography>→ Accédez au <strong>simulateur de prêt</strong> via le menu.</Typography>
                    <Typography>→ Soumettez une demande après simulation.</Typography>
                </Box>
            </Paper>
        </Container>
    )
}