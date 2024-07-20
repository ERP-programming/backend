package ac.su.erp.service;

import ac.su.erp.constant.DeleteStatus;
import ac.su.erp.domain.BreakTime;
import ac.su.erp.domain.Employee;
import ac.su.erp.dto.BreakTimeDTO;
import ac.su.erp.repository.BreakTimeRepository;
import ac.su.erp.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BreakTimeService {

    private final BreakTimeRepository breakTimeRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public BreakTimeService(BreakTimeRepository breakTimeRepository, EmployeeRepository employeeRepository) {
        this.breakTimeRepository = breakTimeRepository;
        this.employeeRepository = employeeRepository;
    }

    public BreakTimeDTO getBreakTimeById(Long id) {
        BreakTime breakTime = breakTimeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Break time not found with ID: " + id));
        return convertToDTO(breakTime);
    }

    public List<BreakTimeDTO> getAllBreakTimes() {
        return breakTimeRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void createBreakTime(BreakTimeDTO breakTimeDTO) {
        BreakTime breakTime = convertToEntity(breakTimeDTO);
        breakTimeRepository.save(breakTime);
    }

    public void updateBreakTime(Long id, BreakTimeDTO breakTimeDTO) {
        BreakTime breakTime = breakTimeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Break time not found with ID: " + id));
        breakTime.setBtStart(breakTimeDTO.getBtStart());
        breakTime.setBtEnd(breakTimeDTO.getBtEnd());
        breakTime.setBtWhy(breakTimeDTO.getBtWhy());
        Employee employee = employeeRepository.findById(breakTimeDTO.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + breakTimeDTO.getEmployeeId()));
        breakTime.setEmployee(employee);
        breakTimeRepository.save(breakTime);
    }

    public void deleteBreakTime(Long id) {
        BreakTime breakTime = breakTimeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Break time not found with ID: " + id));
        breakTime.setDel(DeleteStatus.DELETED);
        breakTimeRepository.save(breakTime);
    }

    private BreakTimeDTO convertToDTO(BreakTime breakTime) {
        if (breakTime == null) {
            return null;
        }

        BreakTimeDTO breakTimeDTO = new BreakTimeDTO();
        breakTimeDTO.setBtId(breakTime.getBtId());
        breakTimeDTO.setBtStart(breakTime.getBtStart());
        breakTimeDTO.setBtEnd(breakTime.getBtEnd());
        breakTimeDTO.setBtWhy(breakTime.getBtWhy());
        breakTimeDTO.setDel(breakTime.getDel());
        breakTimeDTO.setEmployeeId(breakTime.getEmployee().getEmpNum());
        return breakTimeDTO;
    }

    private BreakTime convertToEntity(BreakTimeDTO breakTimeDTO) {
        BreakTime breakTime = new BreakTime();
        breakTime.setBtStart(breakTimeDTO.getBtStart());
        breakTime.setBtEnd(breakTimeDTO.getBtEnd());
        breakTime.setBtWhy(breakTimeDTO.getBtWhy());
        breakTime.setDel(breakTimeDTO.getDel());
        Employee employee = employeeRepository.findById(breakTimeDTO.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + breakTimeDTO.getEmployeeId()));
        breakTime.setEmployee(employee);
        return breakTime;
    }
}
