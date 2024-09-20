package service;



import entities.business.personne.Acteur;
import entities.business.personne.Personne;
import exceptions.EntityNotFoundException;
import exceptions.InvalidDataException;

import web.model.dto.ActeurDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import persistence.repository.IActeurRepository;
import persistence.repository.IPersonneRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ActeurService {

    private final IActeurRepository acteurRepository;
    private final IPersonneRepository personneRepository;

    @Autowired
    public ActeurService(IActeurRepository acteurRepository, IPersonneRepository personneRepository) {
        this.acteurRepository = acteurRepository;
        this.personneRepository = personneRepository;
    }

    // Convert Entity to DTO
    private ActeurDTO convertToDTO(Acteur acteur) {
        return ActeurDTO.fromEntity(acteur);
    }

    // Convert DTO to Entity
    private Acteur convertToEntity(ActeurDTO acteurDTO) {
        return acteurDTO.toEntity();
    }

    // Method to create a new acteur along with Personne
    @Transactional
    public ActeurDTO createActeur(ActeurDTO acteurDTO) {
        if (acteurDTO == null || acteurDTO.getPersonne() == null) {
            throw new InvalidDataException("Invalid acteur or personne data");
        }

        // Validate date of birth
        if (!acteurDTO.isValidDateNaissance()) {
            throw new InvalidDataException("Invalid date of birth format");
        }

        // Check if Personne exists based on identite
        Optional<Personne> existingPersonneOpt = personneRepository.findByIdentite(acteurDTO.getPersonne().getIdentite());
        Personne personne;

        if (existingPersonneOpt.isPresent()) {
            // Personne already exists
            personne = existingPersonneOpt.get();
        } else {
            // Create new Personne entity
            personne = acteurDTO.getPersonne().toEntity();
            personneRepository.save(personne);
        }

        // Create Acteur entity and associate with Personne
        Acteur acteur = acteurDTO.toEntity();
        acteur.setPersonne(personne); // Set the associated Personne
        acteurRepository.save(acteur);

        return ActeurDTO.fromEntity(acteur);
    }

    // Method to update an existing acteur
    @Transactional
    public ActeurDTO updateActeur(Long id, ActeurDTO acteurDTO) {
        Optional<Acteur> existingActeurOpt = acteurRepository.findById(id);
        if (!existingActeurOpt.isPresent()) {
            throw new EntityNotFoundException("Acteur not found with id: " + id);
        }

        // Validate date of birth
        if (acteurDTO.getPersonne() != null && !acteurDTO.isValidDateNaissance()) {
            throw new InvalidDataException("Invalid date of birth format");
        }

        Acteur existingActeur = existingActeurOpt.get();
        existingActeur.setTaille(acteurDTO.getTaille());
        existingActeur.setIdImdb(acteurDTO.getIdImdb());

        // Update Personne if required
        if (acteurDTO.getPersonne() != null) {
            Personne updatedPersonne = acteurDTO.getPersonne().toEntity();
            existingActeur.setPersonne(updatedPersonne);
        }

        acteurRepository.save(existingActeur);
        return ActeurDTO.fromEntity(existingActeur);
    }

    // Method to delete an acteur
    @Transactional
    public void deleteActeur(Long id) {
        if (!acteurRepository.existsById(id)) {
            throw new EntityNotFoundException("Acteur not found with id: " + id);
        }
        acteurRepository.deleteById(id);
    }

    // Method to search and sort acteurs
    public List<ActeurDTO> findActeursWithFiltersAndSorting(String identite, String dateNaissance, String sortBy) {
        // Build sort object
        Sort sort = Sort.by(Sort.Direction.ASC, sortBy != null ? sortBy : "id");

        // Search and filter by identite and dateNaissance
        List<Acteur> acteurs = acteurRepository.findAllWithFilters(identite, dateNaissance, PageRequest.of(0, Integer.MAX_VALUE, sort));

        return acteurs.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}
