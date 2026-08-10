
package repository;

import model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    // Custom query methods (if needed)
    Employee findByEmpid(String empid);
    Employee findByEname(String ename);

    List<Employee> findByDept(String dept);
    

    @Query("SELECT e.empid, e.ename, e.age, e.salary, e.dept FROM Employee e")
List<Object[]> fetchAllEmployeesData();
}