import { createContext, useState, useContext, type ReactNode } from 'react'
import api from '../services/api'

interface User {
    id: number
    username: string
    email: string
    role: 'CLIENT' | 'AGENT' | 'ADMIN'
}

interface AuthContextType {
    user: User | null
    login: (username: string, password: string) => Promise<void>
    signup: (data: any) => Promise<void>
    logout: () => void
    isLoading: boolean
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export const AuthProvider = ({ children }: { children: ReactNode }) => {
    const [user, setUser] = useState<User | null>(null)
    const [isLoading, setIsLoading] = useState(false)

    const login = async (username: string, password: string) => {
        setIsLoading(true)
        try {
            const res = await api.post('/auth/signin', { username, password })
            const { token, ...userData } = res.data
            localStorage.setItem('token', token)
            setUser(userData)
        } finally {
            setIsLoading(false)
        }
    }

    const signup = async (data: any) => {
        setIsLoading(true)
        try {
            await api.post('/auth/signup', data)
            await login(data.username, data.password)
        } finally {
            setIsLoading(false)
        }
    }

    const logout = () => {
        localStorage.removeItem('token')
        setUser(null)
    }

    return (
        <AuthContext.Provider value={{ user, login, signup, logout, isLoading }}>
            {children}
        </AuthContext.Provider>
    )
}

export const useAuth = () => {
    const context = useContext(AuthContext)
    if (!context) throw new Error('useAuth doit être utilisé dans AuthProvider')
    return context
}