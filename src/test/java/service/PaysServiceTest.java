package service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import entities.business.pays.Pays;
import persistence.repository.IPaysRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class PaysServiceTest {

    @InjectMocks
    private PaysService paysService;

    @Mock
    private IPaysRepository paysRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById() {
        Pays pays = new Pays();
        pays.setId(1L);
        when(paysRepository.findById(1L)).thenReturn(Optional.of(pays));

        Optional<Pays> result = paysService.findById(1L);
        assertNotNull(result);
        assertEquals(1L, result.get().getId().longValue());
    }

    @Test
    void testSave() {
        Pays pays = new Pays();
        when(paysRepository.save(pays)).thenReturn(pays);

        Pays result = paysService.save(pays);
        assertNotNull(result);
        verify(paysRepository, times(1)).save(pays);
    }
}
