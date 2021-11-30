package com.masterwork.equipmentrentalapp.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.masterwork.equipmentrentalapp.models.Category;
import com.masterwork.equipmentrentalapp.models.dto.BookingGetDto;
import com.masterwork.equipmentrentalapp.models.dto.EquipmentGetDto;
import com.masterwork.equipmentrentalapp.models.dto.EquipmentPutDto;
import com.masterwork.equipmentrentalapp.models.dto.UserGetDto;
import com.masterwork.equipmentrentalapp.models.dto.UserPostDto;
import com.masterwork.equipmentrentalapp.models.dto.UserPutDto;
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
class UserApiTest {
  
  private List<UserGetDto> dummyData;
  private final ObjectMapper mapper = new ObjectMapper();
  private final String baseUrl = "/api/users";
  
  
  @Autowired
  private MockMvc mockMvc;
  
  public UserApiTest() {
    mapper.registerModule(new ParameterNamesModule());
    mapper.registerModule(new Jdk8Module());
    mapper.registerModule(new JavaTimeModule());
    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
  }
  
  @BeforeEach
  void setUp() {
    dummyData = Arrays.asList(
        new UserGetDto(1L, "Varga Béla", LocalDate.of(1996, 7,1), "varga.bela@gmail.com", "7620, Pécs, Csobánkai út 89", Arrays.asList(4L, 5L), Arrays.asList(1L)),
        new UserGetDto(2L, "Borostyán Péter", LocalDate.of(1986, 2,11), "borostyan.peter@gmail.com", "4420, Szabadegyháza, Tulipán út 51", Arrays.asList(1L), Arrays.asList(2L)),
        new UserGetDto(3L, "Szabó László", LocalDate.of(1992, 11,5), "szabo.lacika@gmail.com", "7560, Perkáta, Bercsényi út 22", Arrays.asList(2L), Collections.emptyList()),
        new UserGetDto(4L, "Benedek Bernadett", LocalDate.of(1989, 12,9), "benedek.bernikee@gmail.com", "3650, Hódmezővásárhely, Rózsás utca 6", Arrays.asList(3L), Collections.emptyList()),
        new UserGetDto(5L, "Rúfusz Boldizsár", LocalDate.of(1974, 1,26), "rufusz.boldizsar@gmail.com", "8520, Tata, Putnoki út 45", Collections.emptyList(), Collections.emptyList()));
  }
  
  @Test
  void getAllUsers() throws Exception {
    String expected = mapper.writeValueAsString(dummyData);
  
    String actual = mockMvc.perform(get(baseUrl))
                           .andExpect(status().isOk())
                           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                           .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
  
    assertEquals(mapper.readTree(expected), mapper.readTree(actual));
  
  }
  
  @Test
  void getUserById() throws Exception {
    int id = 4;
  
    String expected = mapper.writeValueAsString(dummyData.get(id - 1));
  
  
    String actual = mockMvc.perform(get(baseUrl + "/user/" + id)).andExpect(status().isOk())
                           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                           .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
  
    assertEquals(mapper.readTree(expected), mapper.readTree(actual));
  }
  
  @Test
  void getUsersByName() throws Exception {
    String equipmentName = "Varga Béla";
  
    String expected = mapper.writeValueAsString(Arrays.asList(dummyData.get(0)));
  
  
    String actual = mockMvc.perform(get(baseUrl + "/users-by-name?userName=" + equipmentName)).andExpect(status().isOk())
                           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                           .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
  
    assertEquals(mapper.readTree(expected), mapper.readTree(actual));
  }
  
  @Test
  void getUserByEmail() throws Exception {
    String equipmentName = "varga.bela@gmail.com";
  
    String expected = mapper.writeValueAsString(dummyData.get(0));
  
  
    String actual = mockMvc.perform(get(baseUrl + "/users-by-email?email=" + equipmentName)).andExpect(status().isOk())
                           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                           .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
  
    assertEquals(mapper.readTree(expected), mapper.readTree(actual));
  }
  
  @Test
  void deleteUserById() throws Exception {
    int id = 5;
  
    String expected = mapper.writeValueAsString(dummyData.get(id - 1));
  
    String actual =
        mockMvc.perform(delete(baseUrl + "/user/" + id))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    assertEquals(expected, actual);
  }
  
  @Test
  void updateUser() throws Exception {
    int id = 1;
  
    String content = mapper.writeValueAsString(new UserPutDto(
        "Varga Bella",
        "varga.bella@gmail.com",
        "5987, Somogyvámos, Szabadkai út 75"));
  
    String expected = mapper.writeValueAsString(new UserGetDto(
        1L,
        "Varga Bella",
        LocalDate.of(1996, 7, 1),
        "varga.bella@gmail.com",
        "5987, Somogyvámos, Szabadkai út 75",
        Arrays.asList(4L, 5L),
        Arrays.asList(1L)));
  
    String actual =
        mockMvc.perform(put(baseUrl + "/" + id).contentType(MediaType.APPLICATION_JSON).content(content))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    assertEquals(mapper.readTree(expected), mapper.readTree(actual));
  }
  
  @Test
  void createUser() throws Exception {
    String content = mapper.writeValueAsString(new UserPostDto(
        "Hollandi János",
        LocalDate.of(1946, 7,8),
        "hollandi.jani@freemail.hu",
        "1145, Budapest, Virág utca 8"));
  
    String expected = mapper.writeValueAsString(new UserGetDto(
        6L,
        "Hollandi János",
        LocalDate.of(1946, 7,8),
        "hollandi.jani@freemail.hu",
        "1145, Budapest, Virág utca 8",
        Collections.emptyList(),
        Collections.emptyList()));
    
    String actual =
        mockMvc.perform(post(baseUrl).contentType(MediaType.APPLICATION_JSON).content(content))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    assertEquals(mapper.readTree(expected), mapper.readTree(actual));
  }
}
