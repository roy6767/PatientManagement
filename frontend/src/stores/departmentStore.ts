import { create } from 'zustand'
import api from '@/lib/axios'

export interface Department {
  id: number
  name: string
  description: string
  active: boolean
}

export interface Doctor {
  id: number
  firstName: string
  lastName: string
  designation: string
  email: string
  departmentId: number
  active: boolean
}

export interface Treatment {
  id: number
  name: string
  price: number
  departmentId: number
}

interface DepartmentState {
  departments: Department[]
  doctors: Doctor[]
  treatments: Treatment[]
  loading: boolean
  fetchDepartments: () => Promise<void>
  fetchDoctorsByDept: (deptId: number) => Promise<void>
  fetchTreatmentsByDept: (deptId: number) => Promise<void>
}

export const useDepartmentStore = create<DepartmentState>((set) => ({
  departments: [],
  doctors: [],
  treatments: [],
  loading: false,

  fetchDepartments: async () => {
    set({ loading: true })
    try {
      const res = await api.get('/departments')
      set({ departments: res.data })
    } finally {
      set({ loading: false })
    }
  },

  fetchDoctorsByDept: async (deptId) => {
    const res = await api.get(`/doctors/department/${deptId}`)
    set({ doctors: res.data })
  },

  fetchTreatmentsByDept: async (deptId) => {
    const res = await api.get(`/treatments/department/${deptId}`)
    set({ treatments: res.data })
  },
}))
