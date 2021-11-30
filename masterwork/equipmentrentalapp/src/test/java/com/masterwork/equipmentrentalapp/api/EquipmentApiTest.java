package com.masterwork.equipmentrentalapp.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.masterwork.equipmentrentalapp.models.Category;
import com.masterwork.equipmentrentalapp.models.dto.BookingGetDto;
import com.masterwork.equipmentrentalapp.models.dto.EquipmentGetDto;
import com.masterwork.equipmentrentalapp.models.dto.EquipmentPutDto;
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
class EquipmentApiTest {
  
  private List<EquipmentGetDto> dummyData;
  private final ObjectMapper mapper = new ObjectMapper();
  private final String baseUrl = "/api/equipments";
  
  @Autowired
  private MockMvc mockMvc;
  
  public EquipmentApiTest() {
    mapper.registerModule(new ParameterNamesModule());
    mapper.registerModule(new Jdk8Module());
    mapper.registerModule(new JavaTimeModule());
    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
  }
  
  @BeforeEach
  void setUp() {
    dummyData = Arrays.asList(
        new EquipmentGetDto(1L,"Hammer", Category.HAND_TOOLS,500L,Arrays.asList(2L),
            Collections.emptyList()),
        new EquipmentGetDto(2L,"Drill", Category.POWER_TOOLS,1500L,Arrays.asList(1L),
            Arrays.asList(2L)),
        new EquipmentGetDto(3L,"Hammer", Category.HAND_TOOLS,500L,Arrays.asList(3L, 5L),
            Arrays.asList(1L)),
        new EquipmentGetDto(4L,"Compressor", Category.AIR_TOOLS_AND_COMPRESSORS,2000L,Arrays.asList(4L),
            Collections.emptyList()),
        new EquipmentGetDto(5L,"Compressor", Category.AIR_TOOLS_AND_COMPRESSORS,2000L,Arrays.asList(3L),
            Collections.emptyList()),
        new EquipmentGetDto(6L,"Mixer", Category.POWER_TOOLS,1200L, Collections.emptyList(),
            Collections.emptyList())
    );
  }
  
  @Test
  void getAllEquipments() throws Exception {
    String expected = mapper.writeValueAsString(dummyData);
  
    String actual = mockMvc.perform(get(baseUrl))
                           .andExpect(status().isOk())
                           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                           .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
  
    assertEquals(mapper.readTree(expected), mapper.readTree(actual));
    
  }
  
  @Test
  void getEquipmentById_Ok() throws Exception {
    int id = 4;
  
    String expected = mapper.writeValueAsString(dummyData.get((id - 1)));
  
  
    String actual = mockMvc.perform(get(baseUrl + "/equipment/" + id)).andExpect(status().isOk())
                           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                           .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
  
    assertEquals(mapper.readTree(expected), mapper.readTree(actual));
  }
  
  @Test
  void getEquipmentsByName_Ok() throws Exception {
    String equipmentName = "Hammer";
  
    String expected = mapper.writeValueAsString(Arrays.asList(dummyData.get(0), dummyData.get(2)));
  
  
    String actual = mockMvc.perform(get(baseUrl + "/equipments-by-name?equipment=" + equipmentName)).andExpect(status().isOk())
                           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                           .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
  
    assertEquals(mapper.readTree(expected), mapper.readTree(actual));
  }
  
  @Test
  void getEquipmentsByCategory_Ok() throws Exception {
    String equipmentCategory = "hand_tools";
  
    String expected = mapper.writeValueAsString(Arrays.asList(dummyData.get(0), dummyData.get(2)));
  
  
    String actual = mockMvc.perform(get(baseUrl + "/equipments-by-category?equipmentCategory=" + equipmentCategory)).andExpect(status().isOk())
                           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                           .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
  
    assertEquals(mapper.readTree(expected), mapper.readTree(actual));
  }
  
  @Test
  void deleteEquipmentById_Ok() throws Exception {
    int id = 6;
  
    String expected = mapper.writeValueAsString(dummyData.get(id - 1));
  
    String actual =
        mockMvc.perform(delete(baseUrl + "/equipment/" + id))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    assertEquals(expected, actual);
  }
  
  @Test
  void updateEquipment_Ok() throws Exception {
    int id = 1;
  
    String expected = mapper.writeValueAsString(new EquipmentGetDto(
        1L,
        "Hemmer",
        Category.ELECTRICAL,
        200L,
        Arrays.asList(2L),
        Collections.emptyList()));
  
    String content = mapper.writeValueAsString(new EquipmentPutDto("Hemmer", "ELECTRICAL", 200L));
  
    String actual =
        mockMvc.perform(put(baseUrl + "/" + id).contentType(MediaType.APPLICATION_JSON).content(content))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    assertEquals(mapper.readTree(expected), mapper.readTree(actual));
  }
  
  @Test
  void createEquipment_Ok() throws Exception {
  
    String expected = mapper.writeValueAsString(new EquipmentGetDto(
        7L,
        "Medium Hammer",
        Category.HAND_TOOLS,
        700L,
        Collections.emptyList(),
        Collections.emptyList()));
  
    String content = mapper.writeValueAsString(new EquipmentPutDto("Medium Hammer", "HAND_TOOLS", 700L));
  
    String actual =
        mockMvc.perform(post(baseUrl).contentType(MediaType.APPLICATION_JSON).content(content))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    assertEquals(mapper.readTree(expected), mapper.readTree(actual));
  }
}
