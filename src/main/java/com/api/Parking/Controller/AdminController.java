package com.api.Parking.Controller;

import com.api.Parking.Service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;


    @GetMapping("parkeds")
    public ResponseEntity<List<?>> getParkeds(@RequestParam(value = "date") String date){
        return ResponseEntity.status(HttpStatus.FOUND).body(this.adminService.getParkedByDate(date));
    }
}
