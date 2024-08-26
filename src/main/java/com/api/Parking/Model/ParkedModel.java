package com.api.Parking.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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


    private LocalDateTime dateTimeArrival;


    private LocalDateTime dateTimeExit;



    private String code;
}
