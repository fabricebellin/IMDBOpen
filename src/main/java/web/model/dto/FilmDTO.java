package web.model.dto;


import entities.business.Film.Film;
import entities.business.pays.Pays;
import lombok.Data;

@Data
public class FilmDTO {
    private Long id;
    private String imdb;
    private String nom;
    private String annee;
    private String rating;
    private String url;
    private String lieuTour;
    private String langue;
    private String resume;
    private Pays pays;
    private String genres;

    public static FilmDTO fromEntity(Film film) {
        FilmDTO dto = new FilmDTO();
        dto.setId(film.getId());
        dto.setImdb(film.getImdb());
        dto.setNom(film.getNom());
        dto.setAnnee(film.getAnnee());
        dto.setRating(film.getRating());
        dto.setUrl(film.getUrl());
        dto.setLieuTour(film.getLieuTour());
        dto.setLangue(film.getLangue());
        dto.setResume(film.getResume());
        dto.setPays(film.getPays());
        dto.setGenres(film.getGenres());
        return dto;
    }

    public Film toEntity() {
        Film film = new Film();
        film.setId(this.id);
        film.setImdb(this.imdb);
        film.setNom(this.nom);
        film.setAnnee(this.annee);
        film.setRating(this.rating);
        film.setUrl(this.url);
        film.setLieuTour(this.lieuTour);
        film.setLangue(this.langue);
        film.setResume(this.resume);
        film.setPays(this.pays);
        film.setGenres(this.getGenres());
        return film;
    }
}