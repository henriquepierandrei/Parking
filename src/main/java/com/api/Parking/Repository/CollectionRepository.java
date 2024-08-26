package com.api.Parking.Repository;

import com.api.Parking.Model.CollectionModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface CollectionRepository extends JpaRepository<CollectionModel,Long> {
    CollectionModel findByDate(LocalDate localDate);
}
