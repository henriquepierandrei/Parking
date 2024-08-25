package com.api.Parking.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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


    private String code;
}
