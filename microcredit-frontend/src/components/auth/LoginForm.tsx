import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import * as z from 'zod'
import { useAuth } from '../../context/AuthContext'
import { useNavigate } from 'react-router-dom'
import { toast } from 'react-toastify'
import { Button, TextField, Box, Typography, Link } from '@mui/material'

const schema = z.object({
    username: z.string().min(3, 'Nom d’utilisateur requis'),
    password: z.string().min(6, 'Mot de passe trop court'),
})

type FormData = z.infer<typeof schema>

export default function LoginForm() {
    const { login } = useAuth()
    const navigate = useNavigate()
    const {
        register,
        handleSubmit,
        formState: { errors, isSubmitting },
    } = useForm<FormData>({ resolver: zodResolver(schema) })

    const onSubmit = async (data: FormData) => {
        try {
            await login(data.username, data.password)
            toast.success('Connexion réussie !')
            navigate('/dashboard')
        } catch (err: any) {
            toast.error(err.response?.data || 'Erreur de connexion')
        }
    }

    return (
        <Box component="form" onSubmit={handleSubmit(onSubmit)} sx={{ mt: 1 }}>
            <TextField
                margin="normal"
                fullWidth
                label="Nom d'utilisateur"
                {...register('username')}
                error={!!errors.username}
                helperText={errors.username?.message}
            />
            <TextField
                margin="normal"
                fullWidth
                label="Mot de passe"
                type="password"
                {...register('password')}
                error={!!errors.password}
                helperText={errors.password?.message}
            />
            <Button
                type="submit"
                fullWidth
                variant="contained"
                sx={{ mt: 3, mb: 2 }}
                disabled={isSubmitting}
            >
                Se connecter
            </Button>
            <Typography textAlign="center">
                Pas de compte ? <Link href="/signup">S’inscrire</Link>
            </Typography>
        </Box>
    )
}