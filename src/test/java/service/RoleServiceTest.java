package service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import entities.business.role.Role;
import persistence.repository.IRoleRepository;
import web.model.dto.RoleDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class RoleServiceTest {

    @InjectMocks
    private RoleService roleService;

    @Mock
    private IRoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateRole() {
        Role role = new Role();
        role.setId(1L);
        role.setRoleName("Admin");
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleName("Admin");

        RoleDTO result = roleService.createRole(roleDTO);
        assertNotNull(result);
        assertEquals("Admin", result.getRoleName());
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    void testUpdateRole() {
        Role role = new Role();
        role.setId(1L);
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleName("User");

        RoleDTO result = roleService.updateRole(1L, roleDTO);
        assertNotNull(result);
        assertEquals("User", result.getRoleName());
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    void testDeleteRole() {
        Role role = new Role();
        role.setId(1L);
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        roleService.deleteRole(1L);
        verify(roleRepository, times(1)).delete(role);
    }
}