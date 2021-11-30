package com.masterwork.equipmentrentalapp.models.dto;

import static java.util.Objects.requireNonNull;

import com.masterwork.equipmentrentalapp.models.entities.Booking;
import com.masterwork.equipmentrentalapp.models.entities.Equipment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@ToString
@EqualsAndHashCode
@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
@ApiModel(description = "Details about bookings")
public class BookingGetDto {
  
  @ApiModelProperty(notes = "The unique ID of the booking")
  private Long id;
  
  @ApiModelProperty(notes = "The user who is making the booking")
  private Long userId;
  
  @ApiModelProperty(notes = "The list of the equipments booked")
  private List<Long> equipmentIds;
  
  @ApiModelProperty(notes = "The expected return date")
  private LocalDate expectedReturnDate;
  
  @ApiModelProperty(notes = "The rental date")
  private LocalDate rentalDate;
  
  public static BookingGetDto convertToDataObject(Booking booking) {
    requireNonNull(booking, "Booking must not be null.");
    return new BookingGetDto(booking.getId(), booking.getUser().getId(),
        booking.getEquipments().stream().map(Equipment::getId).collect(Collectors.toList()),
        booking.getExpectedReturnDate(), booking.getRentalDate());
  }
}
