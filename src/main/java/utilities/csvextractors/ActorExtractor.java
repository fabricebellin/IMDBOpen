package utilities.csvextractors;

import web.model.dto.ActeurDTO;
import web.model.dto.PersonneDTO;
import persistence.repository.IActeurRepository;
import persistence.repository.IPersonneRepository;
import service.ActeurService;
import com.opencsv.CSVReader;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

@Component
public class ActorExtractor {

    @Autowired
    private IActeurRepository acteurRepository;

    @Autowired
    private ActeurService acteurService;

    @Autowired
    private IPersonneRepository personneRepository;

    @Transactional
    public void extractActorsFromCSV(String filePath) {
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(filePath))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build()) {

            // Read the header row
            reader.readNext();

            String[] line;
            while ((line = reader.readNext()) != null) {
                try {
                    // Parse CSV line
                    String identite = line[1].isEmpty() || "N/A".equals(line[1]) ? null : line[1].trim();
                    String dateNaissanceStr = line[2].isEmpty() || "N/A".equals(line[2]) ? null : line[2].trim();
                    String lieuNaissance = line[3].isEmpty() || "N/A".equals(line[3]) ? null : line[3].trim();
                    String imdbId = line[0].isEmpty() || "N/A".equals(line[0]) ? null : line[0].trim();
                    String tailleStr = line[4].isEmpty() || "N/A".equals(line[4]) ? null : line[4].trim();
                    String url = line[5].isEmpty() || "N/A".equals(line[5]) ? null : line[5].trim();

                    // Check if the Personne already exists
                    Optional<PersonneDTO> optionalPersonneDTO = personneRepository.findByIdentiteAndDateNaissance(identite, dateNaissanceStr)
                            .map(PersonneDTO::fromEntity);

                    PersonneDTO personneDTO;
                    if (optionalPersonneDTO.isPresent()) {
                        // Personne exists, retrieve it
                        personneDTO = optionalPersonneDTO.get();
                    } else {
                        // Personne does not exist, create a new PersonneDTO
                        personneDTO = new PersonneDTO();
                        personneDTO.setIdentite(identite);
                        personneDTO.setDateNaissance(dateNaissanceStr);
                        personneDTO.setLieuNaissance(lieuNaissance);
                        personneDTO.setUrl(url);

                        try {
                            // Save the new Personne via the repository
                            personneRepository.save(personneDTO.toEntity());  // Save as entity
                        } catch (Exception e) {
                            System.err.println("Error saving Personne: " + identite + " - " + e.getMessage());
                            e.printStackTrace();
                            continue; // Skip to the next line
                        }
                    }

                    // Check if the Acteur already exists based on the Personne's ID
                    Optional<ActeurDTO> optionalActeurDTO = acteurRepository.findByPersonne(personneDTO.toEntity())
                            .map(ActeurDTO::fromEntity);

                    if (optionalActeurDTO.isPresent()) {
                        // Acteur already exists, skip it
                        System.out.println("Acteur with Personne ID " + personneDTO.getId() + " already exists. Skipping...");
                    } else {
                        // Acteur does not exist, create a new ActeurDTO
                        ActeurDTO acteurDTO = new ActeurDTO();
                        acteurDTO.setPersonne(personneDTO);  // Associate the PersonneDTO with the ActeurDTO
                        acteurDTO.setIdImdb(imdbId);
                        acteurDTO.setTaille(tailleStr);

                        try {
                            // Save the new Acteur using the service
                            acteurService.createActeur(acteurDTO);
                        } catch (Exception e) {
                            System.err.println("Error saving Acteur: " + identite + " - " + e.getMessage());
                            e.printStackTrace();
                        }
                    }

                } catch (Exception e) {
                    System.err.println("Error processing line for actor: " + (line.length > 1 ? line[1] : "Unknown") + " - " + e.getMessage());
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
