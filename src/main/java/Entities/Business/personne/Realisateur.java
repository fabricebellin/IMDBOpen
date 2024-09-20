package entities.business.personne;

import entities.business.personne.Personne;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "realisateur")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Realisateur  {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "personne_id", referencedColumnName = "id")
    private Personne personne;

    @Getter
    @Setter
    @Column(name = "id_imdb")
    private String idImdb;

    //@ManyToMany
    //@JoinTable(
    //        name = "film_realisateur",
    //        joinColumns = @JoinColumn(name = "realisateur_id_imdb"),
    //        inverseJoinColumns = @JoinColumn(name = "film_imdb")
    //)
    //private List<Film> films = new ArrayList<>();


    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    public Realisateur(String identite, String dateNaissance, String lieuNaissance, String url, String idImdb) {
        this.personne = new Personne();
        this.personne.setIdentite(identite);
        this.personne.setDateNaissance(dateNaissance);
        this.personne.setLieuNaissance(lieuNaissance);
        this.personne.setUrl(url);
        this.idImdb = idImdb;
        this.createdDate = LocalDateTime.now();
    }


}
