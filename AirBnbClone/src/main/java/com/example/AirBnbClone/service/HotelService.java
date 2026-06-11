package com.example.AirBnbClone.service;

import com.example.AirBnbClone.dto.HotelDto;
import com.example.AirBnbClone.entity.Hotel;

public interface HotelService {

    HotelDto createNewHotel(HotelDto hotelDto);
    HotelDto getHotelById(Long id);
}
