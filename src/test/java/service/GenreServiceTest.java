package service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import entities.business.genre.Genre;
import persistence.repository.IGenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class GenreServiceTest {

    @InjectMocks
    private GenreService genreService;

    @Mock
    private IGenreRepository genreRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        Genre genre = new Genre();
        when(genreRepository.save(genre)).thenReturn(genre);

        Genre result = genreService.save(genre);
        assertNotNull(result);
        verify(genreRepository, times(1)).save(genre);
    }
}