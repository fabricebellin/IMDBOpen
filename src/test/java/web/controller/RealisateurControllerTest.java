package web.controller;

import service.RealisateurService;
import web.model.dto.RealisateurDTO;
import web.model.generic.ApiResponse;
import exceptions.EntityNotFoundException;
import exceptions.InvalidDataException;
import exceptions.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class RealisateurControllerTest {

    @InjectMocks
    private RealisateurController realisateurController;

    @Mock
    private RealisateurService realisateurService;

    private RealisateurDTO realisateurDTO;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        realisateurDTO = new RealisateurDTO();
        realisateurDTO.setId(1L);
        realisateurDTO.setIdImdb("imdb12345");
        // Other fields setup...
    }

    @Test
    public void testGetAllRealisateurs() {
        List<RealisateurDTO> realisateurs = Arrays.asList(realisateurDTO);
        when(realisateurService.findAll()).thenReturn(realisateurs);

        ResponseEntity<ApiResponse<List<RealisateurDTO>>> response = realisateurController.getAllRealisateurs();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getData().size());
        assertEquals(realisateurDTO.getId(), response.getBody().getData().get(0).getId());
        verify(realisateurService, times(1)).findAll();
    }

    @Test
    public void testGetRealisateurById_Success() {
        when(realisateurService.findById(1L)).thenReturn(Optional.of(realisateurDTO));

        ResponseEntity<ApiResponse<RealisateurDTO>> response = realisateurController.getRealisateurById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(realisateurDTO.getId(), response.getBody().getData().getId());
        verify(realisateurService, times(1)).findById(1L);
    }

    @Test
    public void testGetRealisateurById_NotFound() {
        when(realisateurService.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            realisateurController.getRealisateurById(1L);
        });

        assertEquals("Realisateur not found with ID: 1", thrown.getMessage());
        verify(realisateurService, times(1)).findById(1L);
    }

    @Test
    public void testGetRealisateursByIdImdb_Success() {
        when(realisateurService.findByIdImdb("imdb12345")).thenReturn(Optional.of(realisateurDTO));

        ResponseEntity<ApiResponse<RealisateurDTO>> response = realisateurController.getRealisateursByIdImdb("imdb12345");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(realisateurDTO.getId(), response.getBody().getData().getId());
        verify(realisateurService, times(1)).findByIdImdb("imdb12345");
    }

    @Test
    public void testGetRealisateursByIdImdb_NotFound() {
        when(realisateurService.findByIdImdb("imdb12345")).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse<RealisateurDTO>> response = realisateurController.getRealisateursByIdImdb("imdb12345");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody().getData());
        verify(realisateurService, times(1)).findByIdImdb("imdb12345");
    }

    @Test
    public void testCreateRealisateur_Success() {
        when(realisateurService.save(any(RealisateurDTO.class))).thenReturn(realisateurDTO);

        ResponseEntity<ApiResponse<RealisateurDTO>> response = realisateurController.createRealisateur(realisateurDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(realisateurDTO.getId(), response.getBody().getData().getId());
        verify(realisateurService, times(1)).save(any(RealisateurDTO.class));
    }

    @Test
    public void testCreateRealisateur_InvalidData() {
        doThrow(new InvalidDataException("Invalid data")).when(realisateurService).save(any(RealisateurDTO.class));

        InvalidDataException thrown = assertThrows(InvalidDataException.class, () -> {
            realisateurController.createRealisateur(realisateurDTO);
        });

        assertEquals("Invalid data", thrown.getMessage());
        verify(realisateurService, times(1)).save(any(RealisateurDTO.class));
    }

    @Test
    public void testUpdateRealisateur_Success() {
        when(realisateurService.update(any(RealisateurDTO.class))).thenReturn(realisateurDTO);

        ResponseEntity<ApiResponse<RealisateurDTO>> response = realisateurController.updateRealisateur(1L, realisateurDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(realisateurDTO.getId(), response.getBody().getData().getId());
        verify(realisateurService, times(1)).update(any(RealisateurDTO.class));
    }

    @Test
    public void testUpdateRealisateur_InvalidData() {
        realisateurDTO.setId(2L); // Mismatched ID to cause InvalidDataException

        InvalidDataException thrown = assertThrows(InvalidDataException.class, () -> {
            realisateurController.updateRealisateur(1L, realisateurDTO);
        });

        assertEquals("Realisateur ID in the request body does not match the ID in the URL", thrown.getMessage());
        verify(realisateurService, never()).update(any(RealisateurDTO.class));
    }

    @Test
    public void testDeleteRealisateur_Success() {
        ResponseEntity<ApiResponse<Void>> response = realisateurController.deleteRealisateur(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(realisateurService, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteRealisateur_NotFound() {
        doThrow(new EntityNotFoundException("Realisateur not found with ID: 1")).when(realisateurService).deleteById(1L);

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            realisateurController.deleteRealisateur(1L);
        });

        assertEquals("Realisateur not found with ID: 1", thrown.getMessage());
        verify(realisateurService, times(1)).deleteById(1L);
    }
}
