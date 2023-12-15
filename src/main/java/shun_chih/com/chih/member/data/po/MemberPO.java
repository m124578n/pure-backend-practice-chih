package shun_chih.com.chih.member.data.po;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import shun_chih.com.chih.member.data.enu.MemberRole;
import shun_chih.com.chih.member.data.enu.MemberStatus;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class MemberPO implements Serializable {

    @Serial
    private static final long serialVersionUID = -7853427416004116464L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(unique = true)
    String username;

    String password;

    String about;

    String avatarLink;

    @Enumerated(value = EnumType.STRING)
    MemberRole role;

    @Enumerated(value = EnumType.STRING)
    MemberStatus status;

    @CreatedBy
    String createdBy;

    @CreatedDate
    Long createdDate;

    @LastModifiedBy
    String lastModifiedBy;

    @LastModifiedDate
    Long lastModifiedDate;
}
