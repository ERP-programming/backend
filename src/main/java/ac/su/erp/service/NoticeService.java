package ac.su.erp.service;

import ac.su.erp.domain.Employee;
import ac.su.erp.domain.Notice;
import ac.su.erp.dto.NoticeDTO;
import ac.su.erp.repository.EmployeeRepository;
import ac.su.erp.repository.NoticeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final EmployeeRepository employeeRepository;


    public NoticeService(NoticeRepository noticeRepository, EmployeeRepository employeeRepository) {
        this.noticeRepository = noticeRepository;
        this.employeeRepository = employeeRepository;
    }

    // 모든 공지 불러오기
    public List<Notice> getAllNotices() {
        return noticeRepository.findAll();
    }

    // 특정 공지 상세내용
    public Notice findById(Long id) {
        return noticeRepository.findById(id).orElse(null);
    }

    // 공지사항 저장
    @Transactional
    public void saveNotice(NoticeDTO noticeDTO) {
        // 새 Notice 객체 생성
        Notice notice = new Notice();
        notice.setTitle(noticeDTO.getTitle());
        notice.setContents(noticeDTO.getContents());

        // emp_num을 사용해 Employee 엔티티를 찾음
        Optional<Employee> employeeOpt = employeeRepository.findById(noticeDTO.getEmp_num());
        if (employeeOpt.isPresent()) {
            notice.setEmployee(employeeOpt.get());
        } else {
            throw new IllegalArgumentException("유효하지 않은 사원번호입니다.");
        }

        // 공지사항 저장
        noticeRepository.save(notice);
    }

    // 공지사항 수정
    public Notice updateNotice(Long id, Notice noticeDetails) {
        Notice notice = noticeRepository.findById(id).orElse(null);
        if (notice != null) {
            notice.setTitle(noticeDetails.getTitle());
            notice.setContents(noticeDetails.getContents());
            return noticeRepository.save(notice);
        } else {
            return null;
        }
    }

    // 공지사항 삭제
    public boolean deleteNotice(Long id) {
        if (noticeRepository.existsById(id)) {
            noticeRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
