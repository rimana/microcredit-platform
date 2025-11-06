import React from 'react'
import { useForm, type SubmitHandler } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import * as z from 'zod'
import { useAuth } from '../../context/AuthContext'
import { useNavigate } from 'react-router-dom'
import { toast } from 'react-toastify'
import {
    Button,
    TextField,
    Box,
    FormControlLabel,
    Checkbox,
    Grid,
    Typography,
    Collapse,
} from '@mui/material'

// === SCHÉMA ZOD (coercion + refine) ===
const signupSchema = z.object({
    username: z.string().min(3, '3 caractères minimum'),
    email: z.string().email('Email invalide'),
    password: z.string().min(6, '6 caractères minimum'),
    phone: z.string().regex(/^\d{10}$/, '10 chiffres requis'),
    cin: z.string().length(8, 'CIN : 8 caractères'),
    address: z.string().min(5, 'Adresse trop courte'),
    monthlyIncome: z.coerce.number({ invalid_type_error: 'Revenu requis' }).positive('Revenu positif'),
    profession: z.string().min(2, 'Profession requise'),
    isFunctionnaire: z.boolean(),
    guarantorName: z.string().optional(),
    guarantorCin: z.string().optional(),
    guarantorPhone: z.string().optional(),
    guarantorAddress: z.string().optional(),
}).refine(
    (data) => data.isFunctionnaire || !!data.guarantorName && !!data.guarantorCin && !!data.guarantorPhone && !!data.guarantorAddress,
    { message: 'Tous les champs du garant requis', path: ['guarantorName'] }
)

type FormData = z.infer<typeof signupSchema>

const SignupForm: React.FC = () => {
    const { signup } = useAuth()
    const navigate = useNavigate()

    const {
        register,
        handleSubmit,
        watch,
        formState: { errors, isSubmitting },
    } = useForm<FormData>({
        resolver: zodResolver(signupSchema),
        defaultValues: { isFunctionnaire: true, monthlyIncome: 0 },
    })

    const isFunctionnaire = watch('isFunctionnaire')

    const onSubmit: SubmitHandler<FormData> = async (data) => {
        try {
            await signup({
                username: data.username,
                email: data.email,
                password: data.password,
                phone: data.phone,
                cin: data.cin,
                address: data.address,
                monthlyIncome: data.monthlyIncome,
                profession: data.profession,
                employed: data.isFunctionnaire,
                role: 'CLIENT',
                guarantorName: data.isFunctionnaire ? null : data.guarantorName,
                guarantorCin: data.isFunctionnaire ? null : data.guarantorCin,
                guarantorPhone: data.isFunctionnaire ? null : data.guarantorPhone,
                guarantorAddress: data.isFunctionnaire ? null : data.guarantorAddress,
            })
            toast.success('Inscription réussie !')
            navigate('/dashboard')
        } catch (err: any) {
            toast.error(err.response?.data || 'Erreur serveur')
        }
    }

    return (
        <Box component="form" onSubmit={handleSubmit(onSubmit)} noValidate>
            <Grid container spacing={2}>
                <Grid item xs={12} sm={6}>
                    <TextField fullWidth label="Nom d'utilisateur" {...register('username')} error={!!errors.username} helperText={errors.username?.message} />
                </Grid>
                <Grid item xs={12} sm={6}>
                    <TextField fullWidth label="Email" type="email" {...register('email')} error={!!errors.email} helperText={errors.email?.message} />
                </Grid>

                <Grid item xs={12} sm={6}>
                    <TextField fullWidth label="Mot de passe" type="password" {...register('password')} error={!!errors.password} helperText={errors.password?.message} />
                </Grid>
                <Grid item xs={12} sm={6}>
                    <TextField fullWidth label="Téléphone" {...register('phone')} error={!!errors.phone} helperText={errors.phone?.message} />
                </Grid>

                <Grid item xs={12} sm={6}>
                    <TextField fullWidth label="CIN" {...register('cin')} error={!!errors.cin} helperText={errors.cin?.message} />
                </Grid>
                <Grid item xs={12} sm={6}>
                    <TextField fullWidth label="Adresse" {...register('address')} error={!!errors.address} helperText={errors.address?.message} />
                </Grid>

                <Grid item xs={12} sm={6}>
                    <TextField
                        fullWidth
                        label="Revenu mensuel (MAD)"
                        type="number"
                        inputProps={{ step: '0.01' }}
                        {...register('monthlyIncome', { valueAsNumber: true })}
                        error={!!errors.monthlyIncome}
                        helperText={errors.monthlyIncome?.message}
                    />
                </Grid>
                <Grid item xs={12} sm={6}>
                    <TextField fullWidth label="Profession" {...register('profession')} error={!!errors.profession} helperText={errors.profession?.message} />
                </Grid>

                <Grid item xs={12}>
                    <FormControlLabel control={<Checkbox {...register('isFunctionnaire')} defaultChecked />} label="Je suis fonctionnaire" />
                </Grid>
            </Grid>

            <Collapse in={!isFunctionnaire}>
                <Box mt={3} p={3} border={1} borderColor="grey.300" borderRadius={2}>
                    <Typography variant="subtitle1" gutterBottom color="primary">Informations du garant</Typography>
                    <Grid container spacing={2}>
                        {[
                            { key: 'guarantorName', label: 'Nom' },
                            { key: 'guarantorCin', label: 'CIN' },
                            { key: 'guarantorPhone', label: 'Téléphone' },
                            { key: 'guarantorAddress', label: 'Adresse' },
                        ].map(({ key, label }) => (
                            <Grid item xs={12} sm={6} key={key}>
                                <TextField
                                    fullWidth
                                    label={label}
                                    {...register(key as 'guarantorName' | 'guarantorCin' | 'guarantorPhone' | 'guarantorAddress')}
                                    error={!!errors[key as keyof typeof errors]}
                                    helperText={(errors[key as keyof typeof errors] as any)?.message}
                                />
                            </Grid>
                        ))}
                    </Grid>
                </Box>
            </Collapse>

            <Button type="submit" fullWidth variant="contained" size="large" sx={{ mt: 4, py: 1.5 }} disabled={isSubmitting}>
                {isSubmitting ? 'Inscription...' : "S'inscrire"}
            </Button>
        </Box>
    )
}

export default SignupForm