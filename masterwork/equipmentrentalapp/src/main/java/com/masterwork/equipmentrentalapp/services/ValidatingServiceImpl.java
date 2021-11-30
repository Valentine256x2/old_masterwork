package com.masterwork.equipmentrentalapp.services;

import com.masterwork.equipmentrentalapp.exceptions.BadResourceException;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@Transactional
public class ValidatingServiceImpl implements ValidatingService {
  
  @Autowired
  private UserService userService;
  
  @Autowired
  private EquipmentService equipmentService;
  
  @Override
  public void validateRentalDates(LocalDate rentalDate, LocalDate expectedReturnDate) {
    if (rentalDate.isAfter(expectedReturnDate) || expectedReturnDate.isEqual(rentalDate)) {
      throw new BadResourceException(
          "The rental date cannot be equal with or after the expected return date");
    }
    if (expectedReturnDate.isAfter(rentalDate.plusMonths(6))) {
      throw new BadResourceException(
          "The expected return date has to be within 6 months from the rental date");
    }
  }
  
  @Override
  public void validateUserExistence(Long userId) {
    userService.getUserById(userId);
  }
  
  @Override
  public void validateEquipmentExistence(Long equipmentId) {
    equipmentService.getEquipmentById(equipmentId);
  }
}
