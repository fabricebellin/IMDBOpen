package service;


import entities.business.pays.Pays;
import persistence.repository.IPaysRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaysService {

    @Autowired
    private IPaysRepository paysRepository;

    public Pays findOrCreatePays(String paysName) {
        return paysRepository.findByName(paysName)
                .orElseGet(() -> paysRepository.save(new Pays(null, paysName)));
    }

    public List<Pays> findAll() {
        return paysRepository.findAll();
    }

    public Optional<Pays> findById(Long id) {
        return paysRepository.findById(id);
    }

    public Pays save(Pays pays) {
        return paysRepository.save(pays);
    }

    public void deleteById(Long id) {
        paysRepository.deleteById(id);
    }
}