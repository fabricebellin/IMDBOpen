package web.model.dto;


import entities.business.CastingPrincipal.CastingPrincipal;

import java.time.LocalDateTime;

public class CastingPrincipalDTO {
    private Long idCasting;
    private String filmId;
    private String acteurId;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    // Getters and Setters
    public Long getIdCasting() {
        return idCasting;
    }

    public void setIdCasting(Long idCasting) {
        this.idCasting = idCasting;
    }

    public String getFilmId() {
        return filmId;
    }

    public void setFilmId(String filmId) {
        this.filmId = filmId;
    }

    public String getActeurId() {
        return acteurId;
    }

    public void setActeurId(String acteurId) {
        this.acteurId = acteurId;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    // Convert from Entity to DTO
    public static CastingPrincipalDTO fromEntity(CastingPrincipal castingPrincipal) {
        CastingPrincipalDTO dto = new CastingPrincipalDTO();
        dto.setIdCasting(castingPrincipal.getIdCasting());
        dto.setFilmId(castingPrincipal.getFilmId());
        dto.setActeurId(castingPrincipal.getActeurId());
        return dto;
    }

    // Convert from DTO to Entity
    public static CastingPrincipal toEntity(CastingPrincipalDTO dto) {
        CastingPrincipal castingPrincipal = new CastingPrincipal();
        castingPrincipal.setIdCasting(dto.getIdCasting());
        castingPrincipal.setFilmId(dto.getFilmId());
        castingPrincipal.setActeurId(dto.getActeurId());
        return castingPrincipal;
    }
}