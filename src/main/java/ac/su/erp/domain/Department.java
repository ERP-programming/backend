
package ac.su.erp.domain;

import ac.su.erp.constant.DeleteStatus;
import ac.su.erp.constant.DepartmentEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

// 부서 엔티티
@Entity
@Table(name = "DEPARTMENT")
@Getter
@Setter
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DEPT_NO") // 부서번호
    private Long deptNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "DEPT_NAME") // 부서명
    private DepartmentEnum deptName;

    @Enumerated(EnumType.STRING)
    @Column(name = "DEL", nullable = false) // 삭제여부
    private DeleteStatus del = DeleteStatus.ACTIVE; // 기본값은 ACTIVE

    // 1:N 매핑
    @OneToMany(mappedBy = "department")
    private List<Employee> employees;

}
