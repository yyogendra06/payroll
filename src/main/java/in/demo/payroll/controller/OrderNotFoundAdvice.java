package in.demo.payroll.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class OrderNotFoundAdvice {

	@ResponseBody
	@ExceptionHandler(OrderNotFoundException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public String orderNotFound(OrderNotFoundException ex) {
		return ex.getMessage();
	}
}
