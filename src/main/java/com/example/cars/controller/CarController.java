package com.example.cars.controller;

import com.example.cars.Car;
import com.example.cars.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class CarController {

    private CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/main")
    public String mainPaige(Model model) {
        List<Car> cars = carService.getCars();

        model.addAttribute("cars", cars);

        return "index";
    }

    @GetMapping("/add")
    public String displayForm(Model model) {
        model.addAttribute("car", new Car());
        return "form";
    }

    @PostMapping("/add")
    public String createCar(@Validated @ModelAttribute Car car, BindingResult result) {

        if (result.hasErrors()) {
            return "form";
        }
        carService.addCar(car);

        return "redirect:" + "/main";
    }

    @GetMapping("/edit/{id}")
    public String displayEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("foundCar", carService.getCarById(id));

        return "edit";
    }

    @PostMapping("/edit")
    public String editCar(@ModelAttribute Car car) {
        carService.modifyCar(car.getId(), car);

        return "redirect:" + "/main";
    }

    @GetMapping("/delete/{id}")
    public String deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);

        return "redirect:" + "/main";
    }

    @GetMapping("/find")
    public String displayFindForm(Model model) {
        model.addAttribute("index", 0L);

        return "find";
    }

    @PostMapping("/find")
    public String findById(@RequestParam(value = "input") String input, Model model) {

        try {
            Long id = Long.parseLong(input);
            Optional<Car> carOptional = carService.getCarById(id);

            if (carOptional.isPresent()) {
                model.addAttribute("foundCar", carOptional.get());
                return "find";
            }

            model.addAttribute("notFound", id);
            return "find";

        } catch (NumberFormatException e) {
            model.addAttribute("wrongInput", "");
            return "find";
        }
    }
}
