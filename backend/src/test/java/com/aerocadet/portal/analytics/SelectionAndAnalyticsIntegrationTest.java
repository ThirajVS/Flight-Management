package com.aerocadet.portal.analytics;

import java.time.Instant;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SelectionAndAnalyticsIntegrationTest {
    @Autowired MockMvc mockMvc; @Autowired ObjectMapper mapper; @Autowired UserAccountRepository users; @Autowired RoleRepository roles;

    @Test
    void scoresAssessmentSchedulesInterviewAndSecuresAnalytics() throws Exception {
        String candidate="selection-candidate@example.test";String candidateToken=register(candidate);
        MvcResult created=mockMvc.perform(post("/api/applications").header("Authorization","Bearer "+candidateToken).contentType(MediaType.APPLICATION_JSON).content("{\"programId\":4}"))
                .andExpect(status().isCreated()).andReturn();
        long appId=mapper.readTree(created.getResponse().getContentAsString()).get("id").asLong();
        mockMvc.perform(post("/api/applications/{id}/submit",appId).header("Authorization","Bearer "+candidateToken)).andExpect(status().isOk());
        mockMvc.perform(get("/api/assessments/questions").header("Authorization","Bearer "+candidateToken))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(10)).andExpect(jsonPath("$[0].correctOption").doesNotExist());
        String answers="{\"1\":\"B\",\"2\":\"C\",\"3\":\"C\",\"4\":\"A\",\"5\":\"B\",\"6\":\"D\",\"7\":\"C\",\"8\":\"A\",\"9\":\"B\",\"10\":\"B\"}";
        mockMvc.perform(post("/api/assessments/submit").header("Authorization","Bearer "+candidateToken).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"applicationId\":"+appId+",\"startedAt\":\""+Instant.now().minusSeconds(600)+"\",\"answers\":"+answers+"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.score").value(100)).andExpect(jsonPath("$.status").value("QUALIFIED"));

        String recruiterToken=promoteAndLogin("selection-recruiter@example.test",RoleName.ROLE_RECRUITER);
        mockMvc.perform(post("/api/interviews").header("Authorization","Bearer "+recruiterToken).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"applicationId\":"+appId+",\"scheduledAt\":\""+Instant.now().plusSeconds(86400)+"\",\"mode\":\"VIDEO\",\"location\":\"https://meet.example.invalid/demo\",\"interviewer\":\"Officer Demo\",\"remarks\":\"Synthetic interview\",\"status\":\"SCHEDULED\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("SCHEDULED"));
        mockMvc.perform(get("/api/recruiter/dashboard").header("Authorization","Bearer "+recruiterToken))
                .andExpect(status().isOk()).andExpect(jsonPath("$.upcomingInterviews").value(1));

        String adminToken=promoteAndLogin("selection-admin@example.test",RoleName.ROLE_ADMIN);
        mockMvc.perform(get("/api/analytics/summary").header("Authorization","Bearer "+adminToken))
                .andExpect(status().isOk()).andExpect(jsonPath("$.totalApplications").isNumber());
        mockMvc.perform(get("/api/audit-logs").header("Authorization","Bearer "+adminToken))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].action").isNotEmpty());
        mockMvc.perform(get("/api/analytics/summary").header("Authorization","Bearer "+candidateToken)).andExpect(status().isForbidden());
    }
    private String promoteAndLogin(String email,RoleName role) throws Exception {register(email);UserAccount user=users.findByEmailIgnoreCase(email).orElseThrow();user.addRole(roles.findByName(role).orElseThrow());users.saveAndFlush(user);return login(email);}
    private String register(String email)throws Exception{MvcResult r=mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content("{\"fullName\":\"Analytics Demo\",\"email\":\""+email+"\",\"phone\":\"+91 90000 00456\",\"password\":\"FlightReady!2026\",\"dateOfBirth\":\"2003-03-12\",\"nationality\":\"Indian\",\"city\":\"Pune\",\"state\":\"Maharashtra\",\"country\":\"India\"}" )).andExpect(status().isCreated()).andReturn();return mapper.readTree(r.getResponse().getContentAsString()).get("accessToken").asText();}
    private String login(String email)throws Exception{MvcResult r=mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content("{\"email\":\""+email+"\",\"password\":\"FlightReady!2026\"}" )).andExpect(status().isOk()).andReturn();return mapper.readTree(r.getResponse().getContentAsString()).get("accessToken").asText();}
}

