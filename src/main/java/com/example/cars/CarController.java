package com.example.cars;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    private CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<CollectionModel<Car>> getCars() {
        List<Car> foundCars = carService.getCars();

        foundCars.forEach(car -> car.addIf(!car.hasLinks(), () -> linkTo(CarController.class).slash(car.getId()).withSelfRel()));
        Link link = linkTo(CarController.class).withSelfRel();
        CollectionModel<Car> resources = CollectionModel.of(foundCars, link);

        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        Link link = linkTo(CarController.class).slash(id).withSelfRel();

        return carService.getCarById(id).map(car -> new ResponseEntity<>(car.addIf(!car.hasLinks(), () -> link), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @GetMapping(value = "/color/{color}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<CollectionModel<Car>> getCarsByColor(@PathVariable String color) {
        List<Car> foundCars = carService.getCarsByColor(color);

        foundCars.forEach(car -> car.addIf(!car.hasLinks(), () -> linkTo(CarController.class).slash(car.getId()).withSelfRel()));
        foundCars.forEach(car -> car.addIf(!car.hasLinks(), () -> linkTo(CarController.class).withRel("allColors")));
        Link link = linkTo(CarController.class).slash("color").slash(color).withSelfRel();
        CollectionModel<Car> resources = CollectionModel.of(foundCars, link);

        return foundCars.size() > 0 ? new ResponseEntity<>(resources, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Car> addCar(@Validated @RequestBody Car car, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        carService.addCar(car);
        Link link = linkTo(CarController.class).slash(car.getId()).withSelfRel();

        return new ResponseEntity<>(car.addIf(!car.hasLinks(), () -> link), HttpStatus.CREATED);
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Car> updateCar(@RequestBody Car car) {
        Optional<Car> carOptional = carService.updateCar(car);

        if (carOptional.isPresent()) {
            Link link = linkTo(CarController.class).slash(car.getId()).withSelfRel();

            return new ResponseEntity<>(car.addIf(!car.hasLinks(), () -> link), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Car> modifyCar(@PathVariable Long id, @RequestBody Car car) {
        Optional<Car> carOptional = carService.modifyCar(id, car);

        if (carOptional.isPresent()) {
            Car modifiedCar = carOptional.get();
            Link link = linkTo(CarController.class).slash(id).withSelfRel();

            return new ResponseEntity<>(modifiedCar.addIf(!modifiedCar.hasLinks(), () -> link), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Car> deleteCar(@PathVariable Long id) {

        return carService.deleteCar(id).map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
