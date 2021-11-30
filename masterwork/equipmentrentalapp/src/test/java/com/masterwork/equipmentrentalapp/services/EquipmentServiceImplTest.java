package com.masterwork.equipmentrentalapp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import com.masterwork.equipmentrentalapp.models.Category;
import com.masterwork.equipmentrentalapp.models.dto.EquipmentGetDto;
import com.masterwork.equipmentrentalapp.models.dto.EquipmentPostDto;
import com.masterwork.equipmentrentalapp.models.dto.EquipmentPutDto;
import com.masterwork.equipmentrentalapp.models.entities.Equipment;
import com.masterwork.equipmentrentalapp.repositories.EquipmentRepo;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EquipmentServiceImplTest {
  
  @Mock
  private EquipmentRepo equipmentRepo;
  
  @InjectMocks
  private EquipmentServiceImpl equipmentService;
  
  private List<EquipmentGetDto> equipmentGetDtos;
  
  private List<Equipment> equipments;
  
  @BeforeEach
  void setUp() {
    equipmentGetDtos = Arrays.asList(
        new EquipmentGetDto(1L, "Hammer", Category.HAND_TOOLS, 500L, Collections.emptyList(),
            Collections.emptyList()),
        new EquipmentGetDto(2L, "Drill", Category.POWER_TOOLS, 1500L, Collections.emptyList(),
            Collections.emptyList()));
    equipments = Arrays.asList(
        new Equipment(1L, "Hammer", Category.HAND_TOOLS, 500L, Collections.emptyList(),
            Collections.emptyList()),
        new Equipment(2L, "Drill", Category.POWER_TOOLS, 1500L, Collections.emptyList(),
            Collections.emptyList()));
  }
  
  @Test
  void getAllEquipments() {
    when(equipmentRepo.findAll()).thenReturn(equipments);
    List<Equipment> expected = equipments;
    List<Equipment> actual = equipmentService.getAllEquipments();
    assertEquals(expected, actual);
    
  }
  
  @Test
  void getAllEquipmentDtos() {
    try (MockedStatic<EquipmentGetDto> mockEquipmentGetDto = mockStatic(EquipmentGetDto.class);) {
  
      mockEquipmentGetDto.when(() -> EquipmentGetDto.convertToDataObject(equipments.get(0))).thenReturn(equipmentGetDtos.get(0));
      mockEquipmentGetDto.when(() -> EquipmentGetDto.convertToDataObject(equipments.get(1))).thenReturn(equipmentGetDtos.get(1));
  
      when(equipmentService.getAllEquipments()).thenReturn(equipments);
      List<EquipmentGetDto> expected = equipmentGetDtos;
      List<EquipmentGetDto> actual = equipmentService.getAllEquipmentDtos();
      assertEquals(expected, actual);
    }
  }
  
  @Test
  void getEquipmentDtoById() {
    try (MockedStatic<EquipmentGetDto> mockEquipmentGetDto = mockStatic(EquipmentGetDto.class)) {
      Long id = 1L;
      EquipmentServiceImpl spy = Mockito.spy(equipmentService);
      
      when(equipmentRepo.findById(id)).thenReturn(Optional.of(equipments.get(0)));
      
      mockEquipmentGetDto.when(() -> EquipmentGetDto.convertToDataObject(equipments.get(0)))
                         .thenReturn(equipmentGetDtos.get(0));
      
      EquipmentGetDto expected = equipmentGetDtos.get(0);
      EquipmentGetDto actual = spy.getEquipmentDtoById(id);
      assertEquals(expected, actual);
    }
  }
  
  @Test
  void getEquipmentsByName() {
    String name = "Hammer";
    when(equipmentRepo.findAllByName(name))
        .thenReturn(Collections.singletonList(equipments.get(0)));
    
    try (MockedStatic<EquipmentGetDto> mockEquipmentGetDto = mockStatic(EquipmentGetDto.class);) {
  
  
      mockEquipmentGetDto.when(() -> EquipmentGetDto.convertToDataObject(equipments.get(0))).thenReturn(equipmentGetDtos.get(0));
  
      List<EquipmentGetDto> expected = Collections.singletonList(equipmentGetDtos.get(0));
      List<EquipmentGetDto> actual = equipmentService.getEquipmentsByName(name);
      assertEquals(expected, actual);
    }
  }
  
  @Test
  void getEquipmentsByCategory() {
    Category categoryEnum = Category.HAND_TOOLS;
    String category = categoryEnum.name();
    
    try (MockedStatic<EquipmentGetDto> mockEquipmentGetDto = mockStatic(EquipmentGetDto.class)) {
      
      
      when(equipmentRepo.findAllByCategory(any()))
          .thenReturn(Collections.singletonList(equipments.get(0)));
      
      
      mockEquipmentGetDto.when(() -> EquipmentGetDto.convertToDataObject(equipments.get(0)))
                         .thenReturn(equipmentGetDtos.get(0));
      
      List<EquipmentGetDto> expected = Collections.singletonList(equipmentGetDtos.get(0));
      List<EquipmentGetDto> actual = equipmentService.getEquipmentsByCategory(category);
      assertEquals(expected, actual);
    }
  }
  
  @Test
  void createEquipment() {
    EquipmentPostDto eqPostDto = new EquipmentPostDto("Saw", "hand_tools", 500L);
    Equipment toBeSaved = new Equipment("Saw", Category.HAND_TOOLS, 500L, Collections.emptyList(),
        Collections.emptyList());
    EquipmentGetDto saved =
        new EquipmentGetDto(7L, "Saw", Category.HAND_TOOLS, 500L, Collections.emptyList(),
            Collections.emptyList());
    
    try (MockedStatic<EquipmentGetDto> mockEquipmentGetDto = mockStatic(EquipmentGetDto.class)) {
      
      EquipmentPostDto spy = Mockito.spy(eqPostDto);
      doReturn(toBeSaved).when(spy).convertToEquipment();
      mockEquipmentGetDto.when(() -> EquipmentGetDto.convertToDataObject(toBeSaved))
                         .thenReturn(saved);
      
      when(equipmentRepo.save(toBeSaved)).thenReturn(toBeSaved);
      
      EquipmentGetDto expected =
          new EquipmentGetDto(7L, "Saw", Category.HAND_TOOLS, 500L, Collections.emptyList(),
              Collections.emptyList());
      EquipmentGetDto actual = equipmentService.createEquipment(spy);
      assertEquals(expected, actual);
    }
  }
  
  @Test
  void updateEquipment() {
    try (MockedStatic<EquipmentGetDto> mockEquipmentGetDto = mockStatic(EquipmentGetDto.class)) {
      Long id = 1L;
      EquipmentPutDto eqPutDto = new EquipmentPutDto("Big Hammer", "hand_tools", 750L);
      EquipmentGetDto expected =
          new EquipmentGetDto(1L, "Big Hammer", Category.HAND_TOOLS, 750L, Collections.emptyList(),
              Collections.emptyList());
      Equipment spy = Mockito.spy(equipments.get(0));
      
      when(equipmentRepo.findById(any())).thenReturn(Optional.of(spy));
      
      when(equipmentRepo.save(spy)).thenReturn(spy);
      
      mockEquipmentGetDto.when(() -> EquipmentGetDto.convertToDataObject(spy)).thenReturn(expected);
      EquipmentGetDto actual = equipmentService.updateEquipment(id, eqPutDto);
      assertEquals(expected, actual);
    }
  }
  
  @Test
  void removeEquipment() {
    Long id = 1L;
    when(equipmentRepo.findById(id)).thenReturn(Optional.of(equipments.get(0)));
    EquipmentGetDto expected = equipmentGetDtos.get(0);
    EquipmentGetDto actual = equipmentService.removeEquipment(id);
    assertEquals(expected, actual);
    
  }
}
