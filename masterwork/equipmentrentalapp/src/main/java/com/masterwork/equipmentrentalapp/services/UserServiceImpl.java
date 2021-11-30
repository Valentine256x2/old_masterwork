package com.masterwork.equipmentrentalapp.services;

import com.masterwork.equipmentrentalapp.exceptions.BadResourceException;
import com.masterwork.equipmentrentalapp.exceptions.ResourceAlreadyExistsException;
import com.masterwork.equipmentrentalapp.exceptions.ResourceNotFoundException;
import com.masterwork.equipmentrentalapp.models.dto.UserGetDto;
import com.masterwork.equipmentrentalapp.models.dto.UserPostDto;
import com.masterwork.equipmentrentalapp.models.dto.UserPutDto;
import com.masterwork.equipmentrentalapp.models.entities.Booking;
import com.masterwork.equipmentrentalapp.models.entities.User;
import com.masterwork.equipmentrentalapp.repositories.UserRepo;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@Transactional
public class UserServiceImpl implements UserService {
  
  @Autowired
  private UserRepo repo;
  
  @Override
  public List<UserGetDto> getAllUsers() {
    return repo.findAll().stream().map(UserGetDto::convertToDataObject)
               .collect(Collectors.toList());
  }
  
  @Override
  public UserGetDto getUserDtoById(Long id) {
    return UserGetDto.convertToDataObject(getUserById(id));
  }
  
  @Override
  public User getUserById(Long id) {
    return repo.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
  }
  
  @Override
  public List<UserGetDto> getUsersByName(String name) {
    return repo.findAllByName(name).stream().map(UserGetDto::convertToDataObject)
               .collect(Collectors.toList());
  }
  
  @Override
  public UserGetDto getUserByEmail(String email) {
    Optional<User> user = repo.findByEmail(email);
    if (user.isPresent()) {
      return UserGetDto.convertToDataObject(user.get());
    } else {
      throw new ResourceNotFoundException("No user found with the given email: " + email);
    }
  }
  
  @Override
  public UserGetDto deleteUser(Long id) {
    User user = getUserById(id);
    
    for (Booking booking : user.getBookings()) {
      if (booking.getRentalDate().isBefore(LocalDate.now())) {
        throw new BadResourceException(
            "The user cannot be deleted because he/she has ongoing booking");
      }
    }
    repo.deleteById(id);
    return UserGetDto.convertToDataObject(user);
  }
  
  @Override
  public UserGetDto createUser(UserPostDto userPostDto) {
    validateEmail(userPostDto.getEmail());
    return UserGetDto.convertToDataObject(repo.save(userPostDto.convertToUser()));
  }
  
  @Override
  public UserGetDto updateUser(Long id, UserPutDto userPutDto) {
    User user = getUserById(id);
    
    
    if (!Objects.isNull(userPutDto.getName()) && !userPutDto.getName().isEmpty()) {
      user.setName(userPutDto.getName());
    }
    if (!Objects.isNull(userPutDto.getAddress()) && !userPutDto.getAddress().isEmpty()) {
      user.setAddress(userPutDto.getAddress());
    }
    if (!Objects.isNull(userPutDto.getEmail()) && !userPutDto.getEmail().isEmpty()) {
      validateEmail(userPutDto.getEmail());
      user.setEmail(userPutDto.getEmail());
    }
    return UserGetDto.convertToDataObject(repo.save(user));
  }
  
  private void validateEmail(@Email String email) {
    if (repo.findByEmail(email).isPresent()) {
      throw new ResourceAlreadyExistsException("This email has been registered already");
    }
  }
}
