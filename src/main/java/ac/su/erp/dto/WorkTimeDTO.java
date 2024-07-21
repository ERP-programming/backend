package ac.su.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkTimeDTO {
    private LocalTime onWork;
    private LocalTime offWork;
    private Long emp_num;
}