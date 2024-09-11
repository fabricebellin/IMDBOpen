package utilities.csvextractors;

import com.opencsv.CSVReader;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileReader;
import java.io.IOException;

@Component
public class FilmRealisateurExtractor {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public void extractAndSaveFilmRealisateurFromCSV(String filePath) {
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(filePath))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build()) {

            String[] line;
            while ((line = reader.readNext()) != null) {
                String realisateurId = line[1].trim();
                String filmId = line[0].trim();

                try {
                    // Insert into film_realisateur table
                    String sql = "INSERT INTO film_realisateur (realisateur_id_imdb, film_imdb) VALUES (?, ?)";
                    jdbcTemplate.update(sql, realisateurId, filmId);
                } catch (Exception e) {
                    System.err.println("Error processing line for film_realisateur - Realisateur ID: " + realisateurId + ", Film ID: " + filmId + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (IOException | CsvValidationException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}