package ac.su.erp.service;

import ac.su.erp.domain.Employee;
import ac.su.erp.dto.ProfileDTO;
import ac.su.erp.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public ProfileDTO getProfileDtoById(Long employeeId) {
        Optional<Employee> employeeOpt = employeeRepository.findById(employeeId);
        if (employeeOpt.isPresent()) {
            Employee employee = employeeOpt.get();
            ProfileDTO profileDTO = new ProfileDTO();
            profileDTO.setEmpNum(employee.getEmpNum());
            profileDTO.setEmpBirth(employee.getEmpBirth());
            profileDTO.setEmpDel(employee.getEmpDel());
            profileDTO.setEmpHead(employee.getEmpHead());
            profileDTO.setEmpName(employee.getEmpName());
            profileDTO.setEmpGender(employee.getEmpGender());
            profileDTO.setEmpIntroduce(employee.getEmpIntroduce());
            profileDTO.setEmpBirthNum(employee.getEmpBirthNum());
            profileDTO.setEmpPnum(employee.getEmpPnum());
            profileDTO.setEmpAddr(employee.getEmpAddr());
            profileDTO.setEmpPos(employee.getEmpPos());
            profileDTO.setEmpEmail(employee.getEmpEmail());
            profileDTO.setStartDay(employee.getStartDay());
            profileDTO.setEndDay(employee.getEndDay());
            profileDTO.setEmpDelInfo(employee.getEmpDelInfo());
            profileDTO.setEmpBanknum(employee.getEmpBanknum());
            return profileDTO;
        } else {
            return new ProfileDTO();
        }
    }
}
