package service;


import entities.business.genre.Genre;
import persistence.repository.IGenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {

    @Autowired
    private IGenreRepository genreRepository;

    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    public Optional<Genre> findById(Long id) {
        return genreRepository.findById(id);
    }

    public Genre save(Genre genre) {
        return genreRepository.save(genre);
    }

    public void deleteById(Long id) {
        genreRepository.deleteById(id);
    }

    public Genre findOrCreateGenre(String genreName) {
        Optional<Genre> genreOptional = Optional.ofNullable(genreRepository.findByName(genreName));
        if (genreOptional.isPresent()) {
            return genreOptional.get();
        } else {
            Genre newGenre = new Genre();
            newGenre.setName(genreName);
            return genreRepository.save(newGenre);
        }
    }
}