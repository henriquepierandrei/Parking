package com.api.Parking.Service;


import com.api.Parking.Model.ParkedModel;
import com.api.Parking.Repository.ParkedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AdminService {
    @Autowired ParkedRepository parkedRepository;


    public List<ParkedModel> getParkedByDate(String date) {
        return new ArrayList<>(this.parkedRepository.findByDate(date));
    }
}
