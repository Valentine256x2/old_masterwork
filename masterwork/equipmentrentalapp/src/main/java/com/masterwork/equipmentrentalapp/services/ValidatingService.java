package com.masterwork.equipmentrentalapp.services;

import java.time.LocalDate;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public interface ValidatingService {
  
  void validateRentalDates(@NotNull @FutureOrPresent LocalDate rentalDate,
                           @NotNull @Future LocalDate expectedReturnDate);
  
  void validateUserExistence(@NotNull @Positive Long userId);
  
  void validateEquipmentExistence(@NotNull @Positive Long equipmentId);
}
