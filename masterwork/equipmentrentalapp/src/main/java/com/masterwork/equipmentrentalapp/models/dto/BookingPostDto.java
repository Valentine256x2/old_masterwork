package com.masterwork.equipmentrentalapp.models.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
@ApiModel(description = "Details about creating booking")
public class BookingPostDto {
  
  @ApiModelProperty(notes = "The user who is making the booking")
  @NotNull(message = "User id cannot be null")
  private Long userId;
  
  @ApiModelProperty(notes = "The list of the equipments booked")
  @NotEmpty(message = "Equipments list cannot be null nor empty list")
  private List<@NotNull(message = "Equipments list elements cannot be null") String> equipmentNames;
  
  @ApiModelProperty(notes = "The expected return date")
  @NotNull(message = "Expected return date cannot be null")
  @Future(message = "Expected return date cannot be in the past")
  private LocalDate expectedReturnDate;
  
  @ApiModelProperty(notes = "The rental date")
  @NotNull(message = "Rental date cannot be null")
  @FutureOrPresent(message = "Rental date cannot be in the past")
  private LocalDate rentalDate;
}
