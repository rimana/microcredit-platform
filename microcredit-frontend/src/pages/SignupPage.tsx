import { Box, Button, Paper, TextField, Typography } from '@mui/material'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import * as z from 'zod'
import { useAuth } from '../context/AuthContext'
import { useNavigate } from 'react-router-dom'

const schema = z.object({
    name: z.string().min(2, 'Nom requis'),
    email: z.string().email('Email invalide'),
    password: z.string().min(6, 'Min 6 caractères'),
    cin: z.string().length(8, 'CIN = 8 chiffres'),
    income: z.coerce.number().positive('Revenu positif'),
})

type FormData = z.infer<typeof schema>

export default function SignupPage() {
    const { signup, isLoading } = useAuth()
    const navigate = useNavigate()
    const {
        register,
        handleSubmit,
        formState: { errors },
    } = useForm<FormData>({ resolver: zodResolver(schema) })

    const onSubmit = async (data: FormData) => {
        await signup(data)
        navigate('/dashboard')
    }

    return (
        <Box
            sx={{
                minHeight: '100vh',
                width: '100vw',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                bgcolor: '#f0f4f8',
                p: 2,
                overflow: 'auto', // Permet le scroll si contenu dépasse
            }}
        >
            <Paper
                elevation={10}
                sx={{
                    p: { xs: 3, sm: 5 },
                    width: { xs: '90%', sm: '450px' },
                    maxWidth: 450,
                    maxHeight: '90vh', // Limite hauteur max pour éviter débordement
                    overflow: 'auto',  // Scroll interne si trop de champs
                    borderRadius: 3,
                    textAlign: 'center',
                    display: 'flex',
                    flexDirection: 'column',
                }}
            >
                <Typography variant="h4" fontWeight="bold" gutterBottom>
                    Inscription
                </Typography>

                <Box
                    component="form"
                    onSubmit={handleSubmit(onSubmit)}
                    sx={{
                        mt: 3,
                        display: 'flex',
                        flexDirection: 'column',
                        flexGrow: 1,
                        justifyContent: 'space-between',
                    }}
                >
                    <Box>
                        <TextField
                            label="Nom"
                            fullWidth
                            margin="normal"
                            {...register('name')}
                            error={!!errors.name}
                            helperText={errors.name?.message}
                            sx={{ mb: 2 }}
                        />
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
                            sx={{ mb: 2 }}
                        />
                        <TextField
                            label="CIN"
                            fullWidth
                            margin="normal"
                            {...register('cin')}
                            error={!!errors.cin}
                            helperText={errors.cin?.message}
                            sx={{ mb: 2 }}
                        />
                        <TextField
                            label="Revenu mensuel (MAD)"
                            type="number"
                            fullWidth
                            margin="normal"
                            {...register('income')}
                            error={!!errors.income}
                            helperText={errors.income?.message}
                            sx={{ mb: 3 }}
                        />
                    </Box>

                    <Box>
                        <Button
                            type="submit"
                            variant="contained"
                            fullWidth
                            size="large"
                            sx={{ py: 1.5, fontSize: '1.1rem', fontWeight: 'bold' }}
                            disabled={isLoading}
                        >
                            {isLoading ? 'Inscription...' : "S'INSCRIRE"}
                        </Button>
                        <Button
                            fullWidth
                            onClick={() => navigate('/login')}
                            sx={{ mt: 2, textTransform: 'none', color: 'primary.main' }}
                        >
                            Déjà un compte ? Se connecter
                        </Button>
                    </Box>
                </Box>
            </Paper>
        </Box>
    )
}