package com.masterwork.equipmentrentalapp.api;

import com.masterwork.equipmentrentalapp.models.dto.EquipmentGetDto;
import com.masterwork.equipmentrentalapp.models.dto.EquipmentPostDto;
import com.masterwork.equipmentrentalapp.models.dto.EquipmentPutDto;
import com.masterwork.equipmentrentalapp.services.EquipmentService;
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
@RequestMapping("/api/equipments")
public class EquipmentController {
  
  @Autowired
  private EquipmentService equipmentService;
  
  @ApiOperation("View all equipments")
  @GetMapping("")
  public ResponseEntity<List<EquipmentGetDto>> getAllEquipments() {
    return ResponseEntity.ok(equipmentService.getAllEquipmentDtos());
  }
  
  @GetMapping("/equipment/{id}")
  @ApiOperation(value = "Returns the equipment with the given ID")
  public ResponseEntity<EquipmentGetDto> getEquipmentById(
      @ApiParam(value = "ID of the queried equipment", required = true, type = "Long") @PathVariable
          Long id) {
    
    return ResponseEntity.ok().body(equipmentService.getEquipmentDtoById(id));
  }
  
  @GetMapping("/equipments-by-name")
  @ApiOperation(value = "Returns all equipments with the given name")
  public ResponseEntity<List<EquipmentGetDto>> getEquipmentsByName(
      @ApiParam(value = "Name of the equipment", required = true, type = "String")
      @RequestParam(required = false, value = "equipment") String equipment) {
    
    return ResponseEntity.ok().body(equipmentService.getEquipmentsByName(equipment));
  }
  
  @GetMapping("/equipments-by-category")
  @ApiOperation(value = "Returns all equipments with the given category")
  public ResponseEntity<List<EquipmentGetDto>> getEquipmentsByCategory(
      @ApiParam(value = "Category of the equipment", required = true, type = "String")
      @RequestParam(required = false) String equipmentCategory) {
    
    return ResponseEntity.ok().body(equipmentService.getEquipmentsByCategory(equipmentCategory));
  }
  
  @DeleteMapping("/equipment/{id}")
  @ApiOperation(value = "Deletes an equipment")
  public ResponseEntity<EquipmentGetDto> deleteEquipmentById(
      @ApiParam(value = "ID of the equipment to be deleted", required = true, type = "Long")
      @PathVariable Long id) {
    
    return ResponseEntity.ok().body(equipmentService.removeEquipment(id));
  }
  
  @PutMapping("/{id}")
  @ApiOperation(value = "Updates an existing equipment.")
  public ResponseEntity<EquipmentGetDto> updateEquipment(
      
      @ApiParam(value = "ID of the equipment to be updated.", required = true) @PathVariable
          Long id,
      
      @ApiParam(value = "The updated equipment to be saved.", required = true)
      @RequestBody(required = false) EquipmentPutDto equipment) {
    Objects.requireNonNull(equipment);
    return ResponseEntity.ok().body(equipmentService.updateEquipment(id, equipment));
  }
  
  @PostMapping
  @ApiOperation(value = "Creates a new equipment.")
  public ResponseEntity<EquipmentGetDto> createEquipment(
      @ApiParam(value = "The equipment to be created.", required = true)
      @RequestBody(required = false) @Valid EquipmentPostDto equipmentPostDto) {
    Objects.requireNonNull(equipmentPostDto);
    return ResponseEntity.ok().body(equipmentService.createEquipment(equipmentPostDto));
  }
}
