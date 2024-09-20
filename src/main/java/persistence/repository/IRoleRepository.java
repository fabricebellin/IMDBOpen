package persistence.repository;


import entities.business.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {

    /**
     * Find roles by the film's ID.
     *
     * @param filmId The ID of the film.
     * @return A list of roles associated with the given film ID.
     */
    @Query("SELECT r FROM Role r WHERE r.filmId = :filmId")
    List<Role> findByFilmId(@Param("filmId") String filmId);

    /**
     * Find roles by the actor's ID.
     *
     * @param actorId The ID of the actor.
     * @return A list of roles associated with the given actor ID.
     */
    @Query("SELECT r FROM Role r WHERE r.acteurId = :actorId")
    List<Role> findByActorId(@Param("actorId") String actorId);

    /**
     * Find roles by the role's name.
     *
     * @param roleName The name of the role.
     * @return A list of roles with the given role name.
     */
    @Query("SELECT r FROM Role r WHERE r.roleName = :roleName")
    List<Role> findByRoleName(@Param("roleName") String roleName);

    /**
     * Find roles by the film's ID and role's name.
     *
     * @param filmId The ID of the film.
     * @param roleName The name of the role.
     * @return A list of roles associated with the given film ID and role name.
     */
    @Query("SELECT r FROM Role r WHERE r.filmId = :filmId AND r.roleName = :roleName")
    List<Role> findByFilmIdAndRoleName(@Param("filmId") String filmId, @Param("roleName") String roleName);

    /**
     * Find roles by the actor's ID and role's name.
     *
     * @param actorId The ID of the actor.
     * @param roleName The name of the role.
     * @return A list of roles associated with the given actor ID and role name.
     */
    @Query("SELECT r FROM Role r WHERE r.acteurId = :actorId AND r.roleName = :roleName")
    List<Role> findByActorIdAndRoleName(@Param("actorId") String actorId, @Param("roleName") String roleName);

    /**
     * Find roles by the film's ID and actor's ID.
     *
     * @param filmId The ID of the film.
     * @param actorId The ID of the actor.
     * @return A list of roles associated with the given film ID and actor ID.
     */
    @Query("SELECT r FROM Role r WHERE r.filmId = :filmId AND r.acteurId = :actorId")
    List<Role> findByFilmIdAndActorId(@Param("filmId") String filmId, @Param("actorId") String actorId);

    /**
     * Find roles by the film's ID, actor's ID, and role's name.
     *
     * @param filmId The ID of the film.
     * @param actorId The ID of the actor.
     * @param roleName The name of the role.
     * @return A list of roles associated with the given film ID, actor ID, and role name.
     */


    /**
     * Find roles by the role's name with partial match.
     *
     * @param partialRoleName The partial name of the role.
     * @return A list of roles with names containing the given partial role name.
     */
    @Query("SELECT r FROM Role r WHERE LOWER(r.roleName) LIKE LOWER(CONCAT('%', :partialRoleName, '%'))")
    List<Role> findByRoleNameContaining(@Param("partialRoleName") String partialRoleName);
    @Query("SELECT r FROM Role r WHERE r.filmId = :filmId AND r.acteurId = :acteurId AND r.roleName = :roleName")
    Optional<Role> findRoleByFilmIdAndActeurIdAndRoleName(
            @Param("filmId") String filmId,
            @Param("acteurId") String acteurId,
            @Param("roleName") String roleName
    );
    boolean existsByFilmIdAndActeurIdAndRoleName(String filmId, String acteurId, String roleName);
}
