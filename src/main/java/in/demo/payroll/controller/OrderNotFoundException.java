package in.demo.payroll.controller;

public class OrderNotFoundException extends RuntimeException {

	public OrderNotFoundException(Long id) {
		super("Order id : " + id + " Not Found");
	}

}
