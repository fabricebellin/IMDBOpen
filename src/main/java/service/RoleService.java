package service;


import entities.business.role.Role;
import exceptions.EntityNotFoundException;
import exceptions.InvalidDataException;
import persistence.repository.IRoleRepository;
import persistence.repository.IFilmRepository;
import persistence.repository.IActeurRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import web.model.dto.RoleDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private IFilmRepository filmRepository;

    @Autowired
    private IActeurRepository actorRepository;

    /**
     * Finds roles based on multiple filters.
     *
     * @param roleName the name of the role
     * @param filmId   the ID of the film associated with the role
     * @param actorId  the ID of the actor associated with the role
     * @return a list of RoleDTO objects
     */
    public List<RoleDTO> findRolesWithFilters(String roleName, Long filmId, Long actorId) {
        List<Role> roles = roleRepository.findAll();

        // Apply filters
        if (StringUtils.hasText(roleName)) {
            roles = roles.stream()
                    .filter(role -> role.getRoleName().toLowerCase().contains(roleName.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (roles.isEmpty()) {
            throw new EntityNotFoundException("No roles found matching the criteria");
        }

        return roles.stream().map(RoleDTO::fromEntity).collect(Collectors.toList());
    }
    /**
     * Finds roles by role name.
     *
     * @param roleName the name of the role
     * @return a list of RoleDTO objects
     */
    public List<RoleDTO> findRolesByRoleName(String roleName) {
        List<Role> roles = roleRepository.findByRoleName(roleName);
        if (roles.isEmpty()) {
            throw new EntityNotFoundException("No roles found with name: " + roleName);
        }
        return roles.stream().map(RoleDTO::fromEntity).collect(Collectors.toList());
    }

    /**
     * Creates a new role.
     *
     * @param roleDTO the RoleDTO object to create
     * @return the created RoleDTO object
     */
    public RoleDTO createRole(RoleDTO roleDTO) {
        if (roleDTO == null) {
            throw new InvalidDataException("Role data cannot be null");
        }
        Role role = roleDTO.toEntity();
        Role savedRole = roleRepository.save(role);
        return RoleDTO.fromEntity(savedRole);
    }

    /**
     * Updates an existing role.
     *
     * @param id      the ID of the role to update
     * @param roleDTO the RoleDTO object with updated data
     * @return the updated RoleDTO object
     */
    public RoleDTO updateRole(Long id, RoleDTO roleDTO) {
        if (roleDTO == null) {
            throw new InvalidDataException("Role data cannot be null");
        }
        Role existingRole = roleRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Role not found with ID: " + id));

        existingRole.setRoleName(roleDTO.getRoleName());


        Role updatedRole = roleRepository.save(existingRole);
        return RoleDTO.fromEntity(updatedRole);
    }

    /**
     * Deletes a role by ID.
     *
     * @param id the ID of the role to delete
     */
    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Role not found with ID: " + id));
        roleRepository.delete(role);
    }
}