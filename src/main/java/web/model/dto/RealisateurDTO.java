package web.model.dto;


import entities.business.personne.Personne;
import entities.business.personne.Realisateur;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class RealisateurDTO {

    private Long id; // ID of the Realisateur
    private String identite;
    private String dateNaissance; // Birth date of the Realisateur
    private String lieuNaissance; // Birth place of the Realisateur
    private String url; // URL of the Realisateur's profile
    private String idImdb; // IMDb ID of the Realisateur
    private LocalDateTime createdDate; // Date when the Realisateur was created
    private LocalDateTime updatedDate; // Date when the Realisateur was last updated

    // Parameterized constructor
    public RealisateurDTO(Long id, String identite, String dateNaissance,
                          String lieuNaissance, String url, String idImdb,
                          LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.id = id;
        this.identite = identite;
        this.dateNaissance = dateNaissance;
        this.lieuNaissance = lieuNaissance;
        this.url = url;
        this.idImdb = idImdb;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    // Static method to convert a Realisateur entity to a RealisateurDTO
    public static RealisateurDTO fromEntity(Realisateur realisateur) {
        if (realisateur == null || realisateur.getPersonne() == null) {
            return null;
        }

        return new RealisateurDTO(
                realisateur.getId(),
                realisateur.getPersonne().getIdentite(),
                realisateur.getPersonne().getDateNaissance(),
                realisateur.getPersonne().getLieuNaissance(),
                realisateur.getPersonne().getUrl(),
                realisateur.getIdImdb(),
                realisateur.getCreatedDate(),
                realisateur.getUpdatedDate()
        );
    }

    // Convert RealisateurDTO to Realisateur entity
    public Realisateur toEntity() {
        Realisateur realisateur = new Realisateur();
        realisateur.setId(this.id);

        // Create and set Personne object
        Personne personne = new Personne();
        personne.setIdentite(this.identite);
        personne.setDateNaissance(this.dateNaissance);
        personne.setLieuNaissance(this.lieuNaissance);
        personne.setUrl(this.url);


        realisateur.setIdImdb(this.idImdb);
        realisateur.setCreatedDate(this.createdDate);
        realisateur.setUpdatedDate(this.updatedDate);

        return realisateur;
    }
}
