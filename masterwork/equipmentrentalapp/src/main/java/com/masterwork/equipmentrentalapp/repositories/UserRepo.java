package com.masterwork.equipmentrentalapp.repositories;

import com.masterwork.equipmentrentalapp.models.entities.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends CrudRepository<User, Long> {
  List<User> findAll();
  
  List<User> findAllByName(String name);
  
  Optional<User> findByEmail(String email);
  
}
