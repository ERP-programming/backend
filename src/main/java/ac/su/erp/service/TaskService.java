package ac.su.erp.service;

import ac.su.erp.domain.Employee;
import ac.su.erp.domain.Task;
import ac.su.erp.dto.TaskDTO;
import ac.su.erp.repository.EmployeeRepository;
import ac.su.erp.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final EmployeeRepository employeeRepository;

    public TaskService(TaskRepository taskRepository, EmployeeRepository employeeRepository) {
        this.taskRepository = taskRepository;
        this.employeeRepository = employeeRepository;
    }

    // 특정 사원의 모든 Task 불러오기
    public List<Task> getAllTasks() {
        Employee employee = new Employee();
        employee.setEmpNum(1L); ////////// 1번 사원 가데이터 입력
        return taskRepository.findByEmployee(employee);
    }

    // 특정 공지 상세내용
    public Task findById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    // Task 생성
    public void saveTask(TaskDTO taskDTO) {
        // 새 Task 객체 생성
        Task newTask = new Task();
        newTask.setContent(taskDTO.getContent());

        // 직원 번호를 기반으로 Employee 객체를 조회
        Optional<Employee> employeeOpt = employeeRepository.findById(taskDTO.getEmp_num());
        if (employeeOpt.isPresent()) {
            Employee employee = employeeOpt.get();
            newTask.setEmployee(employee);
            taskRepository.save(newTask);
        } else {
            throw new IllegalArgumentException("Invalid employee number: " + taskDTO.getEmp_num());
        }
    }

    // Task 수정
    public Task updateTask(Long id, Task task) {
        Task updatedTask = taskRepository.findById(id).orElse(null);
        if (updatedTask != null) {
            updatedTask.setContent(task.getContent());
            taskRepository.save(updatedTask);
            return updatedTask;
        } else {
            return null;
        }
    }

    // Task 삭제
    public boolean deleteTask(Long id) {
        Task task = taskRepository.findById(id).orElse(null);
        if (task != null) {
            taskRepository.delete(task);
            return true;
        } else {
            return false;
        }
    }

//    UserDetails 용 getAllTasks 로직
    /*
    public List<Task> getAllTasks() {
        // 로그인한 사용자의 정보를 가져옴
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();

        // EmployeeService를 사용하여 Employee 엔티티를 조회합니다.
        Employee employee = employeeService.findByUsername(username);

        // 해당 사용자의 Task를 조회합니다.
        return taskRepository.findByEmployee(employee);
    }
    */
}
