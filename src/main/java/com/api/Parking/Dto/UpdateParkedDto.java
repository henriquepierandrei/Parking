package com.api.Parking.Dto;

import com.api.Parking.Model.CarModel;

public record UpdateParkedDto(String place, CarModel model) {
}
