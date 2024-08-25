package com.api.Parking.Model;


import com.api.Parking.Enum.PlaceEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ParkedModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO )
    private long id;


    private String place;

    @ManyToOne
    private CarModel car;


    private String date;
    private String arrivalTime;
    private String exitTime="";


    private String Code;
}
