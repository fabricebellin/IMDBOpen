package persistence.repository;


import entities.business.Film.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Repository
public interface IFilmRepository extends JpaRepository<Film, Long> {

    // Find films by IMDb ID
    Optional<Film> findByImdb(String imdb);


    // Find films by name (partial match, case insensitive)
    List<Film> findByNomContainingIgnoreCase(String nom);

    // Find films by release year
    List<Film> findByAnnee(@NotBlank(message = "Année cannot be blank") @Size(max = 10, message = "Année should not exceed 10 characters") String annee);

    // Find films with a rating above a certain threshold
    List<Film> findByRatingGreaterThanEqual(@NotBlank(message = "Rating cannot be blank") @Size(max = 4, message = "Rating should not exceed 4 characters") String rating);

    // Find films by country name (assuming 'paysList' contains 'Pays' entities)
    @Query("SELECT f FROM Film f WHERE f.pays.name = :paysName")
    List<Film> findByPaysName(@Param("paysName") String paysName);

    // Find films by genre name (assuming 'genres' contains 'Genre' entities)
    @Query("SELECT f FROM Film f JOIN f.genresl g WHERE g.name = :genreName")
    List<Film> findByGenreName(@Param("genreName") String genreName);

    // Find films by language
    List<Film> findByLangueContainingIgnoreCase(String langue);

    // Find films by location of shooting
    List<Film> findByLieuTourContainingIgnoreCase(String lieuTour);

    // Custom query to find films with multiple filters and sorting
    @Query("SELECT f FROM Film f "
            + "WHERE "
            + "(:nom IS NULL OR LOWER(f.nom) LIKE LOWER(CONCAT('%', :nom, '%'))) AND "
            + "(:annee IS NULL OR f.annee = :annee) AND "
            + "(:rating IS NULL OR f.rating >= :rating) AND "
            + "(:paysName IS NULL OR f.pays.name = :paysName) AND "
            + "(:genre IS NULL OR EXISTS (SELECT 1 FROM f.genresl g WHERE g.name = :genreName)) "
            + "ORDER BY "
            + "CASE WHEN :sortBy = 'nom' THEN f.nom END ASC, "
            + "CASE WHEN :sortBy = 'annee' THEN f.annee END ASC, "
            + "CASE WHEN :sortBy = 'rating' THEN f.rating END DESC")
    List<Film> findFilmsWithFiltersAndSorting(
            @Param("nom") String nom,
            @Param("annee") String annee,
            @Param("rating") String rating,
            @Param("paysName") String paysName,
            @Param("genreName") String genreName,
            @Param("sortBy") String sortBy
    );

    // Custom query method to find films by actor
    //@Query("SELECT f FROM Film f JOIN f.acteurs a WHERE a.id = :actorId")
    //List<Film> findFilmsByActor(@Param("actorId") Long actorId);


}