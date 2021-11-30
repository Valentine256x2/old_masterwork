package com.masterwork.equipmentrentalapp.repositories;

import com.masterwork.equipmentrentalapp.models.Category;
import com.masterwork.equipmentrentalapp.models.entities.Equipment;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentRepo extends CrudRepository<Equipment, Long> {
  List<Equipment> findAll();
  
  List<Equipment> findAllByName(String name);
  
  List<Equipment> findAllByCategory(Category category);
}
