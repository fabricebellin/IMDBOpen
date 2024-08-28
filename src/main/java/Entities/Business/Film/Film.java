package Entities.Business.Film;

import Entities.Business.Pays.Pays;
import Entities.Business.Personne.Acteur;
import Entities.Business.Role.Role;
import Entities.Generic.IEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Film")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Film implements IEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "IMDb identifier cannot be blank")
    @Size(max = 50, message = "IMDb identifier should not exceed 50 characters")
    private String imdb;

    @NotBlank(message = "Film name cannot be blank")
    @Size(max = 255, message = "Film name should not exceed 255 characters")
    private String nom;

    @NotBlank(message = "Année cannot be blank")
    @Size(max = 10, message = "Année should not exceed 10 characters")
    private String annee;

    @NotBlank(message = "Rating cannot be blank")
    @Size(max = 4, message = "Rating should not exceed 4 characters")
    private String rating;

    @Size(max = 500, message = "URL should not exceed 500 characters")
    private String url;

    @Size(max = 2255, message = "Filming location should not exceed 2255 characters")
    private String lieuTour;

    @NotBlank(message = "Language cannot be blank")
    @Size(max = 100, message = "Language should not exceed 100 characters")
    private String langue;

    @NotBlank(message = "Resume cannot be blank")
    @Size(max = 10001, message = "Resume should not exceed 10001 characters")
    private String resume;

    private String pays;

    @ManyToMany(mappedBy = "films")
    private List<Acteur> acteurs = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "Film_Genre",
            joinColumns = @JoinColumn(name = "FilmID"),
            inverseJoinColumns = @JoinColumn(name = "GenreID")
    )
    private List<Genre> genres;

    @ManyToMany
    @JoinTable(
            name = "Film_Pays",
            joinColumns = @JoinColumn(name = "FilmID"),
            inverseJoinColumns = @JoinColumn(name = "PaysID")
    )
    private List<Pays> paysList;

    @OneToMany(mappedBy = "film", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Role> roles = new ArrayList<>();

    @Override
    @Transient
    public LocalDateTime getCreatedDate() {
        return null; // Implement as needed
    }

    @Override
    @Transient
    public void setCreatedDate(LocalDateTime createdDate) {
        // Implement as needed
    }

    @Override
    @Transient
    public LocalDateTime getUpdatedDate() {
        return null; // Implement as needed
    }

    @Override
    @Transient
    public void setUpdatedDate(LocalDateTime updatedDate) {
        // Implement as needed
    }

    @Override
    @Transient
    public boolean isDeleted() {
        return false; // Implement as needed
    }

    @Override
    @Transient
    public void setDeleted(boolean deleted) {
        // Implement as needed
    }
}
