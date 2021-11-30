package com.masterwork.equipmentrentalapp.models;

import com.masterwork.equipmentrentalapp.exceptions.BadResourceException;

public enum Category {
  HAND_TOOLS,
  POWER_TOOLS,
  LADDERS_AND_SACK_TRUCKS,
  CLEANING_AND_DRAINAGE,
  ELECTRICAL,
  GARDEN_TOOLS_AND_WATERING,
  AIR_TOOLS_AND_COMPRESSORS,
  SAFETY_AND_DETECTORS,
  FANS_AND_HEATERS;
  
  public static Category parseToEquipmentCategory(String inputCategory) {
    try {
      return valueOf(inputCategory);
    } catch (IllegalArgumentException e) {
      throw new BadResourceException("The given category does not exist");
    }
  }
  
}
