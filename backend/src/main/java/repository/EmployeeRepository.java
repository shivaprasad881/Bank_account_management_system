
package repository;

import model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    // Custom query methods (if needed)
    Employee findByEmpid(String empid);
    Employee findByEname(String ename);
}