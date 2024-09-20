package service;


import entities.business.Film.Film;
import entities.business.genre.Genre;
import entities.business.pays.Pays;
import exceptions.EntityNotFoundException;
import exceptions.InvalidDataException;
import persistence.repository.IFilmRepository;
import persistence.repository.IGenreRepository;
import persistence.repository.IPaysRepository;
import web.model.dto.FilmDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FilmService {

    @Autowired
    private IFilmRepository filmRepository;
    @Autowired
    private IGenreRepository genreRepository;
    @Autowired
    private IPaysRepository paysRepository;

    public List<FilmDTO> findFilmsWithFiltersAndSorting(String nom, String annee, String rating, String paysName, String genreName, String sortBy) {
        List<Film> films = filmRepository.findAll();

        if (StringUtils.hasText(nom)) {
            films = films.stream().filter(film -> film.getNom().toLowerCase().contains(nom.toLowerCase())).collect(Collectors.toList());
        }
        if (annee != null) {
            films = films.stream().filter(film -> film.getAnnee().contains(annee)).collect(Collectors.toList());
        }
        if (rating != null) {
            films = films.stream().filter(film -> film.getRating().contains(rating)).collect(Collectors.toList());
        }
        if (StringUtils.hasText(paysName)) {
            films = films.stream()
                    .filter(film -> film.getPays() != null &&
                            film.getPays().getName().equalsIgnoreCase(paysName))
                    .collect(Collectors.toList());
        }
        if (StringUtils.hasText(genreName)) {
            films = films.stream()
                    .filter(film -> {
                        // Split the genres string by the delimiter (e.g., comma)
                        String[] genresArray = film.getGenres().split(",\\s*"); // Adjust delimiter if needed
                        // Check if the genreName is present in the genresArray
                        return Arrays.stream(genresArray)
                                .anyMatch(genre -> genre.equalsIgnoreCase(genreName));
                    })
                    .collect(Collectors.toList());
        }

        if (StringUtils.hasText(sortBy)) {
            boolean ascending = !sortBy.startsWith("-");
            String sortField = ascending ? sortBy : sortBy.substring(1);

            films = films.stream()
                    .sorted((f1, f2) -> compareFilmsByField(f1, f2, sortField, ascending))
                    .collect(Collectors.toList());
        }

        if (films.isEmpty()) {
            throw new EntityNotFoundException("No films found matching the criteria");
        }

        return films.stream().map(FilmDTO::fromEntity).collect(Collectors.toList());
    }

    private int compareFilmsByField(Film f1, Film f2, String field, boolean ascending) {
        int comparison = 0;
        switch (field) {
            case "nom":
                comparison = f1.getNom().compareToIgnoreCase(f2.getNom());
                break;
            case "annee":
                comparison = CharSequence.compare(f1.getAnnee(), f2.getAnnee());
                break;
            case "rating":
                comparison = CharSequence.compare(f1.getRating(), f2.getRating());
                break;
            default:
                throw new IllegalArgumentException("Unsupported sorting field: " + field);
        }
        return ascending ? comparison : -comparison;
    }

    /**
     * Finds a film by its IMDb ID and converts it to FilmDTO.
     *
     * @param imdb the IMDb ID of the film
     * @return the FilmDTO object if found
     * @throws EntityNotFoundException if no film is found with the given IMDb ID
     */
    public Optional<FilmDTO> findFilmByImdb(String imdb) {
        // Find the film using the repository method, which returns an Optional<Film>
        Optional<Film> filmOptional = filmRepository.findByImdb(imdb);

        // Convert Film entity to FilmDTO if film is found, otherwise return Optional.empty()
        return filmOptional.map(FilmDTO::fromEntity);
    }

    public List<FilmDTO> findFilmsByName(String nom) {
        List<Film> films = filmRepository.findByNomContainingIgnoreCase(nom);
        if (films.isEmpty()) {
            throw new EntityNotFoundException("No films found with name containing: " + nom);
        }
        return films.stream().map(FilmDTO::fromEntity).collect(Collectors.toList());
    }

    public FilmDTO createFilm(FilmDTO filmDTO) {
        if (filmDTO == null) {
            throw new InvalidDataException("Film data cannot be null");
        }
        Film film = filmDTO.toEntity();
        Film savedFilm = filmRepository.save(film);
        return FilmDTO.fromEntity(savedFilm);
    }

    public FilmDTO updateFilm(Long id, FilmDTO filmDTO) {
        if (filmDTO == null) {
            throw new InvalidDataException("Film data cannot be null");
        }
        Film existingFilm = filmRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Film not found with ID: " + id));
        existingFilm.setNom(filmDTO.getNom());
        existingFilm.setAnnee(filmDTO.getAnnee());
        existingFilm.setRating(filmDTO.getRating());
        // Update other fields similarly

        Film updatedFilm = filmRepository.save(existingFilm);
        return FilmDTO.fromEntity(updatedFilm);
    }

    public void deleteFilm(Long id) {
        Film film = filmRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Film not found with ID: " + id));
        filmRepository.delete(film);
    }

    ////public List<Film> findFilmsByActor(Long actorId) {
    //    return filmRepository.findFilmsByActor(actorId);
    //}

    public Genre findOrCreateGenre(String genreName) {
        return genreRepository.findByName(genreName)
                .orElseGet(new java.util.function.Supplier<Genre>() {
                    @Override
                    public Genre get() {
                        return genreRepository.save(new Genre(null, genreName));
                    }
                });
    }


    @Transactional
    public Pays findOrCreatePays(String paysName) {
        return paysRepository.findByName(paysName)
                .orElseGet(new java.util.function.Supplier<Pays>() {
                    @Override
                    public Pays get() {
                        return paysRepository.save(new Pays(null, paysName));
                    }
                });
    }
    public void saveAll(List<Film> films){
        filmRepository.saveAll(films);
    }

    public void save(Film film) {
        filmRepository.saveAndFlush(film);
    }




}