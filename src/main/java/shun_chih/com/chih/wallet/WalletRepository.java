package shun_chih.com.chih.wallet;

import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import shun_chih.com.chih.wallet.data.po.WalletPO;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<WalletPO, Long>, JpaSpecificationExecutor<WalletPO> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional
    Optional<WalletPO> findByMemberId(Long memberId);
}
