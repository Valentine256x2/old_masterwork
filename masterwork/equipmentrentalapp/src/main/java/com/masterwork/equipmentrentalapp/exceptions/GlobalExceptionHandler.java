package com.masterwork.equipmentrentalapp.exceptions;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
  
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ExceptionResponse> handleResourceNotFound(ResourceNotFoundException ex) {
    ExceptionResponse response = ExceptionResponse.builder().message(ex.getMessage()).code(404)
                                                  .timestamp(LocalDateTime.now()).build();
    ex.printStackTrace();
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(BadResourceException.class)
  public ResponseEntity<ExceptionResponse> handleBadResource(BadResourceException ex) {
    ExceptionResponse response = ExceptionResponse.builder().message(ex.getMessage()).code(400)
                                                  .timestamp(LocalDateTime.now()).build();
    ex.printStackTrace();
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }
  
  @ExceptionHandler(ResourceAlreadyExistsException.class)
  public ResponseEntity<ExceptionResponse> handleResourceAlreadyExists(
      ResourceAlreadyExistsException ex) {
    ExceptionResponse response = ExceptionResponse.builder().message(ex.getMessage()).code(409)
                                                  .timestamp(LocalDateTime.now()).build();
    ex.printStackTrace();
    return new ResponseEntity<>(response, HttpStatus.CONFLICT);
  }
  
  @ExceptionHandler(org.hibernate.exception.ConstraintViolationException.class)
  public ResponseEntity<ExceptionResponse> handleHibernateConstraintViolations(
      org.hibernate.exception.ConstraintViolationException ex) {
    
    ExceptionResponse response;
    
    String message = ex.getConstraintName();
    
    Optional<String> uniqueConstraintMessage =
        constraintViolationMessages().entrySet().stream().filter(e -> message.contains(e.getKey()))
                                     .findFirst().map(Map.Entry::getValue);
    
    if (uniqueConstraintMessage.isPresent()) {
      response = ExceptionResponse.builder().message("DB Constraint violation").code(400)
                                  .timestamp(LocalDateTime.now())
                                  .detail(uniqueConstraintMessage.get()).build();
    } else {
      response = ExceptionResponse.builder().message("DB Constraint Violation").code(400)
                                  .timestamp(LocalDateTime.now()).detail(ex.getMessage()).build();
    }
    ex.printStackTrace();
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }
  
  private Map<String, String> constraintViolationMessages() {
    
    return Stream.of(new String[][] {
        {"UNIQUENAMEANDADDRESS", "Already existing user with the same Name & Address"},
        {"UNIQUEEMAIL", "User already exists -> Email has been taken"},})
                 .collect(Collectors.toMap(data -> data[0], data -> data[1]));
  }
  
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ExceptionResponse> handleJavaxConstraintViolation(
      ConstraintViolationException ex) {
    ExceptionResponse response;
    List<String> mess = Arrays.asList(ex.getMessage().split(", messageTemplate="));
    response = ExceptionResponse.builder().message("Parameter Constraint Violation").code(400)
                                .timestamp(LocalDateTime.now()).detail(mess.get(mess.size()-1)).build();
    ex.printStackTrace();
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }
  
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {
    ExceptionResponse response;
    
    List<String> n = Arrays.asList(ex.getMessage().split("; "));
    
    response = ExceptionResponse.builder().message("Invalid Argument Given")
                                .code(400)
                                .detail(n.get(n.size() - 1))
                                .timestamp(LocalDateTime.now())
                                .build();
    ex.printStackTrace();
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

}

