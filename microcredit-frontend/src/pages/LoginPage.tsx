import { Box, Button, Paper, TextField, Typography } from '@mui/material'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import * as z from 'zod'
import { useAuth } from '../context/AuthContext'
import { useNavigate } from 'react-router-dom'

const schema = z.object({
    email: z.string().email('Email invalide'),
    password: z.string().min(6, 'Mot de passe trop court'),
})

type FormData = z.infer<typeof schema>

export default function LoginPage() {
    const { login, isLoading } = useAuth()
    const navigate = useNavigate()
    const {
        register,
        handleSubmit,
        formState: { errors },
    } = useForm<FormData>({
        resolver: zodResolver(schema),
    })

    const onSubmit = async (data: FormData) => {
        await login(data.email, data.password)
        navigate('/dashboard')
    }

    return (
        <Box
            sx={{
                height: '100vh',
                width: '100vw',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                bgcolor: '#f0f4f8',
                position: 'fixed',
                top: 0,
                left: 0,
                p: 2,
            }}
        >
            <Paper
                elevation={10}
                sx={{
                    p: { xs: 3, sm: 5 },
                    width: { xs: '90%', sm: '400px' },
                    maxWidth: 400,
                    borderRadius: 3,
                    textAlign: 'center',
                }}
            >
                <Typography variant="h4" fontWeight="bold" gutterBottom>
                    Connexion
                </Typography>

                <Box component="form" onSubmit={handleSubmit(onSubmit)} sx={{ mt: 3 }}>
                    <TextField
                        label="Email"
                        type="email"
                        fullWidth
                        margin="normal"
                        {...register('email')}
                        error={!!errors.email}
                        helperText={errors.email?.message}
                        sx={{ mb: 2 }}
                    />
                    <TextField
                        label="Mot de passe"
                        type="password"
                        fullWidth
                        margin="normal"
                        {...register('password')}
                        error={!!errors.password}
                        helperText={errors.password?.message}
                        sx={{ mb: 3 }}
                    />
                    <Button
                        type="submit"
                        variant="contained"
                        fullWidth
                        size="large"
                        sx={{ py: 1.5, fontSize: '1.1rem', fontWeight: 'bold' }}
                        disabled={isLoading}
                    >
                        {isLoading ? 'Connexion...' : 'SE CONNECTER'}
                    </Button>
                    <Button
                        fullWidth
                        onClick={() => navigate('/signup')}
                        sx={{ mt: 2, textTransform: 'none', color: 'primary.main' }}
                    >
                        Pas de compte ? S'inscrire
                    </Button>
                </Box>
            </Paper>
        </Box>
    )
}