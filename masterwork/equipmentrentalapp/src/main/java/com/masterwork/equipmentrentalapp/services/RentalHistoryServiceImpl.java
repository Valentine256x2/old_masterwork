package com.masterwork.equipmentrentalapp.services;

import com.masterwork.equipmentrentalapp.exceptions.ResourceNotFoundException;
import com.masterwork.equipmentrentalapp.models.dto.RentalHistoryGetDto;
import com.masterwork.equipmentrentalapp.models.entities.Equipment;
import com.masterwork.equipmentrentalapp.models.entities.RentalHistory;
import com.masterwork.equipmentrentalapp.models.entities.User;
import com.masterwork.equipmentrentalapp.repositories.RentalHistoryRepo;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@Transactional
public class RentalHistoryServiceImpl implements RentalHistoryService {
  
  @Autowired
  private RentalHistoryRepo repo;
  
  @Override
  public List<RentalHistoryGetDto> getAllRentalHistory() {
    return repo.findAll().stream().map(RentalHistoryGetDto::convertToDataObject)
               .collect(Collectors.toList());
  }
  
  @Override
  public RentalHistoryGetDto getRentalHistoryById(Long id) {
    return RentalHistoryGetDto.convertToDataObject(repo.findById(id).orElseThrow(
        () -> new ResourceNotFoundException("Rental history not found with id: " + id)));
  }
  
  @Override
  public RentalHistoryGetDto createRentalHistory(User user, List<Equipment> equipments,
                                                 LocalDate rentalDate, LocalDate expectedReturnDate,
                                                 Long pricePaid) {
    return RentalHistoryGetDto.convertToDataObject(
        repo.save(new RentalHistory(user, equipments, rentalDate, expectedReturnDate, pricePaid)));
  }
  
  @Override
  public List<RentalHistoryGetDto> getRentalHistoryByEquipmentId(Long equipmentId) {
    return repo.findAll().stream().filter(rentalHistory -> rentalHistory.getEquipments().stream()
                                                                        .anyMatch(
                                                                            equipment -> equipment
                                                                                .getId().equals(
                                                                                    equipmentId)))
               .map(RentalHistoryGetDto::convertToDataObject).collect(Collectors.toList());
    //Todo: something wrong on the the return (in postman empty list returns)
  }
  
  @Override
  public List<RentalHistoryGetDto> getRentalHistoryByUserId(Long userId) {
    return repo.findAllByUserId(userId).stream().map(RentalHistoryGetDto::convertToDataObject)
               .collect(Collectors.toList());
  }
}
