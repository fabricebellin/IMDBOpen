package service;


import entities.business.personne.Realisateur;
import persistence.repository.IRealisateurRepository;
import web.model.dto.RealisateurDTO;
import exceptions.EntityNotFoundException;
import exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RealisateurService {

    private final IRealisateurRepository realisateurRepository;

    @Autowired
    public RealisateurService(IRealisateurRepository realisateurRepository) {
        this.realisateurRepository = realisateurRepository;
    }

    public List<RealisateurDTO> findAll() {
        return realisateurRepository.findAll().stream()
                .map(RealisateurDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Optional<RealisateurDTO> findById(Long id) {
        return realisateurRepository.findById(id)
                .map(RealisateurDTO::fromEntity)
                .or(() -> {
                    throw new EntityNotFoundException("Realisateur not found with ID: " + id);
                });
    }

    public Optional<RealisateurDTO> findByIdImdb(String idImdb) {
        // Retrieve the Realisateur entity wrapped in an Optional
        Optional<Realisateur> realisateurOpt = realisateurRepository.findByIdImdb(idImdb);

        // Convert the Realisateur entity to RealisateurDTO if present
        return realisateurOpt.map(RealisateurDTO::fromEntity);
    }

    public RealisateurDTO save(RealisateurDTO realisateurDTO) {
        try {
            Realisateur realisateur = realisateurDTO.toEntity();
            return RealisateurDTO.fromEntity(realisateurRepository.save(realisateur));
        } catch (Exception e) {
            throw new ServiceException("Failed to save Realisateur: " + e.getMessage());
        }
    }

    public RealisateurDTO update(RealisateurDTO realisateurDTO) {
        if (realisateurRepository.existsById(realisateurDTO.getId())) {
            try {
                Realisateur realisateur = realisateurDTO.toEntity();
                return RealisateurDTO.fromEntity(realisateurRepository.save(realisateur));
            } catch (Exception e) {
                throw new ServiceException("Failed to update Realisateur: " + e.getMessage());
            }
        } else {
            throw new EntityNotFoundException("Realisateur not found with ID: " + realisateurDTO.getId());
        }
    }

    public void deleteById(Long id) {
        if (realisateurRepository.existsById(id)) {
            realisateurRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Realisateur not found with ID: " + id);
        }
    }


}
