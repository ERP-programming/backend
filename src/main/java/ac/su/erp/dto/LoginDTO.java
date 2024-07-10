package ac.su.erp.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
public class LoginDTO {

    private Long empNum;
    private String password;
}