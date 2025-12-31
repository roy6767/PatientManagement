package se.biplob.departmentservice.model;

import java.util.List;

public class Treatment {
    private String id;
    private String name;
    private List<Doctor> doctors;
    private Double price;
    private Department department;
}
