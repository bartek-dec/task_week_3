package com.example.cars;

import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

public class Car extends RepresentationModel<Car> {

    @Min(value = 1, message = "Value mast be at least 1")
    private Long id;

    @NotBlank(message = "Mark cannot be blank")
    @Size(min = 2, message = "Mark must be at least 2 characters length")
    private String mark;

    @NotBlank(message = "Model cannot be blank")
    @Size(min = 1, message = "Model must be at least 1 character length")
    private String model;

    @NotNull
    private Color color;

    public Car() {
    }

    public Car(Long id, String mark, String model, Color color) {
        this.id = id;
        this.mark = mark;
        this.model = model;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return id.equals(car.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
