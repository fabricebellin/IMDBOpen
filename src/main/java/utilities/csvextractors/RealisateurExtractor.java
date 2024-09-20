package utilities.csvextractors;


import entities.business.personne.Personne;
import entities.business.personne.Realisateur;
import persistence.repository.IRealisateurRepository;
import persistence.repository.IPersonneRepository;
import com.opencsv.CSVReader;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class RealisateurExtractor {

    @Autowired
    private IRealisateurRepository realisateurRepository;

    @Autowired
    private IPersonneRepository personneRepository;

    @Transactional // Ensure all database operations within this method are transactional
    public void extractRealisateursFromCSV(String filePath) {
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(filePath))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build()) {

            String[] header = reader.readNext(); // Read the header row
            String[] line;

            while ((line = reader.readNext()) != null) {
                try {
                    // Parse CSV line
                    String url = line[4].isEmpty() || "N/A".equals(line[4]) ? null : line[4].trim();
                    String identite = line[1].isEmpty() || "N/A".equals(line[1]) ? null : line[1].trim();
                    String dateNaissanceStr = line[2].isEmpty() || "N/A".equals(line[2]) ? null : line[2].trim();
                    String lieuNaissance = line[3].isEmpty() || "N/A".equals(line[3]) ? null : line[3].trim();
                    String imdbId = line[0].isEmpty() || "N/A".equals(line[0]) ? null : line[0].trim();

                    // Check if the Personne already exists
                    Optional<Personne> optionalPersonne = personneRepository.findByIdentiteAndDateNaissance(identite, dateNaissanceStr);

                    Personne personne;
                    if (optionalPersonne.isPresent()) {
                        // Personne exists, retrieve it
                        personne = optionalPersonne.get();
                    } else {
                        // Personne does not exist, create a new one
                        personne = new Personne();
                        personne.setIdentite(identite);
                        personne.setDateNaissance(dateNaissanceStr); // Treat date as a plain string
                        personne.setLieuNaissance(lieuNaissance);
                        personne.setUrl(url);

                        try {
                            personne = personneRepository.save(personne); // Save and retrieve the saved Personne
                            if (personne.getId() == null) {
                                throw new IllegalStateException("Failed to save Personne: ID is null after save.");
                            }
                        } catch (Exception e) {
                            System.err.println("Error saving Personne: " + identite + " - " + e.getMessage());
                            e.printStackTrace();
                            continue; // Skip to the next line if saving Personne fails
                        }
                    }

                    // Use the new findByPersonne method to check if the Realisateur already exists
                    Optional<Realisateur> optionalRealisateur = realisateurRepository.findByPersonne(personne);

                    if (optionalRealisateur.isPresent()) {
                        // Realisateur already exists, skip it
                        System.out.println("Realisateur with Personne ID " + personne.getId() + " already exists. Skipping...");
                    } else {
                        // Realisateur does not exist, create a new one
                        Realisateur realisateur = new Realisateur();
                        realisateur.setPersonne(personne); // Ensure Realisateur has a valid Personne reference
                        realisateur.setIdImdb(imdbId);
                        realisateur.setCreatedDate(LocalDateTime.now());

                        try {
                            realisateurRepository.save(realisateur);
                            realisateurRepository.flush();// Save the new Realisateur to the database
                        } catch (Exception e) {
                            System.err.println("Error saving Realisateur: " + identite + " - " + e.getMessage());
                            e.printStackTrace();
                        }
                    }

                } catch (Exception e) {
                    System.err.println("Error processing line for realisateur: " + (line.length > 1 ? line[0] : "Unknown") + " - " + e.getMessage());
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
