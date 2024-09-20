package web.model.dto;



import entities.business.personne.Acteur;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Data
public class ActeurDTO {
    private Long id;
    private String idImdb;
    private String taille; // Add taille field
    private PersonneDTO personne; // Updated to include PersonneDTO
    private String dateNaissance; // Keep dateNaissance as String
    private String lieuNaissance; // Add lieuNaissance field
    private String url; // Add url field
    private List<FilmDTO> films; // Use FilmDTO instead of Film
    private List<RoleDTO> roles; // Use RoleDTO for roles

    private static final DateTimeFormatter[] DATE_FORMATTERS = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.US),
            DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.US),
            DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.US),
            DateTimeFormatter.ofPattern("yyyy/MM/dd", Locale.US),
            DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.US),
            DateTimeFormatter.ofPattern("MM-dd-yyyy", Locale.US)
    };

    public static ActeurDTO fromEntity(Acteur acteur) {
        ActeurDTO dto = new ActeurDTO();
        dto.setId(acteur.getId());
        dto.setIdImdb(acteur.getIdImdb());
        dto.setTaille(acteur.getTaille());
        dto.setPersonne(PersonneDTO.fromEntity(acteur.getPersonne()));
        dto.setDateNaissance(acteur.getPersonne().getDateNaissance());
        dto.setLieuNaissance(acteur.getPersonne().getLieuNaissance());
        dto.setUrl(acteur.getPersonne().getUrl());
        return dto;
    }

    public Acteur toEntity() {
        Acteur acteur = new Acteur();
        acteur.setIdImdb(this.idImdb);
        acteur.setTaille(this.taille);
        return acteur;
    }

    public boolean isValidDateNaissance() {
        if (this.personne == null || this.personne.getDateNaissance().isEmpty()) {
            return false;
        }
        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                LocalDate.parse(this.personne.getDateNaissance(), formatter);
                return true;
            } catch (DateTimeParseException e) {
                // Ignore and try the next format
            }
        }
        return false;
    }
}
