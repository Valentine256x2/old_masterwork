package com.masterwork.equipmentrentalapp.api;


import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.masterwork.equipmentrentalapp.models.dto.BookingGetDto;
import com.masterwork.equipmentrentalapp.models.dto.BookingPostDto;
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
public class BookingApiTest {
  
  private List<BookingGetDto> dummyData;
  private final ObjectMapper mapper = new ObjectMapper();
  private final String baseUrl = "/api/bookings";
  
  
  @Autowired
  private MockMvc mockMvc;
  
  public BookingApiTest() {
    mapper.registerModule(new ParameterNamesModule());
    mapper.registerModule(new Jdk8Module());
    mapper.registerModule(new JavaTimeModule());
    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
  }
  
  @BeforeEach
  void setUp() {
    dummyData = Arrays.asList(
        new BookingGetDto(1L, 2L, Arrays.asList(2L), LocalDate.of(2021, 9, 10),
            LocalDate.of(2021, 8, 25)),
        new BookingGetDto(2L, 3L, Arrays.asList(1L), LocalDate.of(2021, 9, 3),
            LocalDate.of(2021, 8, 27)),
        new BookingGetDto(3L, 4L, Arrays.asList(3L, 5L), LocalDate.of(2021, 10, 10),
            LocalDate.of(2021, 9, 7)),
        new BookingGetDto(4L, 1L, Arrays.asList(4L), LocalDate.of(2021, 11, 2),
            LocalDate.of(2021, 8, 2)),
        new BookingGetDto(5L, 1L, Arrays.asList(3L), LocalDate.of(2021, 9, 5),
            LocalDate.of(2021, 8, 5)));
  }
  
  @Test
  void getAllBookings_Ok() throws Exception {
    
    String expected = mapper.writeValueAsString(dummyData);
    
    String actual = mockMvc.perform(get(baseUrl))
                           .andExpect(status().isOk())
                           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                           .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    
    assertEquals(mapper.readTree(expected), mapper.readTree(actual));
    
  }
  
  @Test
  void getBookingById_Ok() throws Exception {
    int id = 4;
    
    String expected = mapper.writeValueAsString(dummyData.get(id - 1));
    
    
    String actual = mockMvc.perform(get(baseUrl + "/booking/" + id)).andExpect(status().isOk())
                           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                           .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
  
    assertEquals(mapper.readTree(expected), mapper.readTree(actual));
  
  }
  
  @Test
  void getBookingsByUserId_Ok() throws Exception {
    Long id = 4L;
    String expected = mapper.writeValueAsString(Collections.singletonList(dummyData.get(2)));
  
  
    String actual =
        mockMvc.perform(get(baseUrl + "/bookings-by-user/" + id))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    assertEquals(mapper.readTree(expected), mapper.readTree(actual));
  
  }

  @Test
  void getBookingsByEquipmentId_Ok() throws Exception {
    Long id = 4L;
    String expected = mapper.writeValueAsString(Collections.singletonList(dummyData.get(3)));
  
  
    String actual =
        mockMvc.perform(get(baseUrl + "/bookings-by-equipment/" + id))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    assertEquals(mapper.readTree(expected), mapper.readTree(actual));
  
  
  }

  @Test
  void checkIfEquipmentAvailable_Ok() throws Exception {
    String expected = "true";
    
    String actual =
        mockMvc.perform(get(baseUrl + "/is-available?equipmentId=2&rentalDate=2021-08-23&expectedReturnDate=2021-08-24"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    assertEquals(expected, actual);
  }

  @Test
  void cancelBookingById_Ok() throws Exception {
    int id = 1;
    
    String expected = mapper.writeValueAsString(dummyData.get(id - 1));
  
    String actual =
        mockMvc.perform(delete(baseUrl + "/cancel-booking/" + id))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    assertEquals(expected, actual);
    
  }

  @Test
  void updateBookingRecordReturnDate_Ok() throws Exception {
    int id = 5;
    
    String expected = mapper.writeValueAsString(new BookingGetDto(
        5L,
        1L,
        Arrays.asList(3L),
        LocalDate.of(2021, 9,6),
        LocalDate.of(2021, 8, 5)));
  
    String content = "{\"expectedReturnDate\": \"2021-09-06\"}";
  
    String actual =
        mockMvc.perform(put(baseUrl + "/" + id).contentType(MediaType.APPLICATION_JSON).content(content))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    assertEquals(mapper.readTree(expected), mapper.readTree(actual));
  }

  @Test
  void createBooking_Ok() throws Exception {
    String content = mapper.writeValueAsString(new BookingPostDto(
        2L,
        Arrays.asList("Hammer", "Drill"),
        LocalDate.of(2021, 11,10),
        LocalDate.of(2021, 10, 7)));
  
    String expected = mapper.writeValueAsString(new BookingGetDto(
        6L,
        2L,
        Arrays.asList(1L, 2L),
        LocalDate.of(2021, 11,10),
        LocalDate.of(2021, 10, 7)));
  
    String actual =
        mockMvc.perform(post(baseUrl).contentType(MediaType.APPLICATION_JSON).content(content))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    assertEquals(mapper.readTree(expected), mapper.readTree(actual));
  }

  @Test
  void returnEquipments_Ok() throws Exception {
    int id = 5;
    
    Long price = 500L * (1 + LocalDate.now().toEpochDay() - LocalDate.of(2021, 8,5).toEpochDay());
  
    String expected = mapper.writeValueAsString(new RentalHistoryGetDto(
        3L,
        1L,
        Arrays.asList(3L),
        LocalDate.of(2021, 8,5),
        LocalDate.of(2021, 9, 5),
        LocalDate.now(),
        price
        ));
  
    String actual =
        mockMvc.perform(put(baseUrl + "/return/" + id))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    assertEquals(mapper.readTree(expected), mapper.readTree(actual));
  }
}
