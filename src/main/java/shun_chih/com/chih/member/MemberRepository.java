package shun_chih.com.chih.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import shun_chih.com.chih.member.data.po.MemberPO;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberPO, Long>, JpaSpecificationExecutor<MemberPO> {

    Optional<MemberPO> findByUsername(String username);
}
