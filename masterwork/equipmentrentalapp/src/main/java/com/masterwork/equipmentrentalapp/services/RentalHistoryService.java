package com.masterwork.equipmentrentalapp.services;

import com.masterwork.equipmentrentalapp.models.dto.RentalHistoryGetDto;
import com.masterwork.equipmentrentalapp.models.entities.Equipment;
import com.masterwork.equipmentrentalapp.models.entities.User;
import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public interface RentalHistoryService {
  
  List<RentalHistoryGetDto> getAllRentalHistory();
  
  RentalHistoryGetDto getRentalHistoryById(@NotNull @Positive Long id);
  
  RentalHistoryGetDto createRentalHistory(User user, List<Equipment> equipments,
                                          LocalDate rentalDate, LocalDate expectedReturnDate,
                                          Long pricePaid);
  
  List<RentalHistoryGetDto> getRentalHistoryByEquipmentId(@NotNull @Positive Long equipmentId);
  
  List<RentalHistoryGetDto> getRentalHistoryByUserId(@NotNull @Positive Long userId);
}
