package entities.business.role;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "film_id")
    private String filmId; // ID in the film table, referenced as a String

    @Column(name = "acteur_id")
    private String acteurId; // ID in the acteur table, referenced as a String

    @Setter
    @Getter
    @Column(name = "role_name")
    private String roleName;

    // Default constructor
    public Role() {}

    // Parameterized constructor
    public Role(String roleName, String filmId, String acteurId) {
        this.roleName = roleName;
        this.filmId = filmId;
        this.acteurId = acteurId;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilmId() {
        return filmId;
    }

    public void setFilmId(String filmId) {
        this.filmId = filmId;
    }

    public String getActeurId() {
        return acteurId;
    }

    public void setActeurId(String acteurId) {
        this.acteurId = acteurId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", roleName='" + roleName + '\'' +
                ", filmId='" + filmId + '\'' +
                ", acteurId='" + acteurId + '\'' +
                '}';
    }
}