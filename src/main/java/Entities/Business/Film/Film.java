package entities.business.Film;


import entities.business.genre.Genre;
import entities.business.pays.Pays;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Film")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Film implements Entities.generic.IEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "imdb")
    @NotBlank(message = "IMDb identifier cannot be blank")
    @Size(max = 50, message = "IMDb identifier should not exceed 50 characters")
    private String imdb;

    @Column(name = "nom")
    @NotBlank(message = "Film name cannot be blank")
    @Size(max = 255, message = "Film name should not exceed 255 characters")
    private String nom;

    @Column(name = "annee")
    @NotBlank(message = "Année cannot be blank")
    @Size(max = 10, message = "Année should not exceed 10 characters")
    private String annee;

    @Column(name = "rating")
    @NotBlank(message = "Rating cannot be blank")
    @Size(max = 4, message = "Rating should not exceed 4 characters")
    private String rating;

    @Column(name = "url")
    @Size(max = 500, message = "URL should not exceed 500 characters")
    private String url;

    @Column(name = "lieu_tour")
    @Size(max = 2255, message = "Filming location should not exceed 2255 characters")
    private String lieuTour;

    @Column(name = "langue")
    @NotBlank(message = "Language cannot be blank")
    @Size(max = 100, message = "Language should not exceed 100 characters")
    private String langue;

    @Column(name = "resume", columnDefinition = "TEXT")
    @NotBlank(message = "Resume cannot be blank")
    private String resume;

    @Column(name = "genres")
    @Size(max = 255, message = "Genres string should not exceed 255 characters")
    private String genres; // For search features

    @ManyToOne
    @JoinColumn(name = "id_pays")
    private Pays pays;

    //@ManyToMany
    //@JoinTable(
    //        name = "Film_Acteur",
    //        joinColumns = @JoinColumn(name = "film_imdb"),
    //        inverseJoinColumns = @JoinColumn(name = "acteur_id_imdb")
    //)
    //private List<Acteur> acteurs = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "film_genre",
            joinColumns = @JoinColumn(name = "film_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genresl = new HashSet<>();

    //@ManyToMany(cascade = CascadeType.ALL)
    //@JoinTable(
    //        name = "film_realisateur",
    //        joinColumns = @JoinColumn(name = "film_imdb"),
    //        inverseJoinColumns = @JoinColumn(name = "realisateur_id_imdb")
    //)
    //private Set<Realisateur> realisateurs = new HashSet<>();


    public LocalDateTime getCreatedDate() {
        return null; // Implement as needed
    }



    public void setCreatedDate(LocalDateTime createdDate) {
        // Implement as needed
    }


    public LocalDateTime getUpdatedDate() {
        return null; // Implement as needed
    }


    public void setUpdatedDate(LocalDateTime updatedDate) {
        // Implement as needed
    }


    public boolean isDeleted() {
        return false; // Implement as needed
    }


    public void setDeleted(boolean deleted) {
        // Implement as needed
    }
}
