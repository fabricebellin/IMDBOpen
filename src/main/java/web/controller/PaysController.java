package web.controller;


import entities.business.pays.Pays;
import service.PaysService;
import web.model.dto.PaysDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pays")
public class PaysController {

    @Autowired
    private PaysService paysService;

    @GetMapping
    public List<PaysDTO> getAllPays() {
        return paysService.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaysDTO> getPaysById(@PathVariable Long id) {
        Optional<Pays> pays = paysService.findById(id);
        return pays.map(value -> ResponseEntity.ok(convertToDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public PaysDTO createPays(@RequestBody PaysDTO paysDTO) {
        Pays pays = convertToEntity(paysDTO);
        return convertToDTO(paysService.save(pays));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaysDTO> updatePays(@PathVariable Long id, @RequestBody PaysDTO paysDTO) {
        Optional<Pays> existingPays = paysService.findById(id);
        if (existingPays.isPresent()) {
            Pays pays = convertToEntity(paysDTO);
            pays.setId(id);
            return ResponseEntity.ok(convertToDTO(paysService.save(pays)));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePays(@PathVariable Long id) {
        if (paysService.findById(id).isPresent()) {
            paysService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private PaysDTO convertToDTO(Pays pays) {
        PaysDTO paysDTO = new PaysDTO();
        paysDTO.setId(pays.getId());
        paysDTO.setNom(pays.getNom());
        return paysDTO;
    }

    private Pays convertToEntity(PaysDTO paysDTO) {
        Pays pays = new Pays();
        pays.setNom(paysDTO.getNom());
        return pays;
    }
}