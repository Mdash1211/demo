package com.employee.demo.Service;

import com.employee.demo.Entity.Employee;
import com.employee.demo.Payload.EmployeeDto;

import java.util.List;

public interface EmployeeService {
    EmployeeDto createEmp(EmployeeDto employee);

    List<Employee> getAllEmployees();
}
