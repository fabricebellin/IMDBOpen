package utilities.csvextractors;


import entities.business.role.Role;
import persistence.repository.IActeurRepository;
import persistence.repository.IFilmRepository;
import persistence.repository.IRoleRepository;
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
public class RoleExtractor {

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private IFilmRepository filmRepository;

    @Autowired
    private IActeurRepository acteurRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public void extractRolesFromCSV(String filePath) {
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(filePath))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build()) {

            // Read the header row
            reader.readNext();

            String[] line;
            while ((line = reader.readNext()) != null) {
                String filmId = line[0].isEmpty() ? null : line[0].trim();
                String acteurId = line[1].isEmpty() ? null : line[1].trim();
                String roleName = line[2].isEmpty() ? null : line[2].trim();

                try {
                    // Check if the Role already exists in the database
                    if (roleRepository.existsByFilmIdAndActeurIdAndRoleName(filmId, acteurId, roleName)) {
                        System.out.println("Role already exists - Film ID: " + filmId + ", Actor ID: " + acteurId + ", Role Name: " + roleName);
                    } else {
                        // Create a new Role entity
                        Role role = new Role();
                        role.setRoleName(roleName);
                        role.setFilmId(filmId);
                        role.setActeurId(acteurId);

                        // Save the new Role to the database
                        roleRepository.save(role);

                        // Insert into film_acteur table
                        String sql = "INSERT INTO film_acteur (acteur_id_imdb, film_imdb) VALUES (?, ?)";
                        jdbcTemplate.update(sql, acteurId, filmId);
                    }
                } catch (Exception e) {
                    System.err.println("Error processing line for role - Film ID: " + filmId + ", Actor ID: " + acteurId + ", Role Name: " + roleName + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading the CSV file: " + e.getMessage());
            e.printStackTrace();
        } catch (CsvValidationException e) {
            System.err.println("CSV validation error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}