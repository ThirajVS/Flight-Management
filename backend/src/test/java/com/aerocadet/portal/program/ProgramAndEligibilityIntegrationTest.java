package com.aerocadet.portal.program;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProgramAndEligibilityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listsAndSearchesFictionalProgramsWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/programs").param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(10))
                .andExpect(jsonPath("$.content.length()").value(5))
                .andExpect(jsonPath("$.content[0].fictionalDemo").value(true));

        mockMvc.perform(get("/api/programs").param("search", "International"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].code").value("IFTP-04"));
    }

    @Test
    void updatesProfileAndPersistsPendingEligibilityResult() throws Exception {
        String token = registerAndToken("program-eligible@example.test");

        mockMvc.perform(put("/api/candidates/profile")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(profileJson("78", "PENDING_VERIFICATION", true)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profileCompletion").value(100))
                .andExpect(jsonPath("$.physicsMarks").value(78));

        mockMvc.perform(post("/api/programs/1/eligibility")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.overallStatus").value("PENDING_VERIFICATION"))
                .andExpect(jsonPath("$.checks[0].status").value("PASSED"));

        mockMvc.perform(get("/api/programs/1/eligibility/latest")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.overallStatus").value("PENDING_VERIFICATION"));
    }

    @Test
    void explainsFailedEligibilityCriterion() throws Exception {
        String token = registerAndToken("program-ineligible@example.test");

        mockMvc.perform(put("/api/candidates/profile")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(profileJson("45", "VERIFIED", true)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/programs/1/eligibility")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.overallStatus").value("NOT_ELIGIBLE"))
                .andExpect(jsonPath("$.checks[?(@.criterion == 'Physics')].status").value("FAILED"));
    }

    private String registerAndToken(String email) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "fullName":"Ira Demo",
                                  "email":"%s",
                                  "phone":"+91 90000 00001",
                                  "password":"FlightReady!2026",
                                  "dateOfBirth":"2004-04-18",
                                  "nationality":"Indian",
                                  "city":"Bengaluru",
                                  "state":"Karnataka",
                                  "country":"India"
                                }
                                """.formatted(email)))
                .andExpect(status().isCreated())
                .andReturn();
        JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
        return response.get("accessToken").asText();
    }

    private String profileJson(String physicsMarks, String medicalStatus, boolean passport) {
        return """
                {
                  "phone":"+91 90000 00001",
                  "nationality":"Indian",
                  "city":"Bengaluru",
                  "state":"Karnataka",
                  "country":"India",
                  "tenthPercentage":82,
                  "twelfthPercentage":80,
                  "physicsMarks":%s,
                  "mathematicsMarks":84,
                  "englishMarks":76,
                  "graduationDetails":"B.Tech — Demo University",
                  "medicalStatus":"%s",
                  "flyingExperience":"Introductory demo flight only",
                  "totalFlightHours":2,
                  "previousAviationTraining":"None",
                  "englishProficiency":"ICAO_LEVEL_4_OR_HIGHER",
                  "passportAvailable":%s,
                  "preferredProgram":"Airline Cadet Pilot Program",
                  "preferredTrainingLocation":"Hyderabad",
                  "preferredAirline":"Demo Air",
                  "availableFrom":"2027-01-01"
                }
                """.formatted(physicsMarks, medicalStatus, passport);
    }
}

