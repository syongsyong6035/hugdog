package spring.hugme.domain.user.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.hugme.domain.user.entity.Member;
import spring.hugme.domain.user.repository.custom.UserRepositoryCustom;

@Repository
public interface UserRepository extends JpaRepository<Member, Long> , UserRepositoryCustom {

    Optional<Member> findByUserId(String userId);
    boolean existsByUserId(String userId);
    boolean existsByEmail(String email);
}

