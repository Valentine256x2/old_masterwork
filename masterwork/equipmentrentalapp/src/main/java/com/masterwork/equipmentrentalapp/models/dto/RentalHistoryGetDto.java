package com.masterwork.equipmentrentalapp.models.dto;

import static java.util.Objects.requireNonNull;

import com.masterwork.equipmentrentalapp.models.entities.Equipment;
import com.masterwork.equipmentrentalapp.models.entities.RentalHistory;
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
@ApiModel(description = "Details about the rental history")
public class RentalHistoryGetDto {
  
  @ApiModelProperty(notes = "The unique ID of the rental history")
  private Long id;
  
  @ApiModelProperty(notes = "The user")
  private Long userId;
  
  @ApiModelProperty(notes = "The equipments rented")
  private List<Long> equipmentIds;
  
  @ApiModelProperty(notes = "The rental date")
  private LocalDate rentalDate;
  
  @ApiModelProperty(notes = "The expected return date")
  private LocalDate expectedReturnDate;
  
  @ApiModelProperty(notes = "The actual return date")
  private LocalDate actualReturnDate;
  
  private Long pricePaid;
  
  public static RentalHistoryGetDto convertToDataObject(RentalHistory rentalHistory) {
    requireNonNull(rentalHistory, "Rental history must not be null.");
    return new RentalHistoryGetDto(rentalHistory.getId(), rentalHistory.getUser().getId(),
        rentalHistory.getEquipments().stream().map(Equipment::getId).collect(Collectors.toList()),
        rentalHistory.getRentalDate(), rentalHistory.getExpectedReturnDate(),
        rentalHistory.getActualReturnDate(), rentalHistory.getPricePaid());
  }
}
