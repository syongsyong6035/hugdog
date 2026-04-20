package spring.hugme.domain.user.repository.custom;

import java.util.Optional;
import spring.hugme.domain.user.entity.Member;

public interface UserRepositoryCustom {

  Optional<Member> findUserId(String userId);

}
