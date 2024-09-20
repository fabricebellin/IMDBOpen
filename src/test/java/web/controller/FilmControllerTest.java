package web.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import exceptions.EntityNotFoundException;
import exceptions.InvalidDataException;
import service.FilmService;

import web.model.dto.FilmDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class FilmControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FilmService filmService;

    @InjectMocks
    private FilmController filmController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(filmController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testSearchFilms_Success() throws Exception {
        // Arrange
        FilmDTO filmDTO = new FilmDTO();
        filmDTO.setId(1L);
        filmDTO.setNom("Test Film");
        List<FilmDTO> films = Arrays.asList(filmDTO);
        when(filmService.findFilmsWithFiltersAndSorting(anyString(), anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(films);

        // Act & Assert
        mockMvc.perform(get("/api/films/search")
                        .param("nom", "Test")
                        .param("annee", "2022")
                        .param("rating", "PG-13")
                        .param("paysName", "France")
                        .param("genreName", "Comedy")
                        .param("sortBy", "name"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Films retrieved successfully"))
                .andExpect(jsonPath("$.data[0].nom").value("Test Film"));

        verify(filmService, times(1)).findFilmsWithFiltersAndSorting(anyString(), anyString(), anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void testSearchFilms_InvalidData() throws Exception {
        // Arrange
        doThrow(new InvalidDataException("Invalid data")).when(filmService).findFilmsWithFiltersAndSorting(anyString(), anyString(), anyString(), anyString(), anyString(), anyString());

        // Act & Assert
        mockMvc.perform(get("/api/films/search")
                        .param("nom", "Test"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Invalid data"));

        verify(filmService, times(1)).findFilmsWithFiltersAndSorting(anyString(), anyString(), anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void testGetFilmByImdb_Success() throws Exception {
        // Arrange
        String imdbId = "tt1234567";
        FilmDTO filmDTO = new FilmDTO();
        filmDTO.setId(1L);
        filmDTO.setNom("Test Film");
        when(filmService.findFilmByImdb(imdbId)).thenReturn(Optional.of(filmDTO));

        // Act & Assert
        mockMvc.perform(get("/api/films/imdb/{imdb}", imdbId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Film retrieved successfully"))
                .andExpect(jsonPath("$.data.nom").value("Test Film"));

        verify(filmService, times(1)).findFilmByImdb(imdbId);
    }

    @Test
    void testGetFilmByImdb_NotFound() throws Exception {
        // Arrange
        String imdbId = "tt1234567";
        when(filmService.findFilmByImdb(imdbId)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/films/imdb/{imdb}", imdbId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("No film found with IMDb ID: " + imdbId));

        verify(filmService, times(1)).findFilmByImdb(imdbId);
    }

    @Test
    void testGetFilmsByName_Success() throws Exception {
        // Arrange
        String nom = "Test Film";
        FilmDTO filmDTO = new FilmDTO();
        filmDTO.setId(1L);
        filmDTO.setNom(nom);
        List<FilmDTO> films = Collections.singletonList(filmDTO);
        when(filmService.findFilmsByName(nom)).thenReturn(films);

        // Act & Assert
        mockMvc.perform(get("/api/films/name/{nom}", nom))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Films retrieved successfully"))
                .andExpect(jsonPath("$.data[0].nom").value(nom));

        verify(filmService, times(1)).findFilmsByName(nom);
    }

    @Test
    void testGetFilmsByName_NotFound() throws Exception {
        // Arrange
        String nom = "Test Film";
        when(filmService.findFilmsByName(nom)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/films/name/{nom}", nom))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("No films found with name: " + nom));

        verify(filmService, times(1)).findFilmsByName(nom);
    }

    @Test
    void testCreateFilm_Success() throws Exception {
        // Arrange
        FilmDTO filmDTO = new FilmDTO();
        filmDTO.setId(1L);
        filmDTO.setNom("Test Film");
        when(filmService.createFilm(any(FilmDTO.class))).thenReturn(filmDTO);

        // Act & Assert
        mockMvc.perform(post("/api/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.message").value("Film created successfully"))
                .andExpect(jsonPath("$.data.nom").value("Test Film"));

        verify(filmService, times(1)).createFilm(any(FilmDTO.class));
    }

    @Test
    void testCreateFilm_InvalidData() throws Exception {
        // Arrange
        FilmDTO filmDTO = new FilmDTO();
        doThrow(new InvalidDataException("Invalid data")).when(filmService).createFilm(any(FilmDTO.class));

        // Act & Assert
        mockMvc.perform(post("/api/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Invalid data"));

        verify(filmService, times(1)).createFilm(any(FilmDTO.class));
    }

    @Test
    void testUpdateFilm_Success() throws Exception {
        // Arrange
        Long id = 1L;
        FilmDTO filmDTO = new FilmDTO();
        filmDTO.setId(id);
        filmDTO.setNom("Updated Film");
        when(filmService.updateFilm(eq(id), any(FilmDTO.class))).thenReturn(filmDTO);

        // Act & Assert
        mockMvc.perform(put("/api/films/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Film updated successfully"))
                .andExpect(jsonPath("$.data.nom").value("Updated Film"));

        verify(filmService, times(1)).updateFilm(eq(id), any(FilmDTO.class));
    }

    @Test
    void testUpdateFilm_NotFound() throws Exception {
        // Arrange
        Long id = 1L;
        FilmDTO filmDTO = new FilmDTO();
        filmDTO.setId(id);
        filmDTO.setNom("Updated Film");
        doThrow(new EntityNotFoundException("Film not found")).when(filmService).updateFilm(eq(id), any(FilmDTO.class));

        // Act & Assert
        mockMvc.perform(put("/api/films/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filmDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Film not found"));

        verify(filmService, times(1)).updateFilm(eq(id), any(FilmDTO.class));
    }

    @Test
    void testDeleteFilm_Success() throws Exception {
        // Arrange
        Long id = 1L;
        doNothing().when(filmService).deleteFilm(id);

        // Act & Assert
        mockMvc.perform(delete("/api/films/{id}", id))
                .andExpect(status().isNoContent());

        verify(filmService, times(1)).deleteFilm(id);
    }

    @Test
    void testDeleteFilm_NotFound() throws Exception {
        // Arrange
        Long id = 1L;
        doThrow(new EntityNotFoundException("Film not found")).when(filmService).deleteFilm(id);

        // Act & Assert
        mockMvc.perform(delete("/api/films/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Film not found"));

        verify(filmService, times(1)).deleteFilm(id);
    }
}
