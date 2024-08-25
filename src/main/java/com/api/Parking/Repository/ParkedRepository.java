package com.api.Parking.Repository;

import com.api.Parking.Model.ParkedModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParkedRepository extends JpaRepository<ParkedModel,Long> {
    List<ParkedModel> findByDate(String date);

    Optional<ParkedModel> findByCode(String code);

    Optional<ParkedModel> findByDateAndPlace(String date, String place);
}
