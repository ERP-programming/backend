package ac.su.erp.dto;

import ac.su.erp.constant.DeleteStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BreakTimeDTO {
    private Long btId;
    private LocalDate btStart;
    private LocalDate btEnd;
    private String btWhy;
    private DeleteStatus del;
    private Long employeeId;
}
