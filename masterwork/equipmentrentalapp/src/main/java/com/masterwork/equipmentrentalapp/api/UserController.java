package com.masterwork.equipmentrentalapp.api;

import com.masterwork.equipmentrentalapp.models.dto.UserGetDto;
import com.masterwork.equipmentrentalapp.models.dto.UserPostDto;
import com.masterwork.equipmentrentalapp.models.dto.UserPutDto;
import com.masterwork.equipmentrentalapp.services.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
  
  @Autowired
  private UserService userService;
  
  @ApiOperation("View all users")
  @GetMapping("")
  public ResponseEntity<List<UserGetDto>> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUsers());
  }
  
  @GetMapping("/user/{id}")
  @ApiOperation(value = "Returns the user with the given ID")
  public ResponseEntity<UserGetDto> getUserById(
      @ApiParam(value = "ID of the queried user", required = true, type = "Long") @PathVariable
          Long id) {
    
    return ResponseEntity.ok().body(userService.getUserDtoById(id));
  }
  
  @GetMapping("/users-by-name")
  @ApiOperation(value = "Returns all users with the given name")
  public ResponseEntity<List<UserGetDto>> getUsersByName(
      @ApiParam(value = "Name of the user", required = true, type = "String")
      @RequestParam(required = false) String userName) {
    
    return ResponseEntity.ok().body(userService.getUsersByName(userName));
  }
  
  @GetMapping("/users-by-email")
  @ApiOperation(value = "Returns the user with the given email")
  public ResponseEntity<UserGetDto> getUserByEmail(
      @ApiParam(value = "Email of the user", required = true, type = "String")
      @RequestParam(required = false) String email) {
    
    return ResponseEntity.ok().body(userService.getUserByEmail(email));
  }
  
  @DeleteMapping("/user/{id}")
  @ApiOperation(value = "Deletes a user")
  public ResponseEntity<UserGetDto> deleteUserById(
      @ApiParam(value = "ID of the user to be deleted", required = true, type = "Long")
      @PathVariable Long id) {
    
    return ResponseEntity.ok().body(userService.deleteUser(id));
  }
  
  @PutMapping("/{id}")
  @ApiOperation(value = "Updates an existing user.")
  public ResponseEntity<UserGetDto> updateUser(
      
      @ApiParam(value = "ID of the user to be updated.", required = true) @PathVariable Long id,
      
      @ApiParam(value = "The updated user to be saved.", required = true)
      @RequestBody(required = false) UserPutDto userPutDto) {
    Objects.requireNonNull(userPutDto);
    return ResponseEntity.ok().body(userService.updateUser(id, userPutDto));
  }
  
  @PostMapping
  @ApiOperation(value = "Creates a new user.")
  public ResponseEntity<UserGetDto> createUser(
      @ApiParam(value = "The user to be created.", required = true) @RequestBody(required = false)
      @Valid UserPostDto userPostDto) {
    Objects.requireNonNull(userPostDto);
    return ResponseEntity.ok().body(userService.createUser(userPostDto));
  }
}
