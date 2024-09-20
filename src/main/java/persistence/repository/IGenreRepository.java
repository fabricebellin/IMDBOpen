package persistence.repository;


import entities.business.genre.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IGenreRepository extends JpaRepository<Genre, Long> {

    // Find or create a genre by name
    @Query("SELECT g FROM Genre g WHERE g.name = :name")
    Genre findByName(@Param("name") String name);

    @Query("SELECT g FROM Genre g WHERE g.name = :name")
    default Genre findOrCreateGenre(@Param("name") String name) {
        Genre genre = findByName(name);
        if (genre == null) {
            genre = new Genre();
            genre.setName(name);
            save(genre);
        }
        return genre;
    }
}