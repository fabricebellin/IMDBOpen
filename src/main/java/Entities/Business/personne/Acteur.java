package entities.business.personne;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "acteur")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Acteur {

    @Setter
    @Getter
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

    @Getter
    @Setter
    @Column(name = "taille")
    private String taille;

    // Constructor for Acteur-specific fields
    public Acteur(String idImdb, String taille) {
        this.idImdb = idImdb;
        this.taille = taille;
    }

    // Optional: you can add another constructor or setter method to set the Personne object if needed
    public void setPersonneDetails(String identite, String dateNaissance) {
        this.personne = new Personne();
        this.personne.setIdentite(identite);
        this.personne.setDateNaissance(dateNaissance);
    }

    // Alternatively, you can provide a setter for Personne
    public void setPersonne(Personne personne) {
        this.personne = personne;
    }
}
