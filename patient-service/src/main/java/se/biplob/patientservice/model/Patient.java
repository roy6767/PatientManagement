package se.biplob.patientservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import se.biplob.patientservice.model.enums.Gender;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @CreationTimestamp
    @Column(name = "registered_date", updatable = false)
    private LocalDate registeredDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String address;
    private String phoneNumber;
}
