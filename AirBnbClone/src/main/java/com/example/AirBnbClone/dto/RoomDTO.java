package com.example.AirBnbClone.dto;

import com.example.AirBnbClone.entity.Hotel;
import jakarta.persistence.*;

import java.math.BigDecimal;

public class RoomDTO {
    private Long id;
    private String type;
    private BigDecimal basePrice;
    private String[] photos;
    private String[] amenities;
    private Integer totalCount;
    private Integer capacity;
}
