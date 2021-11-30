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
@ApiModel(description = "Details about updating the user")
public class UserPutDto {
  
  @ApiModelProperty(notes = "The name")
  private String name;
  
  @ApiModelProperty(notes = "The email")
  private String email;
  
  @ApiModelProperty(notes = "The address")
  private String address;
  
}
