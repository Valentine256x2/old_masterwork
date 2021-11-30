package com.masterwork.equipmentrentalapp.models.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
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
@ApiModel(description = "Details about booking updates")
public class BookingPutDto {
  
  @ApiModelProperty(notes = "The wanted return date")
  LocalDate expectedReturnDate;
  
  @ApiModelProperty(notes = "The wanted rental date")
  LocalDate rentalDate;
}
