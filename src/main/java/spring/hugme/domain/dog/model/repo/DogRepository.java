package spring.hugme.domain.dog.model.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.hugme.domain.community.entity.Favorite;
import spring.hugme.domain.dog.model.entity.Dog;
import spring.hugme.domain.user.entity.Member;

@Repository
public interface DogRepository extends JpaRepository<Dog, Long> {

  List<Dog> findByMember(Member member);
}
