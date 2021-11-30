package com.masterwork.equipmentrentalapp.models.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = "Details about updating equipment")
public class EquipmentPutDto {
  
  @ApiModelProperty(notes = "The name of the equipment")
  private String name;
  
  @ApiModelProperty(notes = "The category of the equipment")
  private String category;
  
  @ApiModelProperty(notes = "The daily price")
  private Long dailyPrice;
}
