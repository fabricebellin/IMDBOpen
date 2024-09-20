package entities.business.CastingPrincipal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "casting_principal")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CastingPrincipal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_casting")
    private Long idCasting;

    @Column(name = "film_id")
    private String filmId;

    @Column(name = "acteur_id")
    private String acteurId;

}