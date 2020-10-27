package com.example.cars;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    private List<Car> cars;

    public CarController() {
        this.cars = new ArrayList<>();

        cars.add(new Car(1L, "Polonez", "Caro", Color.BLACK));
        cars.add(new Car(2L, "Fiat", "125p", Color.BLACK));
        cars.add(new Car(3L, "Alfa Romeo", "Mito", Color.GREEN));
    }

    @GetMapping
    public ResponseEntity<List<Car>> getCars() {
        if (cars.size() > 0) {
            return new ResponseEntity<>(cars, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        Optional<Car> carOptional = cars.stream()
                .filter(e -> Objects.equals(e.getId(), id))
                .findFirst();

        if (carOptional.isPresent()) {
            return new ResponseEntity<>(carOptional.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/color/{color}")
    public ResponseEntity<List<Car>> getCarsByColor(@PathVariable String color) {
        List<Car> foundCars = cars.stream()
                .filter(e -> Objects.equals(e.getColor().toString().toLowerCase(), color.toLowerCase()))
                .collect(Collectors.toList());

        if (foundCars.size()>0) {
            return new ResponseEntity<>(foundCars, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
