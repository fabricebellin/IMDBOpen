package persistence.repository;


import entities.business.personne.Acteur;
import entities.business.personne.Personne;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IActeurRepository extends JpaRepository<Acteur, Long> {



    Optional<Acteur> findById(Long id);

    @Query("SELECT a FROM Acteur a WHERE a.idImdb = :imdb")
    Optional<Acteur> findByImdb(@Param("imdb") String imdb);

    Optional<Acteur> findByPersonne(Personne personne);

    List<Acteur> findAllWithFilters(String identite, String dateNaissance, PageRequest of);
}