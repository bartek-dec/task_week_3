package com.example.cars;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl implements CarService {

    private List<Car> cars;

    public CarServiceImpl() {
        this.cars = new ArrayList<>();

        cars.add(new Car(1L, "Polonez", "Caro", Color.BLACK));
        cars.add(new Car(2L, "Fiat", "125p", Color.BLACK));
        cars.add(new Car(3L, "Alfa Romeo", "Mito", Color.GREEN));
    }

    @Override
    public List<Car> getCars() {
        return cars.stream()
                .sorted(Comparator.comparingLong(Car::getId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Car> getCarById(Long id) {
        return cars.stream()
                .filter(e -> Objects.equals(e.getId(), id))
                .findFirst();
    }

    @Override
    public List<Car> getCarsByColor(String color) {
        return cars.stream()
                .filter(e -> Objects.equals(e.getColor().toString().toLowerCase(), color.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public Car addCar(Car car) {
        Long currentId = getId();
        car.setId(++currentId);
        cars.add(car);

        return car;
    }

    private Long getId() {
        return cars.stream()
                .map(Car::getId)
                .max(Comparator.comparingLong(Long::longValue)).get();
    }

    @Override
    public Optional<Car> updateCar(Car car) {
        Optional<Car> carOptional = cars.stream()
                .filter(e -> Objects.equals(e.getId(), car.getId()))
                .findFirst();

        return carOptional.map(e -> {
            e.setMark(car.getMark());
            e.setModel(car.getModel());
            e.setColor(car.getColor());
            return e;
        });
    }

    @Override
    public Optional<Car> modifyCar(Long id, Car car) {
        Optional<Car> carOptional = cars.stream()
                .filter(e -> Objects.equals(e.getId(), id))
                .findFirst();

        if (carOptional.isPresent()) {
            Car foundCar = carOptional.get();

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

            return Optional.of(foundCar);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Car> deleteCar(Long id) {
        Optional<Car> carOptional = cars.stream()
                .filter(e -> Objects.equals(e.getId(), id))
                .findFirst();

        return carOptional.map(e -> {
            cars.remove(e);
            return Optional.of(e);
        }).orElse(Optional.empty());
    }
}
