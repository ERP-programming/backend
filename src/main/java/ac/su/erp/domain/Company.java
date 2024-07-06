package ac.su.erp.domain;

import ac.su.erp.constant.DeleteStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// 회사정보 엔티티
@Entity
@Table(name = "COMPANY")
@Getter
@Setter
public class Company {
    @Id
    @Column(name = "COM_NUM")   // 사업자 등록번호
    private String comNum;

    @Column(name = "COM_NAME", nullable = false)    // 회사명
    private String comName;

    @Column(name = "COM_CEONAME", nullable = false)   // 대표자명
    private String comCeoname;

    @Column(name = "COM_ADDR", nullable = false)    // 회사 주소
    private String comAddr;

    @Column(name = "COM_BIRTH", nullable = false)   // 회사 설립일
    private String comBirth;

    @Column(name = "COM_LOWNUM", nullable = false)  // 법인 등록번호
    private String comLownum;

    @Column(name = "COM_CEOP", nullable = false)    // 회사 연락처
    private String comCeop;

    @Column(name = "COM_PROFILE", nullable = false)   // 회사 프로필 사진
    private String comProfile = "default.jpg";

    @Enumerated(EnumType.STRING)
    @Column(name = "DEL", nullable = false) // 삭제여부
    private DeleteStatus del = DeleteStatus.ACTIVE; // 기본값은 ACTIVE

    // DeleteStatus의 기본값은 ACTIVE & 변경불가
    private void setDel(DeleteStatus del) {
        this.del = del;
    }
}