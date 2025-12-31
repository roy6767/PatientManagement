package se.biplob.departmentservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String designation;

    @Column(nullable = false)
    private Long departmentId;

    @Column(length = 1000)
    private String about;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private int minWeeklyBookings = 10;

    @Column(nullable = false)
    private int maxWeeklyBookings = 15;

    @Column(nullable = false)
    private boolean active = true;
}
