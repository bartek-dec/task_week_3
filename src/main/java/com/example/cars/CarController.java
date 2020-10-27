package com.example.cars;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    private List<Car> cars;

    public CarController() {
        this.cars = new ArrayList<>();

        cars.add(new Car(1L, "Polonez", "Caro", Color.BLACK));
        cars.add(new Car(2L, "Fiat", "125p", Color.BLUE));
        cars.add(new Car(3L, "Alfa Romeo", "Mito", Color.GREEN));
    }

    @GetMapping
    public ResponseEntity<List<Car>> getCars() {
        if (cars.size() > 0) {
            return new ResponseEntity<>(cars, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
