package ac.su.erp.service;

import ac.su.erp.domain.WorkTime;
import ac.su.erp.repository.WorkTimeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkTimeService {

    private final WorkTimeRepository workTimeRepository;

    public WorkTimeService(WorkTimeRepository workTimeRepository) {
        this.workTimeRepository = workTimeRepository;
    }

    public List<WorkTime> getAllWorkTimes() {
        return workTimeRepository.findAll();
    }
}
