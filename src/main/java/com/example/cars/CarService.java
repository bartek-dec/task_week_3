package com.example.cars;

import java.util.List;
import java.util.Optional;

public interface CarService {
    List<Car> getCars();

    Optional<Car> getCarById(Long id);

    List<Car> getCarsByColor(String color);

    Car addCar(Car car);

    Optional<Car> updateCar(Car car);

    Optional<Car> modifyCar(Long id, Car car);

    Optional<Car> deleteCar(Long id);
}
