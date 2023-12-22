package shun_chih.com.chih.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import shun_chih.com.chih.config.security.JwtTokenService;
import shun_chih.com.chih.config.security.LoginRequestDto;
import shun_chih.com.chih.member.data.dto.MemberAboutRequestDTO;
import shun_chih.com.chih.member.data.dto.MemberLoginResponseDTO;
import shun_chih.com.chih.member.data.dto.MemberPasswordRequestDTO;
import shun_chih.com.chih.member.data.dto.MemberResponseDTO;

import java.security.Principal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Tag(name = "Member")
@RestController
@RequestMapping("/api/v1.0")
@SecurityRequirement(name = "Authentication")
public class MemberController {

    private final JwtTokenService jwtTokenService;
    private final MemberService memberService;

    public MemberController(JwtTokenService jwtTokenService, MemberService memberService) {
        this.jwtTokenService = jwtTokenService;
        this.memberService = memberService;
    }

    MemberResponseDTO getMembers(Principal principal) {
        return memberService
                .query(principal.getName())
                .map(v -> MemberResponseDTO
                        .builder()
                        .id(v.getId())
                        .username(v.getUsername())
                        .about(v.getAbout())
                        .avatarLink(v.getAvatarLink())
                        .role(v.getRole())
                        .createdDateStr(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(OffsetDateTime.ofInstant(Instant.ofEpochMilli(v.getCreatedDate()), ZoneOffset.ofHours(8))))
                        .build()
                )
                .orElseThrow(() -> new RuntimeException("getMembers Err"));
    }

    @Operation(summary = "登入")
    @PostMapping("/members:login")
    ResponseEntity<String> postMembersLogin(@RequestBody LoginRequestDto loginRequestDto) {
        return memberService
                .login(loginRequestDto.getUsername(), loginRequestDto.getPassword())
                .map(v -> jwtTokenService.generate(v.getUsername(), 60L * 60L * 1000L, UUID.randomUUID().toString()))
                .map(v -> ResponseEntity
                        .status(HttpStatus.OK)
                        .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", v))
                        .body(v)
                )
                .orElseThrow(() -> new RuntimeException("postMembersLogin Err"));
    }

    @Operation(summary = "修改自介")
    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/members:modifyAbout")
    ResponseEntity<Object> patchMembersAbout(
            @RequestBody MemberAboutRequestDTO memberAboutRequestDTO,
            Principal principal
            ) {
        return memberService
                .modifyAbout(principal.getName(), memberAboutRequestDTO.getAbout())
                .map(v -> ResponseEntity.noContent().build())
                .orElseThrow(() -> new RuntimeException("patchMembersAbout Err"));
    }

    @Operation(summary = "修改密碼")
    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/members:modifyPassword")
    ResponseEntity<Object> patchMembersPassword(
            @RequestBody MemberPasswordRequestDTO memberPasswordRequestDTO,
            Principal principal
            ) {
        return memberService
                .modifyPassword(principal.getName(), memberPasswordRequestDTO.getOldPassword(), memberPasswordRequestDTO.getNewPassword())
                .map(v -> ResponseEntity.noContent().build())
                .orElseThrow(() -> new RuntimeException("patchMembersPassword Err"));
    }

    @Operation(summary = "凍結用戶")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/members/{username}")
    ResponseEntity<Object> deleteMembers(@PathVariable String username) {
        return memberService
                .remove(username)
                .map(v -> ResponseEntity.noContent().build())
                .orElseThrow(() -> new RuntimeException("deleteMembers Err"));
    }

    @Operation(summary = "登入記錄")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/members/records")
    Page<MemberLoginResponseDTO> getMembersRecords(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            Principal principal) {
        return memberService.queryMembersRecords(principal.getName(), page, size);
    }

}
