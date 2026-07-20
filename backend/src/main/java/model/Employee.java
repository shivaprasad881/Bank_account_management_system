package model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    private String empid;
    private String ename;
    private int age;
    private double salary;
    private String dept;
    private String password;

    public Employee() {}

    public Employee(String empid, String ename, int age, double salary, String dept, String password) {
        this.empid = empid;
        this.ename = ename;
        this.age = age;
        this.salary = salary;
        this.dept = dept;
        this.password = password;
    }

    // Getters
    public String getEmpid() { return empid; }
    public String getEname() { return ename; }
    public int getAge() { return age; }
    public double getSalary() { return salary; }
    public String getDept() { return dept; }
    public String getPassword() { return password; }

    // Setters
    public void setEmpid(String empid) { this.empid = empid; }
    public void setEname(String ename) { this.ename = ename; }
    public void setAge(int age) { this.age = age; }
    public void setSalary(double salary) { this.salary = salary; }
    public void setDept(String dept) { this.dept = dept; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return "Employee{empid='" + empid + "', ename='" + ename + "', age=" + age + 
               ", salary=" + salary + ", dept='" + dept + "'}";
    }
}