package com.masterwork.equipmentrentalapp.models.dto;

import static java.util.Objects.requireNonNull;

import com.masterwork.equipmentrentalapp.models.Category;
import com.masterwork.equipmentrentalapp.models.entities.Booking;
import com.masterwork.equipmentrentalapp.models.entities.Equipment;
import com.masterwork.equipmentrentalapp.models.entities.RentalHistory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = "Details about the equipment")
public class EquipmentGetDto {
  
  @ApiModelProperty(notes = "The unique ID of the equipment")
  private Long id;
  
  @ApiModelProperty(notes = "The name of the equipment")
  private String name;
  
  @ApiModelProperty(notes = "The category of the equipment")
  private Category category;
  
  @ApiModelProperty(notes = "The daily price")
  private Long dailyPrice;
  
  @ApiModelProperty(notes = "The bookings of this equipment")
  private List<Long> bookingIds;
  
  @ApiModelProperty(notes = "The rental history of this equipment")
  private List<Long> rentalHistoryIds;
  
  
  public static EquipmentGetDto convertToDataObject(Equipment equipment) {
    requireNonNull(equipment, "Equipment must not be null.");
    return new EquipmentGetDto(equipment.getId(), equipment.getName(), equipment.getCategory(),
        equipment.getDailyPrice(),
        equipment.getBookings().stream().map(Booking::getId).collect(Collectors.toList()),
        equipment.getRentalHistory().stream().map(RentalHistory::getId)
                 .collect(Collectors.toList()));
  }
}
