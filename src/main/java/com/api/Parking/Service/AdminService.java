package com.api.Parking.Service;


import com.api.Parking.Model.ParkedModel;
import com.api.Parking.Repository.ParkedRepository;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class AdminService {
    @Autowired ParkedRepository parkedRepository;


    public List<ParkedModel> getParkedByDate(String date) {
        if (date == null || date.isEmpty()){
            throw new IllegalArgumentException("This date is null or empty!");
        }
        try {
            List<ParkedModel> parkeds = this.parkedRepository.findByDate(date);
            return parkeds != null ? parkeds : Collections.emptyList();
        } catch (Exception e) {
            throw new ServiceException("Erro ao buscar os dados estacionados", e);
        }
    }
}
