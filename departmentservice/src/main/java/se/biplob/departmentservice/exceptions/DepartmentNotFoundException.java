package se.biplob.departmentservice.exceptions;

public class DepartmentNotFoundException extends RuntimeException {
    public DepartmentNotFoundException(Long id) {
        super("Department with id " + id + " not found");
    }
}
