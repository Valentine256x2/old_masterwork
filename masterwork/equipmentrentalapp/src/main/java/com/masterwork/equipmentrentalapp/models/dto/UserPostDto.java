package com.masterwork.equipmentrentalapp.models.dto;

import com.masterwork.equipmentrentalapp.models.entities.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@ToString
@EqualsAndHashCode
@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@ApiModel(description = "Details about creating the user")
public class UserPostDto {
  
  @ApiModelProperty(notes = "The name")
  @NotBlank(message = "Name cannot be null nor empty string nor string with only whitespaces")
  private String name;
  
  @ApiModelProperty(notes = "The date of birth")
  @NotNull(message = "Date of birth cannot be null")
  @Past(message = "The date of birth has to be in the past")
  private LocalDate dateOfBirth;
  
  @ApiModelProperty(notes = "The email")
  @NotBlank(message = "Email cannot be null nor empty string nor string with only whitespaces")
  @Email(message = "The email has to be a well-formed email address")
  private String email;
  
  @ApiModelProperty(notes = "The address")
  @NotBlank(message = "Address cannot be null nor empty string nor string with only whitespaces")
  private String address;
  
  public UserPostDto(
      @NotBlank(message = "Name cannot be null nor empty string nor string with only whitespaces") String name,
      @NotNull(message = "Date of birth cannot be null") @Past(message = "The date of birth has to be in the past") LocalDate dateOfBirth,
      @NotBlank(message = "Email cannot be null nor empty string nor string with only whitespaces") @Email(message = "The email has to be a well-formed email address") String email,
      @NotBlank(message = "Address cannot be null nor empty string nor string with only whitespaces") String address) {
    this.name = name;
    this.dateOfBirth = dateOfBirth;
    this.email = email;
    this.address = address;
  }
  
  public User convertToUser() {
    return new User(this.name, this.dateOfBirth, this.email, this.address);
  }
}
