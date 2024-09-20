package web.controller;

import web.model.dto.ActeurDTO;
import web.model.dto.PersonneDTO;
import web.model.generic.ApiResponse;
import exceptions.EntityNotFoundException;
import exceptions.InvalidDataException;
import service.ActeurService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ActeurControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ActeurService acteurService;

    @InjectMocks
    private ActeurController acteurController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(acteurController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateActeur_Success() throws Exception {
        // Arrange
        ActeurDTO acteurDTO = new ActeurDTO();
        acteurDTO.setId(1L);
        acteurDTO.setPersonne(new PersonneDTO());
        acteurDTO.getPersonne().setIdentite("Test Acteur");
        acteurDTO.getPersonne().setDateNaissance("1990-01-01");

        when(acteurService.createActeur(any(ActeurDTO.class))).thenReturn(acteurDTO);

        // Act & Assert
        mockMvc.perform(post("/api/acteurs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(acteurDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.message").value("Acteur created successfully"));

        verify(acteurService, times(1)).createActeur(any(ActeurDTO.class));
    }

    @Test
    void testCreateActeur_InvalidDate() throws Exception {
        // Arrange
        ActeurDTO acteurDTO = new ActeurDTO();
        acteurDTO.setPersonne(new PersonneDTO());
        acteurDTO.getPersonne().setIdentite("Test Acteur");
        acteurDTO.setDateNaissance("invalid-date");

        doThrow(new InvalidDataException("Invalid date format")).when(acteurService).createActeur(any(ActeurDTO.class));

        // Act & Assert
        mockMvc.perform(post("/api/acteurs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(acteurDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Invalid date format"));

        verify(acteurService, times(1)).createActeur(any(ActeurDTO.class));
    }

    @Test
    void testUpdateActeur_Success() throws Exception {
        // Arrange
        Long id = 1L;
        ActeurDTO acteurDTO = new ActeurDTO();
        acteurDTO.setId(id);
        acteurDTO.setPersonne(new PersonneDTO());
        acteurDTO.getPersonne().setIdentite("Updated Acteur");
        acteurDTO.getPersonne().setDateNaissance("1990-01-01");

        when(acteurService.updateActeur(eq(id), any(ActeurDTO.class))).thenReturn(acteurDTO);

        // Act & Assert
        mockMvc.perform(put("/api/acteurs/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(acteurDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Acteur updated successfully"));

        verify(acteurService, times(1)).updateActeur(eq(id), any(ActeurDTO.class));
    }

    @Test
    void testUpdateActeur_NotFound() throws Exception {
        // Arrange
        Long id = 1L;
        ActeurDTO acteurDTO = new ActeurDTO();
        acteurDTO.setPersonne(new PersonneDTO());
        acteurDTO.getPersonne().setIdentite("Updated Acteur");

        doThrow(new EntityNotFoundException("Acteur not found")).when(acteurService).updateActeur(eq(id), any(ActeurDTO.class));

        // Act & Assert
        mockMvc.perform(put("/api/acteurs/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(acteurDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Acteur not found"));

        verify(acteurService, times(1)).updateActeur(eq(id), any(ActeurDTO.class));
    }

    @Test
    void testDeleteActeur_Success() throws Exception {
        // Arrange
        Long id = 1L;
        doNothing().when(acteurService).deleteActeur(id);

        // Act & Assert
        mockMvc.perform(delete("/api/acteurs/{id}", id))
                .andExpect(status().isNoContent());

        verify(acteurService, times(1)).deleteActeur(id);
    }

    @Test
    void testDeleteActeur_NotFound() throws Exception {
        // Arrange
        Long id = 1L;
        doThrow(new EntityNotFoundException("Acteur not found")).when(acteurService).deleteActeur(id);

        // Act & Assert
        mockMvc.perform(delete("/api/acteurs/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Acteur not found"));

        verify(acteurService, times(1)).deleteActeur(id);
    }

    @Test
    void testSearchActeurs_Success() throws Exception {
        // Arrange
        List<ActeurDTO> acteurs = new ArrayList<>();
        ActeurDTO acteurDTO = new ActeurDTO();
        acteurDTO.setId(1L);
        acteurDTO.setPersonne(new PersonneDTO());
        acteurDTO.getPersonne().setIdentite("Test Acteur");
        acteurs.add(acteurDTO);

        when(acteurService.findActeursWithFiltersAndSorting(anyString(), anyString(), anyString())).thenReturn(acteurs);

        // Act & Assert
        mockMvc.perform(get("/api/acteurs/search")
                        .param("identite", "Test Acteur")
                        .param("dateNaissance", "1990-01-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Acteurs retrieved successfully"))
                .andExpect(jsonPath("$.data[0].personne.identite").value("Test Acteur"));

        verify(acteurService, times(1)).findActeursWithFiltersAndSorting(anyString(), anyString(), anyString());
    }

    @Test
    void testSearchActeurs_InvalidDate() throws Exception {
        // Arrange
        doThrow(new InvalidDataException("Invalid date format for search")).when(acteurService).findActeursWithFiltersAndSorting(anyString(), eq("invalid-date"), anyString());

        // Act & Assert
        mockMvc.perform(get("/api/acteurs/search")
                        .param("dateNaissance", "invalid-date"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Invalid date format for search"));

        verify(acteurService, times(1)).findActeursWithFiltersAndSorting(anyString(), eq("invalid-date"), anyString());
    }
}
