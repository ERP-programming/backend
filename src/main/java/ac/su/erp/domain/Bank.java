package ac.su.erp.domain;

import ac.su.erp.constant.DeleteStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

// 은행정보 엔티티
@Entity
@Table(name = "BANK")
@Getter
@Setter
public class Bank {
    @Id
    @Column(name = "BANK_CODE", nullable = false) // 은행코드
    private String bankCode;

    @Column(name = "BANK_NAME", nullable = false)   // 은행명
    private String bankName;

    @Enumerated(EnumType.STRING)
    @Column(name = "DEL", nullable = false) // 삭제여부
    private DeleteStatus del = DeleteStatus.ACTIVE; // 기본값은 ACTIVE

    // 1:N 매핑
    @OneToMany(mappedBy = "bank")   // 사원번호
    private List<Employee> employees;
}