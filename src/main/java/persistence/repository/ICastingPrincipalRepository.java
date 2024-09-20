package persistence.repository;


import entities.business.CastingPrincipal.CastingPrincipal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICastingPrincipalRepository extends JpaRepository<CastingPrincipal, Long> {
    Optional<CastingPrincipal> findByFilmIdAndActeurId(String filmId, String acteurId);
    boolean existsByFilmIdAndActeurId(String filmId, String acteurId);
    void deleteByFilmIdAndActeurId(String filmId, String acteurId);
}
