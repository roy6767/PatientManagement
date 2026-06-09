import { create } from 'zustand'
import api from '@/lib/axios'

export interface Patient {
  id: string
  name: string
  email: string
  birthDate: string
  gender: 'MALE' | 'FEMALE' | 'OTHER'
  address: string
  phoneNumber: string
  registeredDate: string
}

interface PatientState {
  patients: Patient[]
  selected: Patient | null
  loading: boolean
  total: number
  fetchPatients: (page?: number, size?: number, search?: string) => Promise<void>
  fetchById: (id: string) => Promise<void>
  createPatient: (data: Omit<Patient, 'id' | 'registeredDate'>) => Promise<void>
  updatePatient: (id: string, data: Partial<Patient>) => Promise<void>
}

export const usePatientStore = create<PatientState>((set) => ({
  patients: [],
  selected: null,
  loading: false,
  total: 0,

  fetchPatients: async (page = 0, size = 10, search = '') => {
    set({ loading: true })
    try {
      const params: Record<string, unknown> = { page, size }
      if (search) params.search = search
      const res = await api.get('/v1/patients', { params })
      const data = res.data
      set({
        patients: data.content ?? data,
        total: data.totalElements ?? (data.content ?? data).length,
      })
    } finally {
      set({ loading: false })
    }
  },

  fetchById: async (id) => {
    const res = await api.get(`/v1/patients/${id}`)
    set({ selected: res.data })
  },

  createPatient: async (data) => {
    await api.post('/v1/patients', data)
  },

  updatePatient: async (id, data) => {
    await api.put(`/v1/patients/${id}`, data)
  },
}))
