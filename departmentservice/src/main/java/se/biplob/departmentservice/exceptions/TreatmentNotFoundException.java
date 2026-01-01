package se.biplob.departmentservice.exceptions;

public class TreatmentNotFoundException extends RuntimeException {
    public TreatmentNotFoundException(Long id) {
        super("Treatment with id " + id + " not found");
    }
}
