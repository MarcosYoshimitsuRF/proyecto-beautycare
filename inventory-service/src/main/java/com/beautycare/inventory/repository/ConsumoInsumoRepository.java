package com.beautycare.inventory.repository;

import com.beautycare.inventory.model.ConsumoInsumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumoInsumoRepository extends JpaRepository<ConsumoInsumo, Long> {
}