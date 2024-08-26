package com.api.Parking.Repository;

import com.api.Parking.Model.CollectionModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionRepository extends JpaRepository<CollectionModel,Long> {
}
