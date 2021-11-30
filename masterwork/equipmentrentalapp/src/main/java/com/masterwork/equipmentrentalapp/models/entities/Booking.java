package com.masterwork.equipmentrentalapp.models.entities;

import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bookings")
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class Booking {
  
  @ApiModelProperty(value = "primary key for entity booking", dataType = "Long", notes = "should be automatically generated")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  @NotNull(message = "User cannot be null")
  private User user;
  
  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinTable(name = "equipments_bookings", joinColumns = {
      @JoinColumn(name = "booking_id", referencedColumnName = "id", nullable = false)}, inverseJoinColumns = {
      @JoinColumn(name = "equipment_id", referencedColumnName = "id", nullable = false)})
  @NotNull(message = "Equipments list cannot be null")
  private List<@NotNull Equipment> equipments;
  
  @NotNull(message = "Expected return date cannot be null")
  @Future(message = "Expected return date cannot be in the future")
  private LocalDate expectedReturnDate;
  
  @NotNull(message = "Rental date cannot be null")
  private LocalDate rentalDate;
  
  public Booking(@NotNull(message = "User cannot be null") User user,
                 @NotNull(message = "Equipments list cannot be null") List<@NotNull Equipment> equipments,
                 @NotNull(message = "Expected return date cannot be null") @Future(message = "Expected return date cannot be in the future") LocalDate expectedReturnDate,
                 @NotNull(message = "Rental date cannot be null") LocalDate rentalDate) {
    this.user = user;
    this.equipments = equipments;
    this.expectedReturnDate = expectedReturnDate;
    this.rentalDate = rentalDate;
  }
  
  @Override
  public String toString() {
    return "Booking{" + "id=" + id + ", user=" + user.getId() + ", equipments=" +
        equipments.stream().map(Equipment::getId).collect(Collectors.toList()) +
        ", expectedReturnDate=" + expectedReturnDate + ", rentalDate=" + rentalDate + '}';
  }
  
  public void setUser(User user) {
    this.user = user;
  }
  
  public void setEquipments(List<Equipment> equipments) {
    this.equipments = equipments;
  }
  
  public void setExpectedReturnDate(LocalDate expectedReturnDate) {
    this.expectedReturnDate = expectedReturnDate;
  }
  
  public void setRentalDate(LocalDate rentalDate) {
    this.rentalDate = rentalDate;
  }
}

