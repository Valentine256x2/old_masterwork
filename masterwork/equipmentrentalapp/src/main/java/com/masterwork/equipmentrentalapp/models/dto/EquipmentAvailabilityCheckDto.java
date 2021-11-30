package com.masterwork.equipmentrentalapp.models.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
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
@ApiModel(description = "Details about the wanted rental")
public class EquipmentAvailabilityCheckDto {
  
  @ApiModelProperty(notes = "The unique ID of the equipment")
  @NotNull
  @Positive
  private Long equipmentId;
  
  @ApiModelProperty(notes = "The rental date of the equipment")
  @NotNull
  @FutureOrPresent
  private LocalDate rentalDate;
  
  @ApiModelProperty(notes = "The expected return date date of the equipment")
  @NotNull
  @Future
  private LocalDate expectedReturnDate;
}
