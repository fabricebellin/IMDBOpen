package web.controller;

import service.RoleService;
import web.model.dto.RoleDTO;
import exceptions.EntityNotFoundException;
import exceptions.InvalidDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/roles")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * Finds roles with optional filters.
     *
     * @param roleName the name of the role (optional)
     * @param filmId   the ID of the film associated with the role (optional)
     * @param actorId  the ID of the actor associated with the role (optional)
     * @return a ResponseEntity with the list of RoleDTO objects
     */
    @GetMapping
    public ResponseEntity<List<RoleDTO>> findRolesWithFilters(
            @RequestParam(required = false) String roleName,
            @RequestParam(required = false) Long filmId,
            @RequestParam(required = false) Long actorId) {
        try {
            List<RoleDTO> roles = roleService.findRolesWithFilters(roleName, filmId, actorId);
            return ResponseEntity.ok(roles);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Finds roles by role name.
     *
     * @param roleName the name of the role
     * @return a ResponseEntity with the list of RoleDTO objects
     */
    @GetMapping("/byRoleName")
    public ResponseEntity<List<RoleDTO>> findRolesByRoleName(@RequestParam String roleName) {
        try {
            List<RoleDTO> roles = roleService.findRolesByRoleName(roleName);
            return ResponseEntity.ok(roles);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Creates a new role.
     *
     * @param roleDTO the RoleDTO object to create
     * @return a ResponseEntity with the created RoleDTO object
     */
    @PostMapping
    public ResponseEntity<RoleDTO> createRole(@RequestBody RoleDTO roleDTO) {
        try {
            RoleDTO createdRole = roleService.createRole(roleDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
        } catch (InvalidDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Updates an existing role.
     *
     * @param id      the ID of the role to update
     * @param roleDTO the RoleDTO object with updated data
     * @return a ResponseEntity with the updated RoleDTO object
     */
    @PutMapping("/{id}")
    public ResponseEntity<RoleDTO> updateRole(
            @PathVariable Long id,
            @RequestBody RoleDTO roleDTO) {
        try {
            RoleDTO updatedRole = roleService.updateRole(id, roleDTO);
            return ResponseEntity.ok(updatedRole);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (InvalidDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Deletes a role by ID.
     *
     * @param id the ID of the role to delete
     * @return a ResponseEntity with no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        try {
            roleService.deleteRole(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}