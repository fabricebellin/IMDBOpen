package web.controller;

import entities.business.pays.Pays;
import service.PaysService;
import web.model.dto.PaysDTO;
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

class PaysControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PaysService paysService;

    @InjectMocks
    private PaysController paysController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(paysController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetAllPays_Success() throws Exception {
        // Arrange
        Pays pays1 = new Pays();
        pays1.setId(1L);
        pays1.setNom("France");

        Pays pays2 = new Pays();
        pays2.setId(2L);
        pays2.setNom("Germany");

        List<Pays> paysList = Arrays.asList(pays1, pays2);
        when(paysService.findAll()).thenReturn(paysList);

        // Act & Assert
        mockMvc.perform(get("/api/pays"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nom", is("France")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].nom", is("Germany")));

        verify(paysService, times(1)).findAll();
    }

    @Test
    void testGetPaysById_Success() throws Exception {
        // Arrange
        Long paysId = 1L;
        Pays pays = new Pays();
        pays.setId(paysId);
        pays.setNom("France");

        when(paysService.findById(paysId)).thenReturn(Optional.of(pays));

        // Act & Assert
        mockMvc.perform(get("/api/pays/{id}", paysId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nom", is("France")));

        verify(paysService, times(1)).findById(paysId);
    }

    @Test
    void testGetPaysById_NotFound() throws Exception {
        // Arrange
        Long paysId = 1L;
        when(paysService.findById(paysId)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/pays/{id}", paysId))
                .andExpect(status().isNotFound());

        verify(paysService, times(1)).findById(paysId);
    }

    @Test
    void testCreatePays_Success() throws Exception {
        // Arrange
        PaysDTO paysDTO = new PaysDTO();
        paysDTO.setNom("France");

        Pays pays = new Pays();
        pays.setId(1L);
        pays.setNom("France");

        when(paysService.save(any(Pays.class))).thenReturn(pays);

        // Act & Assert
        mockMvc.perform(post("/api/pays")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paysDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nom", is("France")));

        verify(paysService, times(1)).save(any(Pays.class));
    }

    @Test
    void testUpdatePays_Success() throws Exception {
        // Arrange
        Long paysId = 1L;
        PaysDTO paysDTO = new PaysDTO();
        paysDTO.setNom("Updated Pays");

        Pays existingPays = new Pays();
        existingPays.setId(paysId);
        existingPays.setNom("France");

        Pays updatedPays = new Pays();
        updatedPays.setId(paysId);
        updatedPays.setNom("Updated Pays");

        when(paysService.findById(paysId)).thenReturn(Optional.of(existingPays));
        when(paysService.save(any(Pays.class))).thenReturn(updatedPays);

        // Act & Assert
        mockMvc.perform(put("/api/pays/{id}", paysId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paysDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nom", is("Updated Pays")));

        verify(paysService, times(1)).findById(paysId);
        verify(paysService, times(1)).save(any(Pays.class));
    }

    @Test
    void testUpdatePays_NotFound() throws Exception {
        // Arrange
        Long paysId = 1L;
        PaysDTO paysDTO = new PaysDTO();
        paysDTO.setNom("Updated Pays");

        when(paysService.findById(paysId)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(put("/api/pays/{id}", paysId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paysDTO)))
                .andExpect(status().isNotFound());

        verify(paysService, times(1)).findById(paysId);
        verify(paysService, times(0)).save(any(Pays.class));
    }

    @Test
    void testDeletePays_Success() throws Exception {
        // Arrange
        Long paysId = 1L;
        when(paysService.findById(paysId)).thenReturn(Optional.of(new Pays()));

        // Act & Assert
        mockMvc.perform(delete("/api/pays/{id}", paysId))
                .andExpect(status().isNoContent());

        verify(paysService, times(1)).findById(paysId);
        verify(paysService, times(1)).deleteById(paysId);
    }

    @Test
    void testDeletePays_NotFound() throws Exception {
        // Arrange
        Long paysId = 1L;
        when(paysService.findById(paysId)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(delete("/api/pays/{id}", paysId))
                .andExpect(status().isNotFound());

        verify(paysService, times(1)).findById(paysId);
        verify(paysService, times(0)).deleteById(paysId);
    }
}
