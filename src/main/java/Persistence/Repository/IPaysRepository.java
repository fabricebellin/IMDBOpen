package Persistence.Repository;

import Entities.Business.Pays.Pays;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPaysRepository extends JpaRepository<Pays, Long> {
    Optional<Pays> findByName(String name);
}