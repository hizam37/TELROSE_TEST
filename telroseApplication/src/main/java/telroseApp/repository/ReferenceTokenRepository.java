package telroseApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telroseApp.model.ReferenceToken;

import java.util.Optional;
@Repository
public interface ReferenceTokenRepository extends JpaRepository<ReferenceToken,Long> {
    Optional<ReferenceToken> findByToken(String token);
}
