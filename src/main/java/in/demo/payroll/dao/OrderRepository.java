package in.demo.payroll.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.demo.payroll.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
