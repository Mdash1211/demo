package com.employee.demo.Controller;

import com.employee.demo.Entity.Employee;
import com.employee.demo.Payload.EmployeeDto;
import com.employee.demo.Payload.TaxDeductionDTO;
import com.employee.demo.Service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping("/api/employee")
@RestController
public class EmployeeController {
@Autowired
    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<?> addEmployee(@Valid @RequestBody EmployeeDto employeeDto, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getFieldError().getDefaultMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            EmployeeDto dto = employeeService.createEmp(employeeDto);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        }
    }

    @GetMapping("/tax-deduction")
    public List<TaxDeductionDTO> getTaxDeductions() {
        List<TaxDeductionDTO> taxDeductions = new ArrayList<>();
        List<Employee> employees = employeeService.getAllEmployees();

        for (Employee employee : employees) {
            TaxDeductionDTO taxDeductionDTO = calculateTaxDeduction(employee);
            taxDeductions.add(taxDeductionDTO);
        }

        return taxDeductions;
    }

    private TaxDeductionDTO calculateTaxDeduction(Employee employee) {
        TaxDeductionDTO taxDeductionDTO = new TaxDeductionDTO();
        taxDeductionDTO.setEmployeeCode(employee.getEmployeeId());
        taxDeductionDTO.setFirstName(employee.getFirstName());
        taxDeductionDTO.setLastName(employee.getLastName());

        double totalSalary = calculateTotalSalary(employee);
        double taxAmount = calculateTax(totalSalary);
        double cessAmount = calculateCess(totalSalary);

        taxDeductionDTO.setYearlySalary(totalSalary);
        taxDeductionDTO.setTaxAmount(taxAmount);
        taxDeductionDTO.setCessAmount(cessAmount);

        return taxDeductionDTO;
    }

    private double calculateTotalSalary(Employee employee) {
        // Logic to calculate total salary based on DOJ and salary
        // For simplicity, let's assume total salary is equal to monthly salary * 12
        return employee.getSalary() * 12;
    }

    private double calculateTax(double totalSalary) {
        // Logic to calculate tax based on tax slabs
        double tax = 0;
        if (totalSalary > 250000 && totalSalary <= 500000) {
            tax += (totalSalary - 250000) * 0.05;
        } else if (totalSalary > 500000 && totalSalary <= 1000000) {
            tax += 12500 + (totalSalary - 500000) * 0.1;
        } else if (totalSalary > 1000000) {
            tax += 62500 + (totalSalary - 1000000) * 0.2;
        }
        return tax;
    }

    private double calculateCess(double totalSalary) {
        // Logic to calculate cess amount
        double cess = 0;
        if (totalSalary > 2500000) {
            cess += (totalSalary - 2500000) * 0.02;
        }
        return cess;
    }


}

