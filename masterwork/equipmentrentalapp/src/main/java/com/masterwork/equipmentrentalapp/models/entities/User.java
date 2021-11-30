package com.masterwork.equipmentrentalapp.models.entities;

import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(name = "UniqueNameAndAddress", columnNames = {"name", "address"})})
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class User {
  
  @ApiModelProperty(value = "primary key for entity user", dataType = "Long", notes = "should be automatically generated")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @NotBlank(message = "Name cannot be null nor empty string nor string containing only white spaces")
  private String name;
  
  @Past(message = "The date of birth has to be in the past")
  @NotNull(message = "Date of birth cannot be null")
  private LocalDate dateOfBirth;
  
  @NotBlank(message = "Email cannot be null nor empty string nor string containing only white spaces")
  @Email(message = "The email has to be a well-formed email address")
  @Column(unique = true)
  private String email;
  
  @NotBlank(message = "Address cannot be null nor empty string nor string containing only white spaces")
  private String address;
  
  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<Booking> bookings;
  
  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<RentalHistory> rentalHistory;
  
  public User(
      @NotBlank(message = "Name cannot be null nor empty string nor string containing only white spaces") String name,
      @Past(message = "The date of birth has to be in the past") @NotNull(message = "Date of birth cannot be null") LocalDate dateOfBirth,
      @NotBlank(message = "Email cannot be null nor empty string nor string containing only white spaces") @Email(message = "The email has to be a well-formed email address") String email,
      @NotBlank(message = "Address cannot be null nor empty string nor string containing only white spaces") String address) {
    this.name = name;
    this.dateOfBirth = dateOfBirth;
    this.email = email;
    this.address = address;
    this.bookings = new ArrayList<>();
    this.rentalHistory = new ArrayList<>();
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setEmail(String email) {
    this.email = email;
  }
  
  public void setAddress(String address) {
    this.address = address;
  }
  
  public void setBookings(List<Booking> bookings) {
    this.bookings = bookings;
  }
  
  public void setRentalHistory(List<RentalHistory> rentalHistory) {
    this.rentalHistory = rentalHistory;
  }
  
  @Override
  public String toString() {
    return "User{" + "id=" + id + ", name='" + name + '\'' + ", dateOfBirth=" + dateOfBirth +
        ", email='" + email + '\'' + ", address='" + address + '\'' + ", bookings=" +
        bookings.stream().map(Booking::getId).collect(Collectors.toList()) + ", rentalHistory=" +
        rentalHistory.stream().map(RentalHistory::getId).collect(Collectors.toList()) + '}';
  }
}


