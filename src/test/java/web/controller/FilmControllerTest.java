package web.controller;

import exceptions.EntityNotFoundException;
import exceptions.InvalidDataException;
import service.FilmService;
import web.model.dto.FilmDTO;
import web.model.generic.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FilmControllerTest {

    @InjectMocks
    private FilmController filmController;

    @Mock
    private FilmService filmService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSearchFilms_Success() {
        // Arrange
        List<FilmDTO> expectedFilms = Arrays.asList(new FilmDTO(), new FilmDTO());
        when(filmService.findFilmsWithFiltersAndSorting(any(), any(), any(), any(), any(), any())).thenReturn(expectedFilms);

        // Act
        ResponseEntity<ApiResponse<List<FilmDTO>>> response = filmController.searchFilms(null, null, null, null, null, null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Films retrieved successfully", response.getBody().getMessage());
        assertEquals(expectedFilms.size(), response.getBody().getData().size());
    }

    @Test
    void testGetFilmByImdb_Found() {
        // Arrange
        String imdbId = "tt1234567";
        FilmDTO expectedFilm = new FilmDTO();
        when(filmService.findFilmByImdb(imdbId)).thenReturn(Optional.of(expectedFilm));

        // Act
        ResponseEntity<ApiResponse<FilmDTO>> response = filmController.getFilmByImdb(imdbId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedFilm, response.getBody().getData());
    }

    @Test
    void testGetFilmByImdb_NotFound() {
        // Arrange
        String imdbId = "tt1234567";
        when(filmService.findFilmByImdb(imdbId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<ApiResponse<FilmDTO>> response = filmController.getFilmByImdb(imdbId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No film found with IMDb ID: " + imdbId, response.getBody().getMessage());
    }

    @Test
    void testGetFilmsByName_Success() {
        // Arrange
        String filmName = "Inception";
        List<FilmDTO> expectedFilms = Arrays.asList(new FilmDTO(), new FilmDTO());
        when(filmService.findFilmsByName(filmName)).thenReturn(expectedFilms);

        // Act
        ResponseEntity<ApiResponse<List<FilmDTO>>> response = filmController.getFilmsByName(filmName);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedFilms.size(), response.getBody().getData().size());
    }

    @Test
    void testGetFilmsByName_NotFound() {
        // Arrange
        String filmName = "Unknown Film";
        when(filmService.findFilmsByName(filmName)).thenReturn(Arrays.asList());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> filmController.getFilmsByName(filmName));
    }

    @Test
    void testCreateFilm_Success() {
        // Arrange
        FilmDTO filmDTO = new FilmDTO();
        FilmDTO createdFilm = new FilmDTO();
        when(filmService.createFilm(filmDTO)).thenReturn(createdFilm);

        // Act
        ResponseEntity<ApiResponse<FilmDTO>> response = filmController.createFilm(filmDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdFilm, response.getBody().getData());
    }

    @Test
    void testUpdateFilm_Success() {
        // Arrange
        Long filmId = 1L;
        FilmDTO filmDTO = new FilmDTO();
        FilmDTO updatedFilm = new FilmDTO();
        when(filmService.updateFilm(filmId, filmDTO)).thenReturn(updatedFilm);

        // Act
        ResponseEntity<ApiResponse<FilmDTO>> response = filmController.updateFilm(filmId, filmDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedFilm, response.getBody().getData());
    }

    @Test
    void testDeleteFilm_Success() {
        // Arrange
        Long filmId = 1L;

        // Act
        ResponseEntity<ApiResponse<Void>> response = filmController.deleteFilm(filmId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    // Additional tests for exception handling can be added here...

    // Example for invalid data exception
    @Test
    void testCreateFilm_InvalidDataException() {
        // Arrange
        FilmDTO filmDTO = new FilmDTO();
        when(filmService.createFilm(filmDTO)).thenThrow(new InvalidDataException("Invalid data"));

        // Act & Assert
        assertThrows(InvalidDataException.class, () -> filmController.createFilm(filmDTO));
    }
}
