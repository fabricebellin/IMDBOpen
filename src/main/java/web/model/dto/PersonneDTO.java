package web.model.dto;



import entities.business.personne.Personne;
import lombok.Data;

@Data
public class PersonneDTO {
    private Long id;
    private String identite;
    private String dateNaissance;
    private String lieuNaissance;
    private String url;

    public static PersonneDTO fromEntity(Personne personne) {
        PersonneDTO dto = new PersonneDTO();
        dto.setId(personne.getId());
        dto.setIdentite(personne.getIdentite());
        dto.setDateNaissance(personne.getDateNaissance());
        dto.setLieuNaissance(personne.getLieuNaissance());
        dto.setUrl(personne.getUrl());
        return dto;
    }

    public Personne toEntity() {
        Personne personne = new Personne();
        personne.setId(this.id);
        personne.setIdentite(this.identite);
        personne.setDateNaissance(this.dateNaissance);
        personne.setLieuNaissance(this.lieuNaissance);
        personne.setUrl(this.url);
        return personne;
    }
}
