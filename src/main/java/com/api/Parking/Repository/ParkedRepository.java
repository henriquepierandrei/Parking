package com.api.Parking.Repository;

import com.api.Parking.Model.ParkedModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ParkedRepository extends JpaRepository<ParkedModel,Long> {
    List<ParkedModel> findByDateTimeArrival(LocalDateTime date);

    Optional<ParkedModel> findByCode(String code);

    Optional<ParkedModel> findByPlace(String place);

    Optional<List<ParkedModel>> findByDate(LocalDate now);
}
