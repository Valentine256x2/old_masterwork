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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "rental_history")
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class RentalHistory {
  
  @ApiModelProperty(value = "primary key for entity rental history", dataType = "Long", notes = "should be automatically generated")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  @NotNull(message = "User cannot be null")
  private User user;
  
  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinTable(name = "equipments_rental_history", joinColumns = {
      @JoinColumn(name = "rental_history_id", referencedColumnName = "id", nullable = false)}, inverseJoinColumns = {
      @JoinColumn(name = "equipment_id", referencedColumnName = "id")})
  @NotNull(message = "Equipments list cannot be null")
  private List<Equipment> equipments;
  
  @Past(message = "The rental date cannot be in the future")
  @NotNull(message = "Rental date cannot be null")
  private LocalDate rentalDate;
  
  @NotNull(message = "Expected return date cannot be null")
  private LocalDate expectedReturnDate;
  
  private LocalDate actualReturnDate;
  
  @NotNull(message = "Price paid cannot be null")
  @Positive(message = "The price paid have to be positive integer")
  private Long pricePaid;
  
  public RentalHistory(@NotNull(message = "User cannot be null") User user,
                       @NotNull(message = "Equipments list cannot be null") List<Equipment> equipments,
                       @Past(message = "The rental date cannot be in the future") @NotNull(message = "Rental date cannot be null") LocalDate rentalDate,
                       @NotNull(message = "Expected return date cannot be null") LocalDate expectedReturnDate,
                       @NotNull(message = "Price paid cannot be null") @Positive(message = "The price paid have to be positive integer") Long pricePaid) {
    this.user = user;
    this.equipments = equipments;
    this.rentalDate = rentalDate;
    this.expectedReturnDate = expectedReturnDate;
    this.pricePaid = pricePaid;
    this.actualReturnDate = LocalDate.now();
  }
  
  @Override
  public String toString() {
    return "RentalHistory{" + "id=" + id + ", user=" + user.getId() + ", equipments=" +
        equipments.stream().map(Equipment::getId).collect(Collectors.toList()) + ", rentalDate=" +
        rentalDate + ", expectedReturnDate=" + expectedReturnDate + ", actualReturnDate=" +
        actualReturnDate + ", pricePaid=" + pricePaid + '}';
  }
}
