package com.masterwork.equipmentrentalapp.services;

import com.masterwork.equipmentrentalapp.models.dto.EquipmentGetDto;
import com.masterwork.equipmentrentalapp.models.dto.EquipmentPostDto;
import com.masterwork.equipmentrentalapp.models.dto.EquipmentPutDto;
import com.masterwork.equipmentrentalapp.models.entities.Equipment;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.NonNull;

public interface EquipmentService {
  
  List<EquipmentGetDto> getAllEquipmentDtos();
  
  Equipment getEquipmentById(@NotNull @Positive Long id);
  
  EquipmentGetDto getEquipmentDtoById(@NotNull @Positive Long id);
  
  List<EquipmentGetDto> getEquipmentsByName(@NotBlank String name);
  
  List<EquipmentGetDto> getEquipmentsByCategory(@NotBlank String category);
  
  EquipmentGetDto createEquipment(@NonNull @Valid EquipmentPostDto equipmentPostDto);
  
  EquipmentGetDto updateEquipment(@NotNull @Positive Long id, EquipmentPutDto equipmentPutDto);
  
  EquipmentGetDto removeEquipment(@NotNull @Positive Long id);
  
  List<Equipment> getAllEquipments();
}
