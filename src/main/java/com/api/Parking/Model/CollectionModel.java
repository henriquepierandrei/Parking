package com.api.Parking.Model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private double finalValue;

    private String Date;

}
