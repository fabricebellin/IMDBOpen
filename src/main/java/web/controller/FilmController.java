package web.controller;

import exceptions.EntityNotFoundException;
import exceptions.InvalidDataException;
import service.FilmService;

import web.model.dto.FilmDTO;
import web.model.generic.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/films")
public class FilmController {

    @Autowired
    private FilmService filmService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<FilmDTO>>> searchFilms(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String annee,
            @RequestParam(required = false) String rating,
            @RequestParam(required = false) String paysName,
            @RequestParam(required = false) String genreName,
            @RequestParam(required = false) String sortBy
    ) {
        try {
            List<FilmDTO> films = filmService.findFilmsWithFiltersAndSorting(nom, annee, rating, paysName, genreName, sortBy);
            ApiResponse<List<FilmDTO>> response = new ApiResponse<>(HttpStatus.OK.value(), "Films retrieved successfully", films);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (InvalidDataException ex) {
            throw ex;  // Handled by the Global Exception Handler
        } catch (Exception ex) {
            throw new RuntimeException("An error occurred while searching for films", ex);  // Handled by the Global Exception Handler
        }
    }

    @GetMapping("/imdb/{imdb}")
    public ResponseEntity<ApiResponse<FilmDTO>> getFilmByImdb(@PathVariable String imdb) {
        try {
            // Call the service method that returns Optional<FilmDTO>
            Optional<FilmDTO> filmOpt = filmService.findFilmByImdb(imdb);

            // Check if film is present in the Optional
            if (filmOpt.isPresent()) {
                FilmDTO film = filmOpt.get();
                ApiResponse<FilmDTO> response = new ApiResponse<>(HttpStatus.OK.value(), "Film retrieved successfully", film);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                // Return a 404 Not Found response if the film is not found
                ApiResponse<FilmDTO> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "No film found with IMDb ID: " + imdb, null);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

        } catch (Exception ex) {
            // Handle unexpected errors
            ApiResponse<FilmDTO> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred while retrieving the film by IMDb", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/name/{nom}")
    public ResponseEntity<ApiResponse<List<FilmDTO>>> getFilmsByName(@PathVariable String nom) {
        try {
            List<FilmDTO> films = filmService.findFilmsByName(nom);
            if (films.isEmpty()) {
                throw new EntityNotFoundException("No films found with name: " + nom);
            }
            ApiResponse<List<FilmDTO>> response = new ApiResponse<>(HttpStatus.OK.value(), "Films retrieved successfully", films);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (EntityNotFoundException ex) {
            throw ex;  // Handled by the Global Exception Handler
        } catch (Exception ex) {
            throw new RuntimeException("An error occurred while retrieving films by name", ex);  // Handled by the Global Exception Handler
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FilmDTO>> createFilm(@RequestBody FilmDTO filmDTO) {
        try {
            FilmDTO createdFilm = filmService.createFilm(filmDTO);
            ApiResponse<FilmDTO> response = new ApiResponse<>(HttpStatus.CREATED.value(), "Film created successfully", createdFilm);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (InvalidDataException ex) {
            throw ex;  // Handled by the Global Exception Handler
        } catch (Exception ex) {
            throw new RuntimeException("An error occurred while creating the film", ex);  // Handled by the Global Exception Handler
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FilmDTO>> updateFilm(@PathVariable Long id, @RequestBody FilmDTO filmDTO) {
        try {
            FilmDTO updatedFilm = filmService.updateFilm(id, filmDTO);
            ApiResponse<FilmDTO> response = new ApiResponse<>(HttpStatus.OK.value(), "Film updated successfully", updatedFilm);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (EntityNotFoundException ex) {
            throw ex;  // Handled by the Global Exception Handler
        } catch (InvalidDataException ex) {
            throw ex;  // Handled by the Global Exception Handler
        } catch (Exception ex) {
            throw new RuntimeException("An error occurred while updating the film", ex);  // Handled by the Global Exception Handler
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFilm(@PathVariable Long id) {
        try {
            filmService.deleteFilm(id);
            ApiResponse<Void> response = new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "Film deleted successfully", null);
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException ex) {
            throw ex;  // Handled by the Global Exception Handler
        } catch (Exception ex) {
            throw new RuntimeException("An error occurred while deleting the film", ex);  // Handled by the Global Exception Handler
        }
    }

    //@GetMapping("/by-actor/{actorId}")
    //public ResponseEntity<List<Film>> getFilmsByActor(@PathVariable Long actorId) {
    //    List<Film> films = filmService.findFilmsByActor(actorId);
    //    return ResponseEntity.ok(films);
    //}
}
