package ac.su.erp.domain;

import ac.su.erp.constant.DeleteStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// 공지사항 엔티티
@Entity
@Table(name = "NOTICE")
@Getter
@Setter
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTICE_ID")    // 공지사항 ID
    private Long noticeId;

    @Column(name = "CREATEDATE", nullable = false)  // 작성일
    private LocalDateTime createDate = LocalDateTime.now();   // 기본값은 현재 날짜

    @Enumerated(EnumType.STRING)
    @Column(name = "DEL", nullable = false) // 삭제여부
    private DeleteStatus del = DeleteStatus.ACTIVE; // 기본값은 ACTIVE

    @Column(name = "TITLE", nullable = false)   // 제목
    private String title;

    @Column(name = "CONTENTS", nullable = false)    // 내용
    private String contents;

    // N:1 매핑
    @ManyToOne
    @JoinColumn(name = "EMP_NUM")   // 사원번호
    private Employee employee;

}