package com.example.AirBnbClone.repository;

import com.example.AirBnbClone.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room,Long> {
}
