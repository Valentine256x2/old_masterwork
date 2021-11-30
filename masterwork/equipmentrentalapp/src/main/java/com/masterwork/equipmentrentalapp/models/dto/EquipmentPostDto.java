package com.masterwork.equipmentrentalapp.models.dto;

import com.masterwork.equipmentrentalapp.models.Category;
import com.masterwork.equipmentrentalapp.models.entities.Equipment;
import com.masterwork.equipmentrentalapp.services.EquipmentService;
import com.masterwork.equipmentrentalapp.services.EquipmentServiceImpl;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@ToString
@EqualsAndHashCode
@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@ApiModel(description = "Details about creating equipment")
public class EquipmentPostDto {
  
  @ApiModelProperty(notes = "The name of the equipment")
  @NotBlank(message = "Name cannot be null nor empty string nor string containing only white spaces")
  private String name;
  
  @ApiModelProperty(notes = "The category of the equipment")
  @NotNull(message = "Category cannot be null")
  private String category;
  
  @ApiModelProperty(notes = "The daily price")
  @NotNull(message = "Daily price cannot be null")
  @Positive(message = "The daily price must be positive integer")
  private Long dailyPrice;
  
  public Equipment convertToEquipment() {
    return new Equipment(this.name, Category.parseToEquipmentCategory(this.category.toUpperCase()), this.dailyPrice, new ArrayList<>(),
        new ArrayList<>());
  }
  
  public EquipmentPostDto(
      @NotBlank(message = "Name cannot be null nor empty string nor string containing only white spaces") String name,
      @NotBlank(message = "Category cannot be null or empty string") String category,
      @NotNull(message = "Daily price cannot be null") @Positive(message = "The daily price must be positive integer") Long dailyPrice) {
    this.name = name;
    this.category = category;
    this.dailyPrice = dailyPrice;
  }
}
