package web.controller;

import service.RoleService;
import web.model.dto.RoleDTO;
import exceptions.EntityNotFoundException;
import exceptions.InvalidDataException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RoleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(roleController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testFindRolesWithFilters_Success() throws Exception {
        // Arrange
        RoleDTO role1 = new RoleDTO(1L, "Role One", "mm165465", "fg456465");
        RoleDTO role2 = new RoleDTO(2L, "Role Two", "fq2516354", "s4565465");
        List<RoleDTO> roles = Arrays.asList(role1, role2);

        when(roleService.findRolesWithFilters("Role", 1L, 1L)).thenReturn(roles);

        // Act & Assert
        mockMvc.perform(get("/api/roles")
                        .param("roleName", "Role")
                        .param("filmId", "1")
                        .param("actorId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].roleName", is("Role One")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].roleName", is("Role Two")));

        verify(roleService, times(1)).findRolesWithFilters("Role", 1L, 1L);
    }

    @Test
    void testFindRolesWithFilters_NotFound() throws Exception {
        // Arrange
        when(roleService.findRolesWithFilters("Nonexistent", null, null)).thenThrow(new EntityNotFoundException("No roles found"));

        // Act & Assert
        mockMvc.perform(get("/api/roles")
                        .param("roleName", "Nonexistent"))
                .andExpect(status().isNotFound());

        verify(roleService, times(1)).findRolesWithFilters("Nonexistent", null, null);
    }

    @Test
    void testFindRolesByRoleName_Success() throws Exception {
        // Arrange
        RoleDTO role1 = new RoleDTO(1L, "Role One", "mm165465", "fg456465");

        List<RoleDTO> roles = Arrays.asList(role1);

        when(roleService.findRolesByRoleName("Role One")).thenReturn(roles);

        // Act & Assert
        mockMvc.perform(get("/api/roles/byRoleName")
                        .param("roleName", "Role One"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].roleName", is("Role One")));

        verify(roleService, times(1)).findRolesByRoleName("Role One");
    }

    @Test
    void testFindRolesByRoleName_NotFound() throws Exception {
        // Arrange
        when(roleService.findRolesByRoleName("Nonexistent")).thenThrow(new EntityNotFoundException("Role not found"));

        // Act & Assert
        mockMvc.perform(get("/api/roles/byRoleName")
                        .param("roleName", "Nonexistent"))
                .andExpect(status().isNotFound());

        verify(roleService, times(1)).findRolesByRoleName("Nonexistent");
    }

    @Test
    void testCreateRole_Success() throws Exception {
        // Arrange
        RoleDTO roleDTO = new RoleDTO(null, "New Role", "fd4564", "gf54654");
        RoleDTO createdRole = new RoleDTO(1L, "New Role", "fd4564", "gf54654");

        when(roleService.createRole(any(RoleDTO.class))).thenReturn(createdRole);

        // Act & Assert
        mockMvc.perform(post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.roleName", is("New Role")));

        verify(roleService, times(1)).createRole(any(RoleDTO.class));
    }

    @Test
    void testCreateRole_InvalidData() throws Exception {
        // Arrange
        RoleDTO roleDTO = new RoleDTO(null, "", "fd4564", "gf54654"); // Invalid data
        when(roleService.createRole(any(RoleDTO.class))).thenThrow(new InvalidDataException("Invalid role data"));

        // Act & Assert
        mockMvc.perform(post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleDTO)))
                .andExpect(status().isBadRequest());

        verify(roleService, times(1)).createRole(any(RoleDTO.class));
    }

    @Test
    void testUpdateRole_Success() throws Exception {
        // Arrange
        Long roleId = 1L;
        RoleDTO roleDTO = new RoleDTO(roleId, "Updated Role", "fd4564", "gf54654");
        when(roleService.updateRole(eq(roleId), any(RoleDTO.class))).thenReturn(roleDTO);

        // Act & Assert
        mockMvc.perform(put("/api/roles/{id}", roleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.roleName", is("Updated Role")));

        verify(roleService, times(1)).updateRole(eq(roleId), any(RoleDTO.class));
    }

    @Test
    void testUpdateRole_NotFound() throws Exception {
        // Arrange
        Long roleId = 1L;
        RoleDTO roleDTO = new RoleDTO(roleId, "Updated Role", "fd4564", "gf54654");
        when(roleService.updateRole(eq(roleId), any(RoleDTO.class))).thenThrow(new EntityNotFoundException("Role not found"));

        // Act & Assert
        mockMvc.perform(put("/api/roles/{id}", roleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleDTO)))
                .andExpect(status().isNotFound());

        verify(roleService, times(1)).updateRole(eq(roleId), any(RoleDTO.class));
    }

    @Test
    void testUpdateRole_InvalidData() throws Exception {
        // Arrange
        Long roleId = 1L;
        RoleDTO roleDTO = new RoleDTO(roleId, "", "", ""); // Invalid data
        when(roleService.updateRole(eq(roleId), any(RoleDTO.class))).thenThrow(new InvalidDataException("Invalid role data"));

        // Act & Assert
        mockMvc.perform(put("/api/roles/{id}", roleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleDTO)))
                .andExpect(status().isBadRequest());

        verify(roleService, times(1)).updateRole(eq(roleId), any(RoleDTO.class));
    }

    @Test
    void testDeleteRole_Success() throws Exception {
        // Arrange
        Long roleId = 1L;
        doNothing().when(roleService).deleteRole(roleId);

        // Act & Assert
        mockMvc.perform(delete("/api/roles/{id}", roleId))
                .andExpect(status().isNoContent());

        verify(roleService, times(1)).deleteRole(roleId);
    }

    @Test
    void testDeleteRole_NotFound() throws Exception {
        // Arrange
        Long roleId = 1L;
        doThrow(new EntityNotFoundException("Role not found")).when(roleService).deleteRole(roleId);

        // Act & Assert
        mockMvc.perform(delete("/api/roles/{id}", roleId))
                .andExpect(status().isNotFound());

        verify(roleService, times(1)).deleteRole(roleId);
    }
}
