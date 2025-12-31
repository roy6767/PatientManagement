package se.biplob.bookingmodule.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import se.biplob.bookingmodule.model.Booking;
import se.biplob.bookingmodule.model.BookingRequest;
import se.biplob.bookingmodule.model.PatientDto;
import se.biplob.bookingmodule.repository.BookingRepository;

import java.util.UUID;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final RestTemplate restTemplate;

    public BookingService(BookingRepository bookingRepository,  RestTemplate restTemplate) {
        this.bookingRepository = bookingRepository;
        this.restTemplate = restTemplate;
    }
    public ResponseEntity<?> issueBooking(BookingRequest bookingRequest) {
        PatientDto patientDto = new PatientDto();
        patientDto=restTemplate.getForObject("http://localhost:8080/api/v1/patients/"+bookingRequest.getPatientId(), PatientDto.class);
        if(patientDto!=null){
            Booking booking = new Booking();
            booking.setPatientId(patientDto.getId());
            booking.setName(patientDto.getName());
            booking.setEmail(patientDto.getEmail());
            booking.setPhone(patientDto.getPhoneNumber());
            bookingRepository.save(booking);
            return ResponseEntity.status(HttpStatus.CREATED).body(booking);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No user found with the id");
    }
    public ResponseEntity<Booking> getBookingByID(UUID id) {
         return ResponseEntity.ok(bookingRepository.findById(id).orElse(null));
    }
}
