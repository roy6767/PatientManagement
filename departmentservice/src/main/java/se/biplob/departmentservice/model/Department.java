package se.biplob.departmentservice.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private List<Treatment> treatments;
    @Column(nullable = false)
    @OneToOne(JoinColumn=@)
    private List<Doctor> doctors;
}
