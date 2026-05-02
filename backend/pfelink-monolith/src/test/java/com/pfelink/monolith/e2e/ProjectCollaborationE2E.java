package com.pfelink.monolith.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pfelink.monolith.application.academic.command.project.create.CreateProjectCommand;
import com.pfelink.monolith.api.academic.project.dto.InviteFriendRequest;
import com.pfelink.monolith.domain.academic.entity.profile.StudentProfile;
import com.pfelink.monolith.domain.academic.repository.IStudentProfileRepository;
import com.pfelink.monolith.domain.auth.entity.User;
import com.pfelink.monolith.domain.auth.enums.AccountStatus;
import com.pfelink.monolith.domain.auth.enums.UserRole;
import com.pfelink.monolith.domain.auth.repository.IUserRepository;
import com.pfelink.monolith.shared.cqrs.Dispatcher;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ProjectCollaborationE2E {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IStudentProfileRepository studentProfileRepository;

    @Autowired
    private Dispatcher dispatcher;

    private User owner;
    private User invitee;
    private StudentProfile inviteeProfile;

    @BeforeEach
    void setUp() {
        // Create Owner
        owner = createTestUser("owner@me.com", "Owner User");
        createStudentProfile(owner);

        // Create Invitee
        invitee = createTestUser("invitee@me.com", "Invitee User");
        inviteeProfile = createStudentProfile(invitee);
    }

    @Test
    void projectInvitationFlow_ShouldWork_WhenAuthorized() throws Exception {
        // 1. Create Project for Owner
        UUID projectId = dispatcher.send(new CreateProjectCommand(
            owner.getId(), "PFE Link", "Security Hardening"
        )).getValue();

        // 2. Login as Owner to get cookies
        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"owner@me.com\", \"password\":\"Password123!\"}"))
                .andReturn();
        
        Cookie accessToken = loginResult.getResponse().getCookie("access_token");

        // 3. Invite Invitee
        InviteFriendRequest inviteReq = new InviteFriendRequest(projectId, inviteeProfile.getId());
        
        MvcResult inviteResult = mockMvc.perform(post("/api/v1/projects/invite")
                .cookie(accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inviteReq)))
                .andExpect(status().isOk())
                .andReturn();

        String invitationIdStr = inviteResult.getResponse().getContentAsString().replace("\"", "");
        UUID invitationId = UUID.fromString(invitationIdStr);

        // 4. Login as Invitee
        MvcResult inviteeLoginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"invitee@me.com\", \"password\":\"Password123!\"}"))
                .andReturn();
        
        Cookie inviteeAccessToken = inviteeLoginResult.getResponse().getCookie("access_token");

        // 5. Accept Invitation
        mockMvc.perform(post("/api/v1/projects/invitations/" + invitationId + "/accept")
                .cookie(inviteeAccessToken))
                .andExpect(status().isOk());

        // Verify invitee is now part of the project
        StudentProfile updatedInvitee = studentProfileRepository.findById(inviteeProfile.getId()).get();
        assertNotNull(updatedInvitee.getProject());
        assertEquals(projectId, updatedInvitee.getProject().getId());
    }

    private User createTestUser(String email, String name) {
        User user = new User();
        user.setEmail(email);
        user.setFullName(name);
        user.setPassword("$2a$10$8.UnVuG9HHgffUDAlk8qnOn5266Nkcln5X.f8rK6n6rFvL.vK.T/. "); // Password123!
        user.setRole(UserRole.STUDENT);
        user.setStatus(AccountStatus.ACTIVE);
        return userRepository.save(user);
    }

    private StudentProfile createStudentProfile(User user) {
        StudentProfile profile = new StudentProfile();
        profile.setUserId(user.getId());
        profile.setFullName(user.getFullName());
        return studentProfileRepository.save(profile);
    }
}
