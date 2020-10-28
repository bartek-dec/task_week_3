package com.example.cars;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
        return cars;
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
        cars.add(car);

        return car;
    }

    @Override
    public Optional<Car> updateCar(Car car) {
        Optional<Car> carOptional = cars.stream()
                .filter(e -> Objects.equals(e.getId(), car.getId()))
                .findFirst();

        return carOptional.map(e -> {
            cars.remove(e);
            cars.add(car);
            return Optional.of(car);
        }).orElse(Optional.empty());
    }

    @Override
    public Optional<Car> modifyCar(Long id, Car car) {
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
