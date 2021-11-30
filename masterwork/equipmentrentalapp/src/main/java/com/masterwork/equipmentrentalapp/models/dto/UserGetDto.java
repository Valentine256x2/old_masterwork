package com.masterwork.equipmentrentalapp.models.dto;

import static java.util.Objects.requireNonNull;

import com.masterwork.equipmentrentalapp.models.entities.Booking;
import com.masterwork.equipmentrentalapp.models.entities.RentalHistory;
import com.masterwork.equipmentrentalapp.models.entities.User;
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
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@ApiModel(description = "Details about the user")
public class UserGetDto {
  
  @ApiModelProperty(notes = "The unique ID of the user")
  private Long id;
  
  @ApiModelProperty(notes = "The name")
  private String name;
  
  @ApiModelProperty(notes = "The date of birth")
  private LocalDate dateOfBirth;
  
  @ApiModelProperty(notes = "The email")
  private String email;
  
  @ApiModelProperty(notes = "The address")
  private String address;
  
  @ApiModelProperty(notes = "The bookings")
  private List<Long> bookingIds;
  
  @ApiModelProperty(notes = "The rental history")
  private List<Long> rentalHistoryIds;
  
  public static UserGetDto convertToDataObject(User user) {
    requireNonNull(user, "Rental history must not be null.");
    return new UserGetDto(user.getId(), user.getName(), user.getDateOfBirth(), user.getEmail(),
        user.getAddress(),
        user.getBookings().stream().map(Booking::getId).collect(Collectors.toList()),
        user.getRentalHistory().stream().map(RentalHistory::getId).collect(Collectors.toList()));
  }
}
