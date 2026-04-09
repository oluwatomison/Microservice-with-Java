package com.learningTutorial.inventory_service.repository;

import com.learningTutorial.inventory_service.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

//JPA Repository takes the type of Object and the type of ID where ID is the primary key
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findBySkuCodeIn(List<String> skuCodes);
}
