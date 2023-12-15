package shun_chih.com.chih.member.data.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import shun_chih.com.chih.member.data.enu.MemberLoginType;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class MemberLoginDocument {

    @Id
    String id;

    @Indexed
    String username;

    MemberLoginType type;

    @CreatedBy
    String createdBy;

    @CreatedDate
    Long createdDate;

    @LastModifiedBy
    String lastModifiedBy;

    @LastModifiedDate
    Long lastModifiedDate;

}
