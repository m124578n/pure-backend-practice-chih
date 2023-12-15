package shun_chih.com.chih.member.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import shun_chih.com.chih.member.data.enu.MemberLoginType;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginResponseDTO {

    String username;

    MemberLoginType type;

    String createdBy;

    String createdByStr;
}
