package service;


import entities.business.CastingPrincipal.CastingPrincipal;
import exceptions.EntityNotFoundException;
import exceptions.InvalidDataException;
import exceptions.ServiceException;
import persistence.repository.ICastingPrincipalRepository;
import web.model.dto.CastingPrincipalDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CastingPrincipalService {

    private final ICastingPrincipalRepository castingPrincipalRepository;

    @Autowired
    public CastingPrincipalService(ICastingPrincipalRepository castingPrincipalRepository) {
        this.castingPrincipalRepository = castingPrincipalRepository;
    }

    public CastingPrincipalDTO getCastingByFilmAndActeur(String filmId, String acteurId) {
        try {
            if (filmId == null || acteurId == null) {
                throw new InvalidDataException("Film ID and Actor ID cannot be null");
            }
            CastingPrincipal casting = castingPrincipalRepository.findByFilmIdAndActeurId(filmId, acteurId)
                    .orElseThrow(() -> new EntityNotFoundException("Casting not found for the provided film and actor"));
            return CastingPrincipalDTO.fromEntity(casting);
        } catch (EntityNotFoundException | InvalidDataException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Failed to retrieve casting by film and actor");
        }
    }

    public void createCasting(CastingPrincipalDTO castingPrincipalDTO) {
        try {
            CastingPrincipal castingPrincipal = CastingPrincipalDTO.toEntity(castingPrincipalDTO);
            if (castingPrincipal.getFilmId() == null || castingPrincipal.getActeurId() == null) {
                throw new InvalidDataException("Film ID and Actor ID are required to create a casting");
            }
            castingPrincipalRepository.save(castingPrincipal);
        } catch (InvalidDataException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Failed to create casting");
        }
    }

    public void updateCasting(Long id, CastingPrincipalDTO updatedCastingDTO) {
        try {
            CastingPrincipal updatedCasting = CastingPrincipalDTO.toEntity(updatedCastingDTO);
            if (updatedCasting.getFilmId() == null || updatedCasting.getActeurId() == null) {
                throw new InvalidDataException("Film ID and Actor ID are required to update casting");
            }

            CastingPrincipal existingCasting = castingPrincipalRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Casting not found with id: " + id));

            existingCasting.setFilmId(updatedCasting.getFilmId());
            existingCasting.setActeurId(updatedCasting.getActeurId());


            castingPrincipalRepository.save(existingCasting);
        } catch (EntityNotFoundException | InvalidDataException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Failed to update casting");
        }
    }

    public void deleteCastingByFilmAndActeur(String filmId, String acteurId) {
        try {
            if (filmId == null || acteurId == null) {
                throw new InvalidDataException("Film ID and Actor ID cannot be null");
            }
            if (!castingPrincipalRepository.existsByFilmIdAndActeurId(filmId, acteurId)) {
                throw new EntityNotFoundException("Casting not found for the provided film and actor");
            }
            castingPrincipalRepository.deleteByFilmIdAndActeurId(filmId, acteurId);
        } catch (EntityNotFoundException | InvalidDataException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Failed to delete casting");
        }
    }
}