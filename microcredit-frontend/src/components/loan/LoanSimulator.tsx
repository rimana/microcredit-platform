import { useState } from 'react'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import * as z from 'zod'
import {
    Box,
    TextField,
    Button,
    Typography,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Paper,
    Alert,
} from '@mui/material'

const schema = z.object({
    amount: z.coerce.number().positive('Montant requis').max(500000),
    duration: z.coerce.number().int().min(1).max(60),
    interestRate: z.coerce.number().min(0).max(50),
})

type FormData = z.infer<typeof schema>

interface Payment {
    month: number
    payment: number
    principal: number
    interest: number
    balance: number
}

export default function LoanSimulator() {
    const [schedule, setSchedule] = useState<Payment[]>([])
    const [monthly, setMonthly] = useState<number | null>(null)
    const [total, setTotal] = useState<number | null>(null)

    const {
        register,
        handleSubmit,
        formState: { errors },
    } = useForm<FormData>({
        resolver: zodResolver(schema),
        defaultValues: { amount: 50000, duration: 24, interestRate: 7.5 },
    })

    const calculate = (data: FormData) => {
        const { amount, duration, interestRate } = data
        const r = interestRate / 100 / 12
        const numerator = r * Math.pow(1 + r, duration)
        const denominator = Math.pow(1 + r, duration) - 1
        const monthlyPayment = amount * (numerator / denominator)

        let balance = amount
        const payments: Payment[] = []

        for (let i = 1; i <= duration; i++) {
            const interest = balance * r
            const principal = monthlyPayment - interest
            balance -= principal

            payments.push({
                month: i,
                payment: monthlyPayment,
                principal,
                interest,
                balance: Math.max(0, balance),
            })
        }

        setMonthly(monthlyPayment)
        setTotal(monthlyPayment * duration)
        setSchedule(payments)
    }

    return (
        <Box>
            <Box
                component="form"
                onSubmit={handleSubmit(calculate)}
                sx={{
                    display: 'grid',
                    gap: 3,
                    gridTemplateColumns: { xs: '1fr', sm: 'repeat(3, 1fr)' },
                    mb: 4,
                }}
            >
                <TextField
                    label="Montant (MAD)"
                    type="number"
                    inputProps={{ step: '1000' }}
                    {...register('amount')}
                    error={!!errors.amount}
                    helperText={errors.amount?.message}
                />
                <TextField
                    label="Durée (mois)"
                    type="number"
                    inputProps={{ min: 1, max: 60 }}
                    {...register('duration')}
                    error={!!errors.duration}
                    helperText={errors.duration?.message}
                />
                <TextField
                    label="Taux (%)"
                    type="number"
                    inputProps={{ step: '0.1' }}
                    {...register('interestRate')}
                    error={!!errors.interestRate}
                    helperText={errors.interestRate?.message}
                />
                <Button type="submit" variant="contained" size="large" sx={{ gridColumn: { sm: '1 / -1' } }}>
                    Simuler
                </Button>
            </Box>

            {monthly && (
                <Alert severity="success" sx={{ mb: 4 }}>
                    <Typography><strong>Mensualité :</strong> {monthly.toFixed(2)} MAD</Typography>
                    <Typography><strong>Total :</strong> {total?.toFixed(2)} MAD</Typography>
                </Alert>
            )}

            {schedule.length > 0 && (
                <TableContainer component={Paper}>
                    <Table size="small">
                        <TableHead>
                            <TableRow>
                                <TableCell><strong>Mois</strong></TableCell>
                                <TableCell align="right"><strong>Mensualité</strong></TableCell>
                                <TableCell align="right"><strong>Capital</strong></TableCell>
                                <TableCell align="right"><strong>Intérêts</strong></TableCell>
                                <TableCell align="right"><strong>Solde</strong></TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {schedule.map((row) => (
                                <TableRow key={row.month}>
                                    <TableCell>{row.month}</TableCell>
                                    <TableCell align="right">{row.payment.toFixed(2)}</TableCell>
                                    <TableCell align="right">{row.principal.toFixed(2)}</TableCell>
                                    <TableCell align="right">{row.interest.toFixed(2)}</TableCell>
                                    <TableCell align="right">{row.balance.toFixed(2)}</TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
            )}
        </Box>
    )
}