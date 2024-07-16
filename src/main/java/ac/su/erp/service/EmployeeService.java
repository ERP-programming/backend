package ac.su.erp.service;

import ac.su.erp.domain.Employee;
import ac.su.erp.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee findByEmpNum(Long empNum) {
        return employeeRepository.findByEmpNum(empNum)
                .orElseThrow(() -> new RuntimeException("Employee not found with empNum: " + empNum));
    }

}
