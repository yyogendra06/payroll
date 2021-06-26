package in.demo.payroll.controller;

public class EmployeeNotFoundException extends RuntimeException {

	public EmployeeNotFoundException(Long id) {
		super("Could not found id " + id);
	}

}
