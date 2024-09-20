package service;

import static org.mockito.Mockito.*;


import entities.business.Film.Film;
import persistence.repository.IFilmRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class FilmServiceTest {

    @InjectMocks
    private FilmService filmService;

    @Mock
    private IFilmRepository filmRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        Film film = new Film();
        when(filmRepository.saveAndFlush(film)).thenReturn(film);

        filmService.save(film); // Adjusted to match the return type of the save method
        verify(filmRepository, times(1)).saveAndFlush(film);
    }
}