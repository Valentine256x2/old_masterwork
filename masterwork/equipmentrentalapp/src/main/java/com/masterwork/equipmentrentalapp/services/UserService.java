package com.masterwork.equipmentrentalapp.services;

import com.masterwork.equipmentrentalapp.models.dto.UserGetDto;
import com.masterwork.equipmentrentalapp.models.dto.UserPostDto;
import com.masterwork.equipmentrentalapp.models.dto.UserPutDto;
import com.masterwork.equipmentrentalapp.models.entities.User;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public interface UserService {
  
  List<UserGetDto> getAllUsers();
  
  UserGetDto getUserDtoById(@NotNull @Positive Long id);
  
  User getUserById(@NotNull @Positive Long id);
  
  List<UserGetDto> getUsersByName(@NotBlank String name);
  
  UserGetDto getUserByEmail(@NotBlank @Email String email);
  
  UserGetDto deleteUser(@NotNull @Positive Long id);
  
  UserGetDto createUser(@Valid UserPostDto userPostDto);
  
  UserGetDto updateUser(@NotNull @Positive Long id, UserPutDto userPutDto);
}
