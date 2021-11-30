package com.masterwork.equipmentrentalapp.models.entities;

import com.masterwork.equipmentrentalapp.models.Category;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "equipments")
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Equipment {
  
  @ApiModelProperty(value = "primary key for entity equipment", dataType = "Long", notes = "should be automatically generated")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @NotBlank(message = "Name cannot be null nor empty string")
  private String name;
  
  @NotNull(message = "Category cannot be null")
  @Enumerated(EnumType.STRING)
  private Category category;
  
  @NotNull(message = "Daily price cannot be null")
  @Positive(message = "Daily price must be a positive integer")
  private Long dailyPrice;
  
  @ManyToMany(mappedBy = "equipments", fetch = FetchType.LAZY)
  private List<Booking> bookings;
  
  @ManyToMany(mappedBy = "equipments", fetch = FetchType.LAZY)
  private List<RentalHistory> rentalHistory;
  
  public Equipment(@NotBlank(message = "Name cannot be null nor empty string") String name,
                   @NotNull(message = "Category cannot be null") Category category,
                   @NotNull(message = "Daily price cannot be null") @Positive(message = "Daily price must be a positive integer") Long dailyPrice,
                   List<Booking> bookings, List<RentalHistory> rentalHistory) {
    this.name = name;
    this.category = category;
    this.dailyPrice = dailyPrice;
    this.bookings = bookings;
    this.rentalHistory = rentalHistory;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setCategory(Category category) {
    this.category = category;
  }
  
  public void setDailyPrice(Long dailyPrice) {
    this.dailyPrice = dailyPrice;
  }
  
  public void setBookings(List<Booking> bookings) {
    this.bookings = bookings;
  }
  
  public void setRentalHistory(List<RentalHistory> rentalHistory) {
    this.rentalHistory = rentalHistory;
  }
  
  @Override
  public String toString() {
    return "Equipment{" + "id=" + id + ", name='" + name + '\'' + ", category=" + category +
        ", dailyPrice=" + dailyPrice + ", bookings=" +
        bookings.stream().map(Booking::getId).collect(Collectors.toList()) + ", rentalHistory=" +
        rentalHistory.stream().map(RentalHistory::getId).collect(Collectors.toList()) + '}';
  }
}
