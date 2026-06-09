import { create } from 'zustand'
import api from '@/lib/axios'

export interface Booking {
  id: string
  patientId: string
  doctorId: number
  treatmentId: number
  appointmentDate: string
  startTime: string
  endTime: string
  status: 'BOOKED' | 'CANCELLED' | 'COMPLETED'
  createdAt: string
}

interface BookingState {
  bookings: Booking[]
  loading: boolean
  fetchByPatient: (patientId: string) => Promise<void>
  createBooking: (data: Omit<Booking, 'id' | 'status' | 'createdAt'>) => Promise<void>
  cancelBooking: (id: string) => Promise<void>
  completeBooking: (id: string) => Promise<void>
}

export const useBookingStore = create<BookingState>((set) => ({
  bookings: [],
  loading: false,

  fetchByPatient: async (patientId) => {
    set({ loading: true })
    try {
      const res = await api.get(`/v1/bookings/patient/${patientId}`)
      set({ bookings: res.data })
    } finally {
      set({ loading: false })
    }
  },

  createBooking: async (data) => {
    await api.post('/v1/bookings', data)
  },

  cancelBooking: async (id) => {
    await api.put(`/v1/bookings/${id}/cancel`)
  },

  completeBooking: async (id) => {
    await api.put(`/v1/bookings/${id}/complete`)
  },
}))
