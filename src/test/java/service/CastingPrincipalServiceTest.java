package service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import entities.business.CastingPrincipal.CastingPrincipal;
import persistence.repository.ICastingPrincipalRepository;
import web.model.dto.CastingPrincipalDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class CastingPrincipalServiceTest {

    @InjectMocks
    private CastingPrincipalService castingPrincipalService;

    @Mock
    private ICastingPrincipalRepository castingPrincipalRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCastingByFilmAndActeur() {
        CastingPrincipal castingPrincipal = new CastingPrincipal();
        castingPrincipal.setFilmId("film1");
        castingPrincipal.setActeurId("acteur1");
        when(castingPrincipalRepository.findByFilmIdAndActeurId("film1", "acteur1"))
                .thenReturn(Optional.of(castingPrincipal));

        CastingPrincipalDTO result = castingPrincipalService.getCastingByFilmAndActeur("film1", "acteur1");
        assertNotNull(result);
        assertEquals("film1", result.getFilmId());
        assertEquals("acteur1", result.getActeurId());
    }

    @Test
    void testCreateCasting() {
        CastingPrincipalDTO castingPrincipalDTO = new CastingPrincipalDTO();
        castingPrincipalDTO.setFilmId("film1");
        castingPrincipalDTO.setActeurId("acteur1");

        CastingPrincipal castingPrincipal = new CastingPrincipal();
        castingPrincipal.setFilmId("film1");
        castingPrincipal.setActeurId("acteur1");

        when(castingPrincipalRepository.save(any(CastingPrincipal.class))).thenReturn(castingPrincipal);

        castingPrincipalService.createCasting(castingPrincipalDTO);
        verify(castingPrincipalRepository, times(1)).save(any(CastingPrincipal.class));
    }

    @Test
    void testUpdateCasting() {
        CastingPrincipalDTO castingPrincipalDTO = new CastingPrincipalDTO();
        castingPrincipalDTO.setFilmId("film1");
        castingPrincipalDTO.setActeurId("acteur1");

        CastingPrincipal castingPrincipal = new CastingPrincipal();
        castingPrincipal.setFilmId("film1");
        castingPrincipal.setActeurId("acteur1");

        when(castingPrincipalRepository.findById(1L)).thenReturn(Optional.of(castingPrincipal));
        when(castingPrincipalRepository.save(any(CastingPrincipal.class))).thenReturn(castingPrincipal);

        castingPrincipalService.updateCasting(1L, castingPrincipalDTO);
        verify(castingPrincipalRepository, times(1)).save(any(CastingPrincipal.class));
    }

    @Test
    void testDeleteCastingByFilmAndActeur() {
        when(castingPrincipalRepository.existsByFilmIdAndActeurId("film1", "acteur1")).thenReturn(true);

        castingPrincipalService.deleteCastingByFilmAndActeur("film1", "acteur1");
        verify(castingPrincipalRepository, times(1)).deleteByFilmIdAndActeurId("film1", "acteur1");
    }
}