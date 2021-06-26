package in.demo.payroll.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.demo.payroll.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
