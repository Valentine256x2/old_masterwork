package com.masterwork.equipmentrentalapp.services;

import com.masterwork.equipmentrentalapp.exceptions.BadResourceException;
import com.masterwork.equipmentrentalapp.exceptions.ResourceNotFoundException;
import com.masterwork.equipmentrentalapp.models.Category;
import com.masterwork.equipmentrentalapp.models.dto.EquipmentGetDto;
import com.masterwork.equipmentrentalapp.models.dto.EquipmentPostDto;
import com.masterwork.equipmentrentalapp.models.dto.EquipmentPutDto;
import com.masterwork.equipmentrentalapp.models.entities.Equipment;
import com.masterwork.equipmentrentalapp.repositories.EquipmentRepo;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.executable.ValidateOnExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@ValidateOnExecution
@Validated
@Service
@Transactional
public class EquipmentServiceImpl implements EquipmentService {
  
  @Autowired
  private EquipmentRepo repo;
  
  @Override
  public List<Equipment> getAllEquipments() {
    return repo.findAll();
  }
  
  @Override
  public List<EquipmentGetDto> getAllEquipmentDtos() {
    return getAllEquipments().stream().map(EquipmentGetDto::convertToDataObject)
                             .collect(Collectors.toList());
  }
  
  @Override
  public Equipment getEquipmentById(Long id) {
    return repo.findById(id).orElseThrow(
        () -> new ResourceNotFoundException("Equipment not found with id: " + id));
  }

  @Override
  public EquipmentGetDto getEquipmentDtoById(Long id) {
    return EquipmentGetDto.convertToDataObject(getEquipmentById(id));
  }
  
  @Override
  public List<EquipmentGetDto> getEquipmentsByName(String name) {
    
    List<Equipment> equipments = repo.findAllByName(name);
    if (equipments.isEmpty()) {
      throw new ResourceNotFoundException("Cannot find any equipment with the given name: " + name);
    }
    return equipments.stream().map(EquipmentGetDto::convertToDataObject)
                     .collect(Collectors.toList());
  }
  
  @Override
  public List<EquipmentGetDto> getEquipmentsByCategory(String inputCategory) {
    
    Category category = Category.parseToEquipmentCategory(inputCategory.toUpperCase());
    
    List<Equipment> equipments = repo.findAllByCategory(category);
    if (equipments.isEmpty()) {
      throw new ResourceNotFoundException(
          "Cannot find any equipment with the given category: " + inputCategory);
    }
    return equipments.stream().map(EquipmentGetDto::convertToDataObject)
                     .collect(Collectors.toList());
  }
  
  @Override
  public EquipmentGetDto createEquipment(EquipmentPostDto equipmentPostDto) {
    return EquipmentGetDto.convertToDataObject(repo.save(equipmentPostDto.convertToEquipment()));
  }
  
  @Override
  public EquipmentGetDto updateEquipment(Long id, EquipmentPutDto equipmentPutDto) {
    Equipment equipment = getEquipmentById(id);
    
    if (null != equipmentPutDto.getName() && !equipmentPutDto.getName().isEmpty()) {
      equipment.setName(equipmentPutDto.getName());
    }
    if (null != equipmentPutDto.getCategory()) {
      equipment.setCategory(Category.parseToEquipmentCategory(equipmentPutDto.getCategory().toUpperCase()));
    }
    if (null != equipmentPutDto.getDailyPrice()) {
      if (equipmentPutDto.getDailyPrice() > 0) {
        equipment.setDailyPrice(equipmentPutDto.getDailyPrice());
      } else {
        throw new BadResourceException("Daily price must be positive and not 0");
      }
    }
    return EquipmentGetDto.convertToDataObject(repo.save(equipment));
  }
  
  @Override
  public EquipmentGetDto removeEquipment(Long id) {
    EquipmentGetDto equipment = getEquipmentDtoById(id);
    
    if (!equipment.getBookingIds().isEmpty()) {
      throw new BadResourceException(
          "Equipment cannot be deleted because it is rented at the moment or has bookings in the future");
    }
    repo.delete(getEquipmentById(id));
    return equipment;
  }
}
