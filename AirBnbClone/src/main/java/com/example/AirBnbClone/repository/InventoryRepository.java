package com.example.AirBnbClone.repository;

import com.example.AirBnbClone.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory , Long> {

}
