package web.controller;

import entities.business.genre.Genre;
import service.GenreService;
import web.model.dto.GenreDTO;
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
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GenreControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GenreService genreService;

    @InjectMocks
    private GenreController genreController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(genreController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetAllGenres_Success() throws Exception {
        // Arrange
        Genre genre1 = new Genre();
        genre1.setId(1L);
        genre1.setName("Comedy");

        Genre genre2 = new Genre();
        genre2.setId(2L);
        genre2.setName("Drama");

        List<Genre> genres = Arrays.asList(genre1, genre2);
        when(genreService.findAll()).thenReturn(genres);

        // Act & Assert
        mockMvc.perform(get("/api/genres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nom", is("Comedy")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].nom", is("Drama")));

        verify(genreService, times(1)).findAll();
    }

    @Test
    void testGetGenreById_Success() throws Exception {
        // Arrange
        Long genreId = 1L;
        Genre genre = new Genre();
        genre.setId(genreId);
        genre.setName("Comedy");

        when(genreService.findById(genreId)).thenReturn(Optional.of(genre));

        // Act & Assert
        mockMvc.perform(get("/api/genres/{id}", genreId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nom", is("Comedy")));

        verify(genreService, times(1)).findById(genreId);
    }

    @Test
    void testGetGenreById_NotFound() throws Exception {
        // Arrange
        Long genreId = 1L;
        when(genreService.findById(genreId)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/genres/{id}", genreId))
                .andExpect(status().isNotFound());

        verify(genreService, times(1)).findById(genreId);
    }

    @Test
    void testCreateGenre_Success() throws Exception {
        // Arrange
        GenreDTO genreDTO = new GenreDTO();
        genreDTO.setNom("Comedy");

        Genre genre = new Genre();
        genre.setId(1L);
        genre.setName("Comedy");

        when(genreService.save(any(Genre.class))).thenReturn(genre);

        // Act & Assert
        mockMvc.perform(post("/api/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(genreDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nom", is("Comedy")));

        verify(genreService, times(1)).save(any(Genre.class));
    }

    @Test
    void testUpdateGenre_Success() throws Exception {
        // Arrange
        Long genreId = 1L;
        GenreDTO genreDTO = new GenreDTO();
        genreDTO.setNom("Updated Genre");

        Genre existingGenre = new Genre();
        existingGenre.setId(genreId);
        existingGenre.setName("Comedy");

        Genre updatedGenre = new Genre();
        updatedGenre.setId(genreId);
        updatedGenre.setName("Updated Genre");

        when(genreService.findById(genreId)).thenReturn(Optional.of(existingGenre));
        when(genreService.save(any(Genre.class))).thenReturn(updatedGenre);

        // Act & Assert
        mockMvc.perform(put("/api/genres/{id}", genreId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(genreDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nom", is("Updated Genre")));

        verify(genreService, times(1)).findById(genreId);
        verify(genreService, times(1)).save(any(Genre.class));
    }

    @Test
    void testUpdateGenre_NotFound() throws Exception {
        // Arrange
        Long genreId = 1L;
        GenreDTO genreDTO = new GenreDTO();
        genreDTO.setNom("Updated Genre");

        when(genreService.findById(genreId)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(put("/api/genres/{id}", genreId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(genreDTO)))
                .andExpect(status().isNotFound());

        verify(genreService, times(1)).findById(genreId);
        verify(genreService, times(0)).save(any(Genre.class));
    }

    @Test
    void testDeleteGenre_Success() throws Exception {
        // Arrange
        Long genreId = 1L;
        when(genreService.findById(genreId)).thenReturn(Optional.of(new Genre()));

        // Act & Assert
        mockMvc.perform(delete("/api/genres/{id}", genreId))
                .andExpect(status().isNoContent());

        verify(genreService, times(1)).findById(genreId);
        verify(genreService, times(1)).deleteById(genreId);
    }

    @Test
    void testDeleteGenre_NotFound() throws Exception {
        // Arrange
        Long genreId = 1L;
        when(genreService.findById(genreId)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(delete("/api/genres/{id}", genreId))
                .andExpect(status().isNotFound());

        verify(genreService, times(1)).findById(genreId);
        verify(genreService, times(0)).deleteById(genreId);
    }
}
