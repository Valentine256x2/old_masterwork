package com.masterwork.equipmentrentalapp.api;

import com.masterwork.equipmentrentalapp.models.dto.RentalHistoryGetDto;
import com.masterwork.equipmentrentalapp.services.RentalHistoryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rental-history")
public class RentalHistoryController {
  
  @Autowired
  private RentalHistoryService rentalHistoryService;
  
  @ApiOperation("View all rental history")
  @GetMapping("")
  public ResponseEntity<List<RentalHistoryGetDto>> getAllRentalHistories() {
    return ResponseEntity.ok(rentalHistoryService.getAllRentalHistory());
  }
  
  @GetMapping("/records/{id}")
  @ApiOperation(value = "Returns the rental history record with the given ID")
  public ResponseEntity<RentalHistoryGetDto> getRentalHistoryById(
      @ApiParam(value = "ID of the queried rental history record", required = true, type = "Long")
      @PathVariable Long id) {
    
    return ResponseEntity.ok().body(rentalHistoryService.getRentalHistoryById(id));
  }
  
  @GetMapping("/records-by-user/{id}")
  @ApiOperation(value = "Returns the rental history record with the given user ID")
  public ResponseEntity<List<RentalHistoryGetDto>> getRentalHistoriesByUserId(
      @ApiParam(value = "ID of the user", required = true, type = "Long") @PathVariable Long id) {
    
    return ResponseEntity.ok().body(rentalHistoryService.getRentalHistoryByUserId(id));
  }
  
  @GetMapping("/records-by-equipment/{id}")
  @ApiOperation(value = "Returns the rental history record with the given equipment ID")
  public ResponseEntity<List<RentalHistoryGetDto>> getRentalHistoriesByEquipmentId(
      @ApiParam(value = "ID of the equipment", required = true, type = "Long") @PathVariable
          Long id) {
    
    return ResponseEntity.ok().body(rentalHistoryService.getRentalHistoryByEquipmentId(id));
  }
}
