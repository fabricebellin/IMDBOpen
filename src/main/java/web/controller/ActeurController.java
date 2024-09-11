package web.controller;

import exceptions.EntityNotFoundException;
import exceptions.InvalidDataException;
import service.ActeurService;
import web.model.dto.ActeurDTO;
import web.model.generic.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("api/acteurs")
public class ActeurController {

    private static final Logger logger = LoggerFactory.getLogger(ActeurController.class);

    @Autowired
    private ActeurService acteurService;

    // Create a new Acteur
    @PostMapping
    public ResponseEntity<ApiResponse<ActeurDTO>> createActeur(@RequestBody ActeurDTO acteurDTO) {
        try {
            ActeurDTO createdActeur = acteurService.createActeur(acteurDTO);
            ApiResponse<ActeurDTO> response = new ApiResponse<>(HttpStatus.CREATED.value(), "Acteur created successfully", createdActeur);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (InvalidDataException ex) {
            logger.error("Invalid data provided for acteur creation: {}", ex.getMessage(), ex);
            throw ex;  // Handled by the Global Exception Handler
        } catch (Exception ex) {
            logger.error("An error occurred while creating the acteur: {}", ex.getMessage(), ex);
            throw new RuntimeException("An error occurred while creating the acteur", ex);  // Handled by the Global Exception Handler
        }
    }

    // Update an existing Acteur
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ActeurDTO>> updateActeur(@PathVariable Long id, @RequestBody ActeurDTO acteurDTO) {
        try {
            ActeurDTO updatedActeur = acteurService.updateActeur(id, acteurDTO);
            ApiResponse<ActeurDTO> response = new ApiResponse<>(HttpStatus.OK.value(), "Acteur updated successfully", updatedActeur);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (EntityNotFoundException ex) {
            logger.warn("Acteur not found for update, id {}: {}", id, ex.getMessage());
            throw ex;  // Handled by the Global Exception Handler
        } catch (InvalidDataException ex) {
            logger.error("Invalid data provided for acteur update, id {}: {}", id, ex.getMessage(), ex);
            throw ex;  // Handled by the Global Exception Handler
        } catch (Exception ex) {
            logger.error("An error occurred while updating the acteur, id {}: {}", id, ex.getMessage(), ex);
            throw new RuntimeException("An error occurred while updating the acteur", ex);  // Handled by the Global Exception Handler
        }
    }

    // Delete an Acteur
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteActeur(@PathVariable Long id) {
        try {
            acteurService.deleteActeur(id);
            ApiResponse<Void> response = new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "Acteur deleted successfully", null);
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException ex) {
            logger.warn("Acteur not found for deletion, id {}: {}", id, ex.getMessage());
            throw ex;  // Handled by the Global Exception Handler
        } catch (Exception ex) {
            logger.error("An error occurred while deleting the acteur, id {}: {}", id, ex.getMessage(), ex);
            throw new RuntimeException("An error occurred while deleting the acteur", ex);  // Handled by the Global Exception Handler
        }
    }



    // Optional: If you have a search method with filters (for example, by name, date, etc.)
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ActeurDTO>>> searchActeurs(
            @RequestParam(required = false) String identite,
            @RequestParam(required = false) String dateNaissance,
            @RequestParam(required = false) String sortBy
    ) {
        try {
            List<ActeurDTO> acteurs = acteurService.findActeursWithFiltersAndSorting(identite, dateNaissance, sortBy);
            ApiResponse<List<ActeurDTO>> response = new ApiResponse<>(HttpStatus.OK.value(), "Acteurs retrieved successfully", acteurs);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (InvalidDataException ex) {
            logger.error("Invalid data provided for search: {}", ex.getMessage(), ex);
            throw ex;  // Handled by the Global Exception Handler
        } catch (Exception ex) {
            logger.error("An error occurred while searching for acteurs: {}", ex.getMessage(), ex);
            throw new RuntimeException("An error occurred while searching for acteurs", ex);  // Handled by the Global Exception Handler
        }
    }
}
