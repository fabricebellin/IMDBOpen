package web.controller;

import service.RealisateurService;
import web.model.dto.RealisateurDTO;
import web.model.generic.ApiResponse;
import exceptions.EntityNotFoundException;
import exceptions.InvalidDataException;
import exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/realisateurs")
public class RealisateurController {

    private final RealisateurService realisateurService;

    @Autowired
    public RealisateurController(RealisateurService realisateurService) {
        this.realisateurService = realisateurService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RealisateurDTO>>> getAllRealisateurs() {
        List<RealisateurDTO> realisateurs = realisateurService.findAll();
        ApiResponse<List<RealisateurDTO>> response = new ApiResponse<>(HttpStatus.OK.value(), "Realisateurs fetched successfully", realisateurs);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RealisateurDTO>> getRealisateurById(@PathVariable Long id) {
        try {
            RealisateurDTO realisateur = realisateurService.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Realisateur not found with ID: " + id));
            ApiResponse<RealisateurDTO> response = new ApiResponse<>(HttpStatus.OK.value(), "Realisateur fetched successfully", realisateur);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceException("Failed to fetch Realisateur: " + ex.getMessage());
        }
    }

    @GetMapping("/imdb/{idImdb}")
    public ResponseEntity<ApiResponse<RealisateurDTO>> getRealisateursByIdImdb(@PathVariable String idImdb) {
        // Call the service method to get the Optional<RealisateurDTO>
        Optional<RealisateurDTO> realisateurOpt = realisateurService.findByIdImdb(idImdb);

        if (realisateurOpt.isPresent()) {
            // If present, return the DTO wrapped in ApiResponse
            ApiResponse<RealisateurDTO> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Realisateur fetched successfully",
                    realisateurOpt.get()
            );
            return ResponseEntity.ok(response);
        } else {
            // If not present, return a 404 response with a suitable message
            ApiResponse<RealisateurDTO> response = new ApiResponse<>(
                    HttpStatus.NOT_FOUND.value(),
                    "Realisateur not found with IMDb ID: " + idImdb,
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


    @PostMapping
    public ResponseEntity<ApiResponse<RealisateurDTO>> createRealisateur(@RequestBody RealisateurDTO realisateurDTO) {
        try {
            RealisateurDTO createdRealisateur = realisateurService.save(realisateurDTO);
            ApiResponse<RealisateurDTO> response = new ApiResponse<>(HttpStatus.CREATED.value(), "Realisateur created successfully", createdRealisateur);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (InvalidDataException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceException("Failed to create Realisateur: " + ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RealisateurDTO>> updateRealisateur(@PathVariable Long id, @RequestBody RealisateurDTO realisateurDTO) {
        if (!id.equals(realisateurDTO.getId())) {
            throw new InvalidDataException("Realisateur ID in the request body does not match the ID in the URL");
        }

        try {
            RealisateurDTO updatedRealisateur = realisateurService.update(realisateurDTO);
            ApiResponse<RealisateurDTO> response = new ApiResponse<>(HttpStatus.OK.value(), "Realisateur updated successfully", updatedRealisateur);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceException("Failed to update Realisateur: " + ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRealisateur(@PathVariable Long id) {
        try {
            realisateurService.deleteById(id);
            ApiResponse<Void> response = new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "Realisateur deleted successfully", null);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceException("Failed to delete Realisateur: " + ex.getMessage());
        }
    }



}
