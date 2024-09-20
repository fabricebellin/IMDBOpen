package web.controller;

import exceptions.EntityNotFoundException;
import exceptions.InvalidDataException;
import service.ActeurService;
import web.model.dto.ActeurDTO;
import web.model.dto.PersonneDTO;
import web.model.generic.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ActeurControllerTest {

    @InjectMocks
    private ActeurController acteurController;

    @Mock
    private ActeurService acteurService;

    private ActeurDTO acteurDTO;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PersonneDTO personneDTO = new PersonneDTO();
        personneDTO.setIdentite("Jean-Luc");
        personneDTO.setDateNaissance("1980-01-01");
        personneDTO.setLieuNaissance("Paris");
        personneDTO.setUrl("http://example.com");

        acteurDTO = new ActeurDTO();
        acteurDTO.setId(1L);
        acteurDTO.setIdImdb("nm1234567");
        acteurDTO.setTaille("180cm");
        acteurDTO.setPersonne(personneDTO);  // Setting PersonneDTO
        acteurDTO.setDateNaissance("1980-01-01");  // This might be redundant if already in PersonneDTO
        acteurDTO.setLieuNaissance("Paris");
        acteurDTO.setUrl("http://example.com");
        // Other fields...
    }

    @Test
    public void testCreateActeur_Success() {
        when(acteurService.createActeur(any(ActeurDTO.class))).thenReturn(acteurDTO);

        ResponseEntity<ApiResponse<ActeurDTO>> response = acteurController.createActeur(acteurDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(acteurDTO.getId(), response.getBody().getData().getId());
        verify(acteurService, times(1)).createActeur(any(ActeurDTO.class));
    }

    @Test
    public void testCreateActeur_InvalidData() {
        doThrow(new InvalidDataException("Invalid data")).when(acteurService).createActeur(any(ActeurDTO.class));

        InvalidDataException thrown = assertThrows(InvalidDataException.class, () -> {
            acteurController.createActeur(acteurDTO);
        });

        assertEquals("Invalid data", thrown.getMessage());
        verify(acteurService, times(1)).createActeur(any(ActeurDTO.class));
    }

    @Test
    public void testUpdateActeur_Success() {
        when(acteurService.updateActeur(eq(1L), any(ActeurDTO.class))).thenReturn(acteurDTO);

        ResponseEntity<ApiResponse<ActeurDTO>> response = acteurController.updateActeur(1L, acteurDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(acteurDTO.getId(), response.getBody().getData().getId());
        verify(acteurService, times(1)).updateActeur(eq(1L), any(ActeurDTO.class));
    }

    @Test
    public void testUpdateActeur_NotFound() {
        doThrow(new EntityNotFoundException("Acteur not found")).when(acteurService).updateActeur(eq(1L), any(ActeurDTO.class));

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            acteurController.updateActeur(1L, acteurDTO);
        });

        assertEquals("Acteur not found", thrown.getMessage());
        verify(acteurService, times(1)).updateActeur(eq(1L), any(ActeurDTO.class));
    }

    @Test
    public void testDeleteActeur_Success() {
        doNothing().when(acteurService).deleteActeur(1L);

        ResponseEntity<ApiResponse<Void>> response = acteurController.deleteActeur(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(acteurService, times(1)).deleteActeur(1L);
    }

    @Test
    public void testDeleteActeur_NotFound() {
        doThrow(new EntityNotFoundException("Acteur not found")).when(acteurService).deleteActeur(1L);

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            acteurController.deleteActeur(1L);
        });

        assertEquals("Acteur not found", thrown.getMessage());
        verify(acteurService, times(1)).deleteActeur(1L);
    }

    @Test
    public void testSearchActeurs_Success() {
        List<ActeurDTO> acteurs = Arrays.asList(acteurDTO);
        when(acteurService.findActeursWithFiltersAndSorting(anyString(), anyString(), anyString())).thenReturn(acteurs);

        ResponseEntity<ApiResponse<List<ActeurDTO>>> response = acteurController.searchActeurs("Jean", "1980-01-01", "name");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getData().size());
        verify(acteurService, times(1)).findActeursWithFiltersAndSorting(anyString(), anyString(), anyString());
    }


}
