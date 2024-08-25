package com.api.Parking.Repository;

import com.api.Parking.Model.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<CarModel,Long> {
}
