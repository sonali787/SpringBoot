package com.example.AirBnbClone.service;


import com.example.AirBnbClone.dto.HotelDto;
import com.example.AirBnbClone.entity.Hotel;
import com.example.AirBnbClone.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HotelServiceImpl implements HotelService{

    private final HotelRepository hotelRepository;

    public HotelServiceImpl(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @Override
    public Hotel createNewHotel(HotelDto hotelDto) {
        log.info("creating new hotel with name : {} " , hotelDto.getName());
        return null;
    }

    @Override
    public Hotel getHotelById(Long id) {
        return null;
    }
}
