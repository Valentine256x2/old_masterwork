package com.masterwork.equipmentrentalapp.services;

import com.masterwork.equipmentrentalapp.models.dto.BookingGetDto;
import com.masterwork.equipmentrentalapp.models.dto.RentalHistoryGetDto;
import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public interface BookingService {
  
  List<BookingGetDto> getAllBookingDtos();
  
  BookingGetDto getBookingDtoById(@NotNull @Positive Long id);
  
  BookingGetDto updateBookingReturnDate(@NotNull @Positive Long id,
                                        LocalDate expectedReturnDate,
                                        LocalDate rentalDate);
  
  BookingGetDto removeBooking(@NotNull @Positive Long id);
  
  List<BookingGetDto> getBookingsByUserId(@NotNull @Positive Long userId);
  
  List<BookingGetDto> getBookingsByEquipmentId(@NotNull @Positive Long equipmentId);
  
  BookingGetDto bookEquipments(@NotEmpty List<@NotBlank String> equipmentNames, LocalDate rentalDate,
                               LocalDate expectedReturnDate, Long userId);
  
  boolean isEquipmentAvailableBetween(Long equipmentId, LocalDate rentalDate, LocalDate returnDate);
  
  RentalHistoryGetDto returnEquipments(@NotNull @Positive Long bookingId);
}
