package web.controller;

import service.RealisateurService;
import web.model.dto.RealisateurDTO;
import exceptions.EntityNotFoundException;
import exceptions.InvalidDataException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RealisateurControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RealisateurService realisateurService;

    @InjectMocks
    private RealisateurController realisateurController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(realisateurController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetAllRealisateurs_Success() throws Exception {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        RealisateurDTO realisateur1 = new RealisateurDTO(1L, "Realisateur One", "1980-01-01", "Paris, France", "http://example.com/one", "IDIMDB1", now, now);
        RealisateurDTO realisateur2 = new RealisateurDTO(2L, "Realisateur Two", "1975-05-05", "New York, USA", "http://example.com/two", "IDIMDB2", now, now);
        List<RealisateurDTO> realisateurList = Arrays.asList(realisateur1, realisateur2);
        when(realisateurService.findAll()).thenReturn(realisateurList);

        // Act & Assert
        mockMvc.perform(get("/api/realisateurs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode", is(200)))
                .andExpect(jsonPath("$.message", is("Realisateurs fetched successfully")))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].id", is(1)))
                .andExpect(jsonPath("$.data[0].identite", is("Realisateur One")))
                .andExpect(jsonPath("$.data[0].dateNaissance", is("1980-01-01")))
                .andExpect(jsonPath("$.data[0].lieuNaissance", is("Paris, France")))
                .andExpect(jsonPath("$.data[0].url", is("http://example.com/one")))
                .andExpect(jsonPath("$.data[0].idImdb", is("IDIMDB1")))
                .andExpect(jsonPath("$.data[1].id", is(2)))
                .andExpect(jsonPath("$.data[1].identite", is("Realisateur Two")))
                .andExpect(jsonPath("$.data[1].dateNaissance", is("1975-05-05")))
                .andExpect(jsonPath("$.data[1].lieuNaissance", is("New York, USA")))
                .andExpect(jsonPath("$.data[1].url", is("http://example.com/two")))
                .andExpect(jsonPath("$.data[1].idImdb", is("IDIMDB2")));

        verify(realisateurService, times(1)).findAll();
    }

    @Test
    void testGetRealisateurById_Success() throws Exception {
        // Arrange
        Long realisateurId = 1L;
        LocalDateTime now = LocalDateTime.now();
        RealisateurDTO realisateurDTO = new RealisateurDTO(realisateurId, "Realisateur One", "1980-01-01", "Paris, France", "http://example.com/one", "IDIMDB1", now, now);

        when(realisateurService.findById(realisateurId)).thenReturn(Optional.of(realisateurDTO));

        // Act & Assert
        mockMvc.perform(get("/api/realisateurs/{id}", realisateurId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode", is(200)))
                .andExpect(jsonPath("$.message", is("Realisateur fetched successfully")))
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.identite", is("Realisateur One")))
                .andExpect(jsonPath("$.data.dateNaissance", is("1980-01-01")))
                .andExpect(jsonPath("$.data.lieuNaissance", is("Paris, France")))
                .andExpect(jsonPath("$.data.url", is("http://example.com/one")))
                .andExpect(jsonPath("$.data.idImdb", is("IDIMDB1")));

        verify(realisateurService, times(1)).findById(realisateurId);
    }

    @Test
    void testGetRealisateurById_NotFound() throws Exception {
        // Arrange
        Long realisateurId = 1L;
        when(realisateurService.findById(realisateurId)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/realisateurs/{id}", realisateurId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Realisateur not found with ID: " + realisateurId)));

        verify(realisateurService, times(1)).findById(realisateurId);
    }

    @Test
    void testGetRealisateurByIdImdb_Success() throws Exception {
        // Arrange
        String imdbId = "IDIMDB1";
        LocalDateTime now = LocalDateTime.now();
        RealisateurDTO realisateurDTO = new RealisateurDTO(
                1L,
                "Realisateur One",
                "1980-01-01",
                "Paris, France",
                "http://example.com/one",
                imdbId,
                now,
                now
        );

        when(realisateurService.findByIdImdb(imdbId)).thenReturn(Optional.of(realisateurDTO));

        // Act & Assert
        mockMvc.perform(get("/api/realisateurs/imdb/{idImdb}", imdbId))
                .andExpect(status().isOk())
      //          .andExpect(jsonPath("$.statusCode", is(200)))
                .andExpect(jsonPath("$.message", is("Realisateur fetched successfully")))
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.identite", is("Realisateur One")))
                .andExpect(jsonPath("$.data.dateNaissance", is("1980-01-01")))
                .andExpect(jsonPath("$.data.lieuNaissance", is("Paris, France")))
                .andExpect(jsonPath("$.data.url", is("http://example.com/one")))
                .andExpect(jsonPath("$.data.idImdb", is(imdbId)));

        verify(realisateurService, times(1)).findByIdImdb(imdbId);
    }

    @Test
    void testCreateRealisateur_Success() throws Exception {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        RealisateurDTO realisateurDTO = new RealisateurDTO(null, "New Realisateur", "1985-08-15", "London, UK", "http://example.com/new", "IDIMDB3", now, now);
        RealisateurDTO createdRealisateur = new RealisateurDTO(1L, "New Realisateur", "1985-08-15", "London, UK", "http://example.com/new", "IDIMDB3", now, now);

        when(realisateurService.save(any(RealisateurDTO.class))).thenReturn(createdRealisateur);

        // Act & Assert
        mockMvc.perform(post("/api/realisateurs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(realisateurDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode", is(201)))
                .andExpect(jsonPath("$.message", is("Realisateur created successfully")))
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.identite", is("New Realisateur")))
                .andExpect(jsonPath("$.data.dateNaissance", is("1985-08-15")))
                .andExpect(jsonPath("$.data.lieuNaissance", is("London, UK")))
                .andExpect(jsonPath("$.data.url", is("http://example.com/new")))
                .andExpect(jsonPath("$.data.idImdb", is("IDIMDB3")));

        verify(realisateurService, times(1)).save(any(RealisateurDTO.class));
    }

    @Test
    void testCreateRealisateur_InvalidData() throws Exception {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        RealisateurDTO invalidRealisateurDTO = new RealisateurDTO(null, "", "invalid-date", "Invalid Place", "", "", now, now);

        doThrow(new InvalidDataException("Invalid data")).when(realisateurService).save(any(RealisateurDTO.class));

        // Act & Assert
        mockMvc.perform(post("/api/realisateurs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRealisateurDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Invalid data")));

        verify(realisateurService, times(1)).save(any(RealisateurDTO.class));
    }

    @Test
    void testUpdateRealisateur_Success() throws Exception {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        Long realisateurId = 1L;
        RealisateurDTO realisateurDTO = new RealisateurDTO(realisateurId, "Updated Realisateur", "1980-01-01", "Paris, France", "http://example.com/updated", "IDIMDB1", now, now);

        when(realisateurService.update(any(RealisateurDTO.class))).thenReturn(realisateurDTO);

        // Act & Assert
        mockMvc.perform(put("/api/realisateurs/{id}", realisateurId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(realisateurDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode", is(200)))
                .andExpect(jsonPath("$.message", is("Realisateur updated successfully")))
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.identite", is("Updated Realisateur")))
                .andExpect(jsonPath("$.data.dateNaissance", is("1980-01-01")))
                .andExpect(jsonPath("$.data.lieuNaissance", is("Paris, France")))
                .andExpect(jsonPath("$.data.url", is("http://example.com/updated")))
                .andExpect(jsonPath("$.data.idImdb", is("IDIMDB1")));

        verify(realisateurService, times(1)).update(any(RealisateurDTO.class));
    }

    @Test
    void testUpdateRealisateur_InvalidId() throws Exception {
        // Arrange
        Long realisateurId = 1L;
        LocalDateTime now = LocalDateTime.now();
        RealisateurDTO realisateurDTO = new RealisateurDTO(2L, "Updated Realisateur", "1980-01-01", "Paris, France", "http://example.com/updated", "IDIMDB1", now, now);

        // Act & Assert
        mockMvc.perform(put("/api/realisateurs/{id}", realisateurId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(realisateurDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Realisateur ID in the request body does not match the ID in the URL")));

        verify(realisateurService, times(0)).update(any(RealisateurDTO.class));
    }

    @Test
    void testDeleteRealisateur_Success() throws Exception {
        // Arrange
        Long realisateurId = 1L;
        doNothing().when(realisateurService).deleteById(realisateurId);

        // Act & Assert
        mockMvc.perform(delete("/api/realisateurs/{id}", realisateurId))
                .andExpect(status().isNoContent());

        verify(realisateurService, times(1)).deleteById(realisateurId);
    }

    @Test
    void testDeleteRealisateur_NotFound() throws Exception {
        // Arrange
        Long realisateurId = 1L;
        doThrow(new EntityNotFoundException("Realisateur not found with ID: " + realisateurId))
                .when(realisateurService).deleteById(realisateurId);

        // Act & Assert
        mockMvc.perform(delete("/api/realisateurs/{id}", realisateurId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Realisateur not found with ID: " + realisateurId)));

        verify(realisateurService, times(1)).deleteById(realisateurId);
    }
}
