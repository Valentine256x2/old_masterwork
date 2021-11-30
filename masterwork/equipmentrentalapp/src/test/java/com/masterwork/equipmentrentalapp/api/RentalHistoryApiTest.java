package com.masterwork.equipmentrentalapp.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.masterwork.equipmentrentalapp.models.dto.EquipmentGetDto;
import com.masterwork.equipmentrentalapp.models.dto.RentalHistoryGetDto;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class RentalHistoryApiTest {

  private List<RentalHistoryGetDto> dummyData;
  private final ObjectMapper mapper = new ObjectMapper();
  private final String baseUrl = "/api/rental-history";

  @Autowired
  private MockMvc mockMvc;

  public RentalHistoryApiTest() {
    mapper.registerModule(new ParameterNamesModule());
    mapper.registerModule(new Jdk8Module());
    mapper.registerModule(new JavaTimeModule());
    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
  }

  @BeforeEach
  void setUp() {
    dummyData = Arrays.asList(
        new RentalHistoryGetDto(1L, 1L, Arrays.asList(3L), LocalDate.of(2021, 7, 4), LocalDate.of(2021, 7,5), LocalDate.of(2021, 7, 5), 500L),
        new RentalHistoryGetDto(2L, 2L, Arrays.asList(2L), LocalDate.of(2021, 7, 2), LocalDate.of(2021, 7,4), LocalDate.of(2021, 7, 4), 3000L)
    );

  }

  @Test
  void getAllRentalHistories() throws Exception {
    String expected = mapper.writeValueAsString(dummyData);
  
    String actual = mockMvc.perform(get(baseUrl))
                           .andExpect(status().isOk())
                           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                           .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
  
    assertEquals(mapper.readTree(expected), mapper.readTree(actual));
  }

  @Test
  void getRentalHistoryById() throws Exception {
    int id = 1;
  
    String expected = mapper.writeValueAsString(Collections.singletonList(dummyData.get(id - 1)));
  
  
    String actual = mockMvc.perform(get(baseUrl + "/records-by-user/" + id))
                           .andExpect(status().isOk())
                           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                           .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
  
    assertEquals(mapper.readTree(expected), mapper.readTree(actual));
  }

  @Test
  void getRentalHistoriesByUserId() throws Exception {
    int id = 2;
  
    String expected = mapper.writeValueAsString(dummyData.get(id - 1));
  
  
    String actual =
        mockMvc.perform(get(baseUrl + "/records/" + id))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
  
    assertEquals(mapper.readTree(expected), mapper.readTree(actual));
  }

  @Test
  void getRentalHistoriesByEquipmentId() throws Exception {
    int id = 2;
  
    String expected = mapper.writeValueAsString(Collections.singletonList(dummyData.get(id - 1)));
  
    String actual =
        mockMvc.perform(get(baseUrl + "/records-by-equipment/" + id))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
  
    assertEquals(mapper.readTree(expected), mapper.readTree(actual));
  }
}
