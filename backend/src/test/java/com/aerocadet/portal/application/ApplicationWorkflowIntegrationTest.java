package com.aerocadet.portal.application;

import com.aerocadet.portal.audit.AuditLogRepository;
import com.aerocadet.portal.user.RoleName;
import com.aerocadet.portal.user.RoleRepository;
import com.aerocadet.portal.user.UserAccount;
import com.aerocadet.portal.user.UserAccountRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApplicationWorkflowIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired UserAccountRepository userRepository;
    @Autowired RoleRepository roleRepository;
    @Autowired AuditLogRepository auditLogRepository;

    @Test
    void completesDraftUploadReviewAndStatusWorkflow() throws Exception {
        String candidateToken = registerAndToken("application-candidate@example.test");
        MvcResult created = mockMvc.perform(post("/api/applications")
                        .header("Authorization", "Bearer " + candidateToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"programId\":2}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("DRAFT"))
                .andReturn();
        long applicationId = objectMapper.readTree(created.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(put("/api/applications/{id}/draft", applicationId)
                        .header("Authorization", "Bearer " + candidateToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"step\":4,\"data\":{\"passportReady\":true}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentStep").value(4));

        mockMvc.perform(post("/api/applications/{id}/submit", applicationId)
                        .header("Authorization", "Bearer " + candidateToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUBMITTED"));

        MockMultipartFile file = new MockMultipartFile(
                "file", "dummy-passport.pdf", MediaType.APPLICATION_PDF_VALUE, "Synthetic demo document".getBytes());
        MvcResult uploaded = mockMvc.perform(multipart("/api/applications/{id}/documents", applicationId)
                        .file(file)
                        .param("type", "PASSPORT")
                        .header("Authorization", "Bearer " + candidateToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING_VERIFICATION"))
                .andReturn();
        long documentId = objectMapper.readTree(uploaded.getResponse().getContentAsString()).get("id").asLong();

        String reviewerToken = recruiterToken("application-recruiter@example.test");
        mockMvc.perform(put("/api/documents/{id}/review", documentId)
                        .header("Authorization", "Bearer " + reviewerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"VERIFIED\",\"reason\":\"Synthetic document verified\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("VERIFIED"));

        mockMvc.perform(put("/api/applications/{id}/status", applicationId)
                        .header("Authorization", "Bearer " + reviewerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"SHORTLISTED\",\"remarks\":\"Ready for demo assessment\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SHORTLISTED"));

        mockMvc.perform(get("/api/applications/{id}/status", applicationId)
                        .header("Authorization", "Bearer " + candidateToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.history.length()").value(3))
                .andExpect(jsonPath("$.selectionStages.length()").value(7));

        mockMvc.perform(get("/api/notifications/unread-count")
                        .header("Authorization", "Bearer " + candidateToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.unread").value(3));

        assertThat(auditLogRepository.count()).isGreaterThanOrEqualTo(6);
    }

    @Test
    void preventsDuplicateProgramApplication() throws Exception {
        String token = registerAndToken("application-duplicate@example.test");
        String body = "{\"programId\":3}";
        mockMvc.perform(post("/api/applications").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/applications").header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isConflict());
    }

    private String recruiterToken(String email) throws Exception {
        registerAndToken(email);
        UserAccount user = userRepository.findByEmailIgnoreCase(email).orElseThrow();
        user.addRole(roleRepository.findByName(RoleName.ROLE_RECRUITER).orElseThrow());
        userRepository.saveAndFlush(user);
        return loginToken(email);
    }

    private String registerAndToken(String email) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"fullName":"Workflow Demo","email":"%s","phone":"+91 90000 00123",
                                "password":"FlightReady!2026","dateOfBirth":"2003-03-12","nationality":"Indian",
                                "city":"Pune","state":"Maharashtra","country":"India"}
                                """.formatted(email)))
                .andExpect(status().isCreated()).andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString()).get("accessToken").asText();
    }

    private String loginToken(String email) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + email + "\",\"password\":\"FlightReady!2026\"}"))
                .andExpect(status().isOk()).andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString()).get("accessToken").asText();
    }
}

