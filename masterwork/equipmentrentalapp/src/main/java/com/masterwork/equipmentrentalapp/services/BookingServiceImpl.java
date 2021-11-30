package com.masterwork.equipmentrentalapp.services;

import com.masterwork.equipmentrentalapp.exceptions.BadResourceException;
import com.masterwork.equipmentrentalapp.exceptions.ResourceNotFoundException;
import com.masterwork.equipmentrentalapp.models.dto.BookingGetDto;
import com.masterwork.equipmentrentalapp.models.dto.RentalHistoryGetDto;
import com.masterwork.equipmentrentalapp.models.entities.Booking;
import com.masterwork.equipmentrentalapp.models.entities.Equipment;
import com.masterwork.equipmentrentalapp.repositories.BookingRepo;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@Transactional
public class BookingServiceImpl implements BookingService {
  
  @Autowired
  private BookingRepo repo;
  
  @Autowired
  private UserService userService;
  
  @Autowired
  private EquipmentService equipmentService;
  
  @Autowired
  private ValidatingService validator;
  
  @Autowired
  private RentalHistoryService rentalHistoryService;
  
  private List<Booking> getAllBookings() {
    return repo.findAll();
  }
  
  @Override
  public List<BookingGetDto> getAllBookingDtos() {
    return getAllBookings().stream().map(BookingGetDto::convertToDataObject)
                           .collect(Collectors.toList());
  }
  
  @Override
  public BookingGetDto getBookingDtoById(Long id) {
    return BookingGetDto.convertToDataObject(getBookingById(id));
  }
  
  private Booking getBookingById(@NotNull @Positive Long id) {
    return repo.findById(id).orElseThrow(
        () -> new ResourceNotFoundException("Booking not found with id: " + id));
  }
  
  private BookingGetDto createBooking(@Valid Booking booking) {
    return BookingGetDto.convertToDataObject(repo.save(booking));
  }
  
  @Override
  public BookingGetDto updateBookingReturnDate(Long id, LocalDate expectedReturnDate,
                                               LocalDate rentalDate) {
    Booking booking = getBookingById(id);
    if (!Objects.isNull(rentalDate) && Objects.isNull(expectedReturnDate)) {
      if (booking.getRentalDate().isBefore(LocalDate.now())){
        throw new BadResourceException("Ongoing bookings rental date cannot be changed");
      }
      if (canBeLengthen(booking.getExpectedReturnDate(), rentalDate, booking)) {
        booking.setRentalDate(rentalDate);
      } else {
        throw new BadResourceException(
            "One or more equipments are not available on the required rental date");
      }
      
    } else if (Objects.isNull(rentalDate) && !Objects.isNull(expectedReturnDate)) {
      if (canBeLengthen(expectedReturnDate, LocalDate.now(), booking)) {
        booking.setExpectedReturnDate(expectedReturnDate);
      } else {
        throw new BadResourceException(
            "One or more equipments are not available on the required return date");
      }
      
    } else if (Objects.isNull(rentalDate)) {
      throw new BadResourceException("Rental date and return date cannot be null at the same time");
      
    } else {
      if (booking.getRentalDate().isBefore(LocalDate.now())){
        throw new BadResourceException("Ongoing bookings rental date cannot be changed");
      }
      if (canBeLengthen(expectedReturnDate, rentalDate, booking)) {
        booking.setExpectedReturnDate(expectedReturnDate);
        booking.setRentalDate(rentalDate);
      } else {
        throw new BadResourceException(
            "One or more equipments are not available on the required dates");
      }
    }
    return BookingGetDto.convertToDataObject(repo.save(booking));
  }
  
  private boolean canBeLengthen(LocalDate expectedReturnDate, LocalDate rentalDate,
                                Booking booking) {
    validator.validateRentalDates(rentalDate, expectedReturnDate);
    
    AtomicBoolean canBeChanged = new AtomicBoolean(true);
    
    booking.getEquipments().stream().forEach(equipment -> {
      if (!isEquipmentAvailableBetweenForLengthening(booking.getId(), equipment.getId(), rentalDate, expectedReturnDate)) {
        canBeChanged.set(false);
      }
    });
    return canBeChanged.get();
  }
  
  @Override
  public BookingGetDto removeBooking(Long id) {
    BookingGetDto bookingDto = getBookingDtoById(id);
    if (bookingDto.getRentalDate().isBefore(LocalDate.now())) {
      throw new BadResourceException(
          "Booking cannot be deleted because it is ongoing at the moment");
    }
    repo.deleteById(id);
    return bookingDto;
  }
  
  @Override
  public List<BookingGetDto> getBookingsByUserId(Long userId) {
    return repo.findBookingsByUserId(userId).stream()
                      .map(BookingGetDto::convertToDataObject).collect(Collectors.toList());
  }
  
  @Override
  public List<BookingGetDto> getBookingsByEquipmentId(Long equipmentId) {
    return repo.findAll().stream().filter(booking -> booking.getEquipments().stream().anyMatch(
        equipment -> equipment.getId().equals(equipmentId))).map(BookingGetDto::convertToDataObject)
               .collect(Collectors.toList());
  }
  
