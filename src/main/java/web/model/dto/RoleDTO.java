package web.model.dto;


import entities.business.role.Role;
import lombok.Getter;
import lombok.Setter;

public class RoleDTO {

    @Setter
    @Getter
    private Long id;

    @Setter
    @Getter
    private String roleName;
    private String filmId; // Use String for the foreign key ID
    private String acteurId; // Use String for the foreign key ID

    // Default constructor
    public RoleDTO() {}

    // Parameterized constructor
    public RoleDTO(Long id, String roleName, String filmId, String acteurId) {
        this.id = id;
        this.roleName = roleName;
        this.filmId = filmId;
        this.acteurId = acteurId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
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

    // Static method to convert a Role entity to a RoleDTO
    public static RoleDTO fromEntity(Role role) {
        return new RoleDTO(
                role.getId(),
                role.getRoleName(),
                role.getFilmId(),
                role.getActeurId()
        );
    }

    public Role toEntity() {
        Role role = new Role();
        role.setId(this.id);
        role.setRoleName(this.roleName);
        role.setFilmId(this.filmId);
        role.setActeurId(this.acteurId);
        return role;
    }
}