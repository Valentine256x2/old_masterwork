package com.masterwork.equipmentrentalapp.api;

import com.masterwork.equipmentrentalapp.models.dto.BookingGetDto;
import com.masterwork.equipmentrentalapp.models.dto.BookingPostDto;
import com.masterwork.equipmentrentalapp.models.dto.BookingPutDto;
import com.masterwork.equipmentrentalapp.models.dto.EquipmentAvailabilityCheckDto;
import com.masterwork.equipmentrentalapp.models.dto.RentalHistoryGetDto;
import com.masterwork.equipmentrentalapp.services.BookingService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.time.LocalDate;
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
@RequestMapping("/api/bookings")
public class BookingController {
  
  @Autowired
  private BookingService bookingService;
  
  @ApiOperation("View all bookings")
  @GetMapping("")
  public ResponseEntity<List<BookingGetDto>> getAllBookings() {
    return ResponseEntity.ok(bookingService.getAllBookingDtos());
  }
  
  @GetMapping("/booking/{id}")
  @ApiOperation(value = "Returns the booking with the given ID")
  public ResponseEntity<BookingGetDto> getBookingById(
      @ApiParam(value = "ID of the queried booking", required = true, type = "Long") @PathVariable
          Long id) {
    
    return ResponseEntity.ok().body(bookingService.getBookingDtoById(id));
  }
  
  @GetMapping("/bookings-by-user/{id}")
  @ApiOperation(value = "Returns the bookings with the given user ID")
  public ResponseEntity<List<BookingGetDto>> getBookingsByUserId(
      @ApiParam(value = "ID of the user", required = true, type = "Long") @PathVariable Long id) {
    
    return ResponseEntity.ok().body(bookingService.getBookingsByUserId(id));
  }
  
  @GetMapping("/bookings-by-equipment/{id}")
  @ApiOperation(value = "Returns the bookings with the given equipment ID")
  public ResponseEntity<List<BookingGetDto>> getBookingsByEquipmentId(
      @ApiParam(value = "ID of the equipment", required = true, type = "Long") @PathVariable
          Long id) {
    
    return ResponseEntity.ok().body(bookingService.getBookingsByEquipmentId(id));
  }
  
  @GetMapping("/is-available")
  @ApiOperation(value = "Returns a boolean whether the chosen equipment is available or not")
  public ResponseEntity<Boolean> checkIfEquipmentAvailable(
      @ApiParam(value = "The equipment id", required = true, type = "Long")
      @RequestParam("equipmentId") Long equipmentId,
      @ApiParam(value = "The rental date (yyyy-MM-dd)", required = true, type = "String")
      @RequestParam("rentalDate") String rentalDate,
      @ApiParam(value = "The expected return date (yyyy-MM-dd)", required = true, type = "String")
      @RequestParam("expectedReturnDate") String expectedReturnDate) {
    
      EquipmentAvailabilityCheckDto dto =
          new EquipmentAvailabilityCheckDto(equipmentId, LocalDate.parse(rentalDate), LocalDate.parse(expectedReturnDate));
  
      return ResponseEntity.ok().body(bookingService
          .isEquipmentAvailableBetween(dto.getEquipmentId(), dto.getRentalDate(), dto.getExpectedReturnDate()));
  }
  
  @DeleteMapping("/cancel-booking/{id}")
  @ApiOperation(value = "Deletes a booking")
  public ResponseEntity<BookingGetDto> cancelBookingById(
      @ApiParam(value = "ID of the booking to be deleted", required = true, type = "Long")
      @PathVariable Long id) {
    
    return ResponseEntity.ok().body(bookingService.removeBooking(id));
  }
  
  @PutMapping("/{id}")
  @ApiOperation(value = "Updates an existing booking record's return date.")
  public ResponseEntity<BookingGetDto> updateBookingRecordReturnDate(
      
      @ApiParam(value = "ID of the booking record to be updated.", required = true)
      @PathVariable(name = "id") Long id,
      @ApiParam(value = "The updated booking record to be saved.", required = true)
      @RequestBody(required = false) BookingPutDto bookingPutDto) {
    Objects.requireNonNull(bookingPutDto);
    return ResponseEntity.ok().body(bookingService
        .updateBookingReturnDate(id, bookingPutDto.getExpectedReturnDate(),
            bookingPutDto.getRentalDate()));
  }
  
  @PostMapping
  @ApiOperation(value = "Creates a new booking record with multiple equipments.")
  public ResponseEntity<BookingGetDto> createBooking(
      @ApiParam(value = "The booking record to be created.", required = true)
      @RequestBody(required = false) @Valid BookingPostDto bookingPostDto) {
    
    Objects.requireNonNull(bookingPostDto, "The booking cannot be null");
    
    return ResponseEntity.ok().body(bookingService
        .bookEquipments(bookingPostDto.getEquipmentNames(), bookingPostDto.getRentalDate(),
            bookingPostDto.getExpectedReturnDate(), bookingPostDto.getUserId()));
  }
  
  @PutMapping("/return/{id}")
  @ApiOperation(value = "Deletes an existing booking record and creates a rental history record.")
  public ResponseEntity<RentalHistoryGetDto> returnEquipments(
      
      @ApiParam(value = "ID of the booking record to be deleted.", required = true) @PathVariable
          Long id) {
    
    return ResponseEntity.ok().body(bookingService.returnEquipments(id));
  }
}
