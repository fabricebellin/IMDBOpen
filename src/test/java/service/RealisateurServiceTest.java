package service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import entities.business.personne.Personne;
import entities.business.personne.Realisateur;
import persistence.repository.IPersonneRepository;
import persistence.repository.IRealisateurRepository;
import web.model.dto.RealisateurDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import exceptions.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RealisateurServiceTest {

    @InjectMocks
    private RealisateurService realisateurService;

    @Mock
    private IRealisateurRepository realisateurRepository;

    @Mock
    private IPersonneRepository personneRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Realisateur realisateur = new Realisateur();
        when(realisateurRepository.findAll()).thenReturn(Stream.of(realisateur).collect(Collectors.toList()));

        List<RealisateurDTO> result = realisateurService.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(realisateurRepository, times(1)).findAll();
    }




    @Test
    void testUpdate_Success() {
        // Arrange
        Personne personne = new Personne();
        personne.setIdentite("John Doe");
        personne.setDateNaissance("1990-01-01");
        personne.setLieuNaissance("Paris, France");
        personne.setUrl("http://example.com/johndoe");

        Realisateur existingRealisateur = new Realisateur();
        existingRealisateur.setId(1L);
        existingRealisateur.setIdImdb("nm0000001");
        existingRealisateur.setPersonne(personne);

        RealisateurDTO realisateurDTO = new RealisateurDTO();
        realisateurDTO.setId(1L);
        realisateurDTO.setIdentite("Updated Identite");
        realisateurDTO.setIdImdb("nm0000001");
        // Add other necessary fields if required

        when(realisateurRepository.existsById(1L)).thenReturn(true);
        when(realisateurRepository.save(any(Realisateur.class))).thenReturn(existingRealisateur);

        // Act
        RealisateurDTO result = realisateurService.update(realisateurDTO);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getIdentite());
        assertEquals("nm0000001", result.getIdImdb());
        verify(realisateurRepository, times(1)).save(any(Realisateur.class));
    }

    @Test
    void testUpdate_NotFound() {
        // Arrange
        RealisateurDTO realisateurDTO = new RealisateurDTO();
        realisateurDTO.setId(1L);
        realisateurDTO.setIdentite("Updated Identite");
        realisateurDTO.setIdImdb("nm0000001");

        when(realisateurRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            realisateurService.update(realisateurDTO);
        });
        assertEquals("Realisateur not found with ID: 1", thrown.getMessage());
        verify(realisateurRepository, times(0)).save(any(Realisateur.class));
    }

    @Test
    void testUpdate_Failure() {
        // Arrange
        Personne personne = new Personne();
        personne.setIdentite("John Doe");
        personne.setDateNaissance("1990-01-01");
        personne.setLieuNaissance("Paris, France");
        personne.setUrl("http://example.com/johndoe");

        Realisateur realisateur = new Realisateur();
        realisateur.setId(1L);
        realisateur.setIdImdb("nm0000001");
        realisateur.setPersonne(personne);

        RealisateurDTO realisateurDTO = new RealisateurDTO();
        realisateurDTO.setId(1L);
        realisateurDTO.setIdentite("Updated Identite");
        realisateurDTO.setIdImdb("nm0000001");

        when(realisateurRepository.existsById(1L)).thenReturn(true);
        when(realisateurRepository.save(any(Realisateur.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        ServiceException thrown = assertThrows(ServiceException.class, () -> {
            realisateurService.update(realisateurDTO);
        });
        assertEquals("Failed to update Realisateur: Database error", thrown.getMessage());
        verify(realisateurRepository, times(1)).save(any(Realisateur.class));
    }




    @Test
    void testDeleteById() {
        when(realisateurRepository.existsById(1L)).thenReturn(true);

        realisateurService.deleteById(1L);
        verify(realisateurRepository, times(1)).deleteById(1L);
    }
}