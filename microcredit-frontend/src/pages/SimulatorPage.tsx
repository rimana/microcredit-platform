import { Container, Typography, Paper } from '@mui/material'
import LoanSimulator from '../components/loan/LoanSimulator'

export default function SimulatorPage() {
    return (
        <Container maxWidth="lg" sx={{ mt: 4, mb: 6 }}>
            <Paper sx={{ p: { xs: 3, sm: 5 }, borderRadius: 3, boxShadow: 4 }}>
                <Typography
                    variant="h4"
                    component="h1"
                    gutterBottom
                    textAlign="center"
                    color="primary.main"
                    fontWeight="bold"
                >
                    Simulateur de Prêt
                </Typography>
                <Typography
                    variant="body1"
                    textAlign="center"
                    color="text.secondary"
                    mb={4}
                >
                    Calculez votre mensualité et visualisez le tableau d’amortissement.
                </Typography>

                <LoanSimulator />
            </Paper>
        </Container>
    )
}