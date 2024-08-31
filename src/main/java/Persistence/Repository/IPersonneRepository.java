package Persistence.Repository;

import Entities.Business.Personne.Personne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IPersonneRepository extends JpaRepository<Personne, Long> {

    // Find by Nom
    List<Personne> findByIdentite(String nom);


    // Find All Personnes Ordered by Nom (Alphabetical Order)
    List<Personne> findAllByOrderByIdentiteAsc();

    // Find All Personnes Ordered by Nom (Reverse Alphabetical Order)
    List<Personne> findAllByOrderByIdentiteDesc();
    // Method to find a Personne by nom, prenom, and dateNaissance
    @Query("SELECT p FROM Personne p WHERE p.identite = :identite AND p.dateNaissance = :dateNaissance")
   Optional<Personne> findByIdentiteAndDateNaissance(String identite, String dateNaissance);
}