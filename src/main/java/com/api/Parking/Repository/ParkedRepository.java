package com.api.Parking.Repository;

import com.api.Parking.Model.ParkedModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ParkedRepository extends JpaRepository<ParkedModel,Long> {
    List<ParkedModel> findByDate(String date);
}