  @Override
  public BookingGetDto bookEquipments(List<String> equipmentNames, LocalDate rentalDate,
                                      LocalDate expectedReturnDate, Long userId) {
    validator.validateRentalDates(rentalDate, expectedReturnDate);
    validator.validateUserExistence(userId);
    
    List<Equipment> allEquipments = equipmentService.getAllEquipments();
    List<Equipment> equipmentBeingBooked = new ArrayList<>();
    List<String> equipmentsCannotBeBookedBecauseNotOnStock = new ArrayList<>();
    List<String> equipmentsCannotBeBookedBecauseNoneAvailableOnTheGivenPeriod = new ArrayList<>();
    
    equipmentNames.forEach(equipmentName -> {
      boolean equipmentOnStock = false;
      boolean foundABookableEquipment = false;
      for (int i = 0; i < allEquipments.size() && !foundABookableEquipment; i++) {
        if (!equipmentBeingBooked.contains(allEquipments.get(i)) &&
            allEquipments.get(i).getName().equals(equipmentName)) {
          equipmentOnStock = true;
          if (isEquipmentAvailableBetween(allEquipments.get(i).getId(), rentalDate,
              expectedReturnDate)) {
            equipmentBeingBooked.add(allEquipments.get(i));
            foundABookableEquipment = true;
          }
        }
      }
      if (equipmentOnStock && !foundABookableEquipment) {
        equipmentsCannotBeBookedBecauseNoneAvailableOnTheGivenPeriod.add(equipmentName);
      } else if (!equipmentOnStock) {
        equipmentsCannotBeBookedBecauseNotOnStock.add(equipmentName);
      }
    });
    
    if (equipmentBeingBooked.size() != equipmentNames.size()) {
      StringBuilder finalSb = new StringBuilder();
      equipmentsCannotBeBookedBecauseNotOnStock
          .forEach(equipment -> finalSb.append(equipment).append(", "));
      String notOnStock = finalSb.toString();
      
      StringBuilder finalSb1 = new StringBuilder();
      equipmentsCannotBeBookedBecauseNoneAvailableOnTheGivenPeriod
          .forEach(equipment -> finalSb1.append(equipment).append(", "));
      String notAvailable = finalSb1.toString();
      throw new BadResourceException(
          "The following equipments can't be booked due to not on stock: " + notOnStock +
              " and the following equipments are not available on the given dates: " + notAvailable);
    }
    return createBooking(
        new Booking(userService.getUserById(userId), equipmentBeingBooked, expectedReturnDate,
            rentalDate));
    
  }
  
  @Override
  public boolean isEquipmentAvailableBetween(Long equipmentId, LocalDate rentalDate,
                                             LocalDate expectedReturnDate) {
    validator.validateEquipmentExistence(equipmentId);
    validator.validateRentalDates(rentalDate, expectedReturnDate);
    
    Equipment equipment = equipmentService.getEquipmentById(equipmentId);
    
    boolean isAvailable = true;
    for (Booking booking : equipment.getBookings()) {
      if (expectedReturnDate.isAfter(booking.getRentalDate()) &&
          rentalDate.isBefore(booking.getExpectedReturnDate()) ||
          rentalDate.isEqual(booking.getExpectedReturnDate())) {
        isAvailable = false;
      }
      if (rentalDate.isBefore(booking.getExpectedReturnDate()) &&
          expectedReturnDate.isAfter(booking.getRentalDate()) ||
          expectedReturnDate.isEqual(booking.getRentalDate())) {
        isAvailable = false;
      }
    }
    return isAvailable;
  }
  
  @Override
  public RentalHistoryGetDto returnEquipments(Long bookingId) {
    Long priceToBePaid = 0L;
    Booking booking = getBookingById(bookingId);
    Long days = LocalDate.now().toEpochDay() - booking.getRentalDate().toEpochDay();
    
    if (days > 0) {
      for (Equipment equipment : booking.getEquipments()) {
        priceToBePaid += equipment.getDailyPrice() * (days + 1);
      }
    } else {
      throw new BadResourceException("The booking has not started yet. You need to cancel it.");
    }
    repo.deleteById(bookingId);
    return rentalHistoryService
        .createRentalHistory(booking.getUser(), booking.getEquipments(), booking.getRentalDate(),
            booking.getExpectedReturnDate(), priceToBePaid);
  }
  
  private boolean isEquipmentAvailableBetweenForLengthening(Long bookingId, Long equipmentId, LocalDate rentalDate,
                                             LocalDate expectedReturnDate) {
    
    Equipment equipment = equipmentService.getEquipmentById(equipmentId);
    
    boolean isAvailable = true;
    for (Booking booking : equipment.getBookings()) {
      if (!booking.getId().equals(bookingId)) {
        if (expectedReturnDate.isAfter(booking.getRentalDate()) && rentalDate.isBefore(booking.getExpectedReturnDate()) ||
            rentalDate.isEqual(booking.getExpectedReturnDate())) {
          isAvailable = false;
        }
        if (rentalDate.isBefore(booking.getExpectedReturnDate()) && expectedReturnDate.isAfter(booking.getRentalDate()) ||
            expectedReturnDate.isEqual(booking.getRentalDate())) {
          isAvailable = false;
        }
      }
    }
    return isAvailable;
  }
}
