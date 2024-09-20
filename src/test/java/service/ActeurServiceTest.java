package service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import entities.business.personne.Acteur;
import entities.business.personne.Personne;
import persistence.repository.IActeurRepository;
import persistence.repository.IPersonneRepository;
import web.model.dto.ActeurDTO;
import web.model.dto.PersonneDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import exceptions.*;
import java.util.Optional;

public class ActeurServiceTest {

    @InjectMocks
    private ActeurService acteurService;

    @Mock
    private IActeurRepository acteurRepository;

    @Mock
    private IPersonneRepository personneRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateActeur_Success() {
        // Arrange
        PersonneDTO personneDTO = new PersonneDTO();
        personneDTO.setIdentite("John Doe");
        personneDTO.setDateNaissance("01-01-1995");
        personneDTO.setLieuNaissance("Paris, France");
        personneDTO.setUrl("http://example.com/johndoe");

        ActeurDTO acteurDTO = new ActeurDTO();
        acteurDTO.setPersonne(personneDTO);  // Set the PersonneDTO in ActeurDTO
        acteurDTO.setIdImdb("nm0000001");
        acteurDTO.setTaille("1.80m");

        // Mock ActeurRepository
        Acteur acteur = new Acteur();
        acteur.setId(1L);
        acteur.setIdImdb("nm0000001");
        acteur.setTaille("1.80m");
        acteur.setPersonne(new Personne("John Doe", "01-01-1995", "Paris, France", "http://example.com/johndoe"));

        when(acteurRepository.save(any(Acteur.class))).thenReturn(acteur);

        // Act
        ActeurDTO result = acteurService.createActeur(acteurDTO);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getPersonne().getIdentite());
        assertEquals("01-01-1995", result.getPersonne().getDateNaissance());
        assertEquals("1.80m", result.getTaille());
        assertEquals("nm0000001", result.getIdImdb());
        verify(acteurRepository, times(1)).save(any(Acteur.class));
    }

    @Test
    void testCreateActeur_InvalidData() {
        // Arrange
        ActeurDTO acteurDTO = new ActeurDTO(); // Missing required data

        // Act & Assert
        InvalidDataException thrown = assertThrows(InvalidDataException.class, () -> {
            acteurService.createActeur(acteurDTO);
        });
        assertEquals("Invalid acteur or personne data", thrown.getMessage());
        verify(acteurRepository, never()).save(any(Acteur.class));
    }

    @Test
    void testUpdateActeur_Success() {
        // Arrange
        Personne personne = new Personne();
        personne.setIdentite("John Doe");
        personne.setDateNaissance("1990-01-01");
        personne.setLieuNaissance("Paris, France");
        personne.setUrl("http://example.com/johndoe");

        Acteur acteur = new Acteur();
        acteur.setPersonne(personne);
        acteur.setId(1L);
        acteur.setIdImdb("nm0000001");
        acteur.setTaille("1.80m");

        ActeurDTO acteurDTO = new ActeurDTO();
        acteurDTO.setIdImdb("nm0000001");
        acteurDTO.setTaille("1.80m");

        // Mock repository behavior
        when(acteurRepository.findById(1L)).thenReturn(Optional.of(acteur));
        when(acteurRepository.save(any(Acteur.class))).thenReturn(acteur);

        // Act
        ActeurDTO result = acteurService.updateActeur(1L, acteurDTO);

        // Assert
        assertNotNull(result);
        assertEquals("1.80m", result.getTaille());
        assertEquals("nm0000001", result.getIdImdb());
        verify(acteurRepository, times(1)).findById(1L);
        verify(acteurRepository, times(1)).save(any(Acteur.class));
    }

    @Test
    void testUpdateActeur_ActeurNotFound() {
        // Arrange
        ActeurDTO acteurDTO = new ActeurDTO();
        acteurDTO.setTaille("1,80m");
        acteurDTO.setIdImdb("nm0000001");

        // Mock repository behavior
        when(acteurRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            acteurService.updateActeur(1L, acteurDTO);
        });
        assertEquals("Acteur not found with id: 1", thrown.getMessage());
        verify(acteurRepository, times(1)).findById(1L);
        verify(acteurRepository, never()).save(any(Acteur.class));
    }

    @Test
    void testDeleteActeur() {
        when(acteurRepository.existsById(1L)).thenReturn(true);

        acteurService.deleteActeur(1L);
        verify(acteurRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteActeur_EntityNotFoundException() {
        // Arrange
        when(acteurRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            acteurService.deleteActeur(1L);
        });
        assertEquals("Acteur not found with id: 1", thrown.getMessage());
        verify(acteurRepository, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteActeur_Exception() {
        // Arrange
        when(acteurRepository.existsById(1L)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(acteurRepository).deleteById(1L);

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            acteurService.deleteActeur(1L);
        });
        assertEquals("Database error", thrown.getMessage());
        verify(acteurRepository, times(1)).deleteById(1L);
    }
}
