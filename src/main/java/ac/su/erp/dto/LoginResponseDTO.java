package ac.su.erp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter @Setter
@AllArgsConstructor
public class LoginResponseDTO {
    private Long empNum;
    private String password;
    private String accessToken;
}
