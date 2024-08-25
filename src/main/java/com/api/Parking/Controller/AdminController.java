package com.api.Parking.Controller;

import com.api.Parking.Dto.CreateParkedDto;
import com.api.Parking.Model.ParkedModel;
import com.api.Parking.Service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;


    @GetMapping("/parkeds")
    public ResponseEntity<List<?>> getParkeds(@RequestParam(value = "date") String date) {
        try {
            List<?> parkeds = this.adminService.getParkedByDate(date);
            return ResponseEntity.ok(parkeds);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/create")
    public Object createParked(@RequestBody CreateParkedDto createParkedDto){
        Optional<ParkedModel> optionalParkedModel = this.adminService.getByDateAndPlace(createParkedDto.date(),createParkedDto.place());
        if (optionalParkedModel.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not available!");
        }


        ParkedModel parkedModel = new ParkedModel();
        parkedModel.setDate(createParkedDto.date());
        parkedModel.setArrivalTime(createParkedDto.arrivalTime());
        parkedModel.setPlace(createParkedDto.place().toString());
        parkedModel.setCode(this.adminService.createCode());
        


    }

}
