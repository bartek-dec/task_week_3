package com.example.cars;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Car>> getCars() {
        if (cars.size() > 0) {
            return new ResponseEntity<>(cars, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        Optional<Car> carOptional = cars.stream()
                .filter(e -> Objects.equals(e.getId(), id))
                .findFirst();

        if (carOptional.isPresent()) {
            return new ResponseEntity<>(carOptional.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/color/{color}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<List<Car>> getCarsByColor(@PathVariable String color) {
        List<Car> foundCars = cars.stream()
                .filter(e -> Objects.equals(e.getColor().toString().toLowerCase(), color.toLowerCase()))
                .collect(Collectors.toList());

        if (foundCars.size() > 0) {
            return new ResponseEntity<>(foundCars, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Car> addCar(@RequestBody Car car) {
        Optional<Car> carOptional = cars.stream()
                .filter(e -> Objects.equals(e.getId(), car.getId()))
                .findFirst();

        if (carOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        cars.add(car);

        return new ResponseEntity<>(car, HttpStatus.CREATED);
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Car> updateCar(@RequestBody Car car) {
        Optional<Car> carOptional = cars.stream()
                .filter(e -> Objects.equals(e.getId(), car.getId()))
                .findFirst();

        if (carOptional.isPresent()) {
            cars.remove(carOptional.get());
            cars.add(car);

            return new ResponseEntity<>(car, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Car> modifyCar(@PathVariable Long id, @RequestBody Car car) {
        Optional<Car> carOptional = cars.stream()
                .filter(e -> Objects.equals(e.getId(), id))
                .findFirst();

        if (carOptional.isPresent()) {
            Car foundCar = carOptional.get();
            cars.remove(foundCar);

            if (car.getId() != null) {
                foundCar.setId(car.getId());
            }

            if (car.getMark() != null) {
                foundCar.setMark(car.getMark());
            }

            if (car.getModel() != null) {
                foundCar.setModel(car.getModel());
            }

            if (car.getColor() != null) {
                foundCar.setColor(car.getColor());
            }

            cars.add(foundCar);
            return new ResponseEntity<>(foundCar, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Car> deleteCar(@PathVariable Long id) {
        Optional<Car> carOptional = cars.stream()
                .filter(e -> Objects.equals(e.getId(), id))
                .findFirst();

        if (carOptional.isPresent()) {
            cars.remove(carOptional.get());

            return new ResponseEntity<>(carOptional.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
