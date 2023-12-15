package shun_chih.com.chih.member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import shun_chih.com.chih.member.data.po.MemberLoginDocument;

@Repository
public interface MemberLoginRepository extends MongoRepository<MemberLoginDocument, String> {

    Page<MemberLoginDocument> findAllByUsernameOrderByCreatedByDesc(String username, Pageable pageable);
}
