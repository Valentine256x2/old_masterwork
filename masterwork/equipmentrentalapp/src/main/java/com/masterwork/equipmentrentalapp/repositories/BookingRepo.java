package com.masterwork.equipmentrentalapp.repositories;

import com.masterwork.equipmentrentalapp.models.entities.Booking;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepo extends CrudRepository<Booking, Long> {
  List<Booking> findAll();
  
  List<Booking> findBookingsByUserId(Long user_id);
}
