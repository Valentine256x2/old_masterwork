package com.masterwork.equipmentrentalapp.repositories;

import com.masterwork.equipmentrentalapp.models.entities.RentalHistory;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalHistoryRepo extends CrudRepository<RentalHistory, Long> {
  List<RentalHistory> findAll();
  
  List<RentalHistory> findAllByUserId(Long user_id);
}
