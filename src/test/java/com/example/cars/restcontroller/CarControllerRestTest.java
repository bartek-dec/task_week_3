package com.example.cars.restcontroller;

import com.example.cars.Car;
import com.example.cars.CarService;
import com.example.cars.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CarControllerRestTest {

    @Mock
    private CarService carService;

    @InjectMocks
    private CarControllerRest carController;

    private MockMvc mockMvc;

    private List<Car> cars;

    private final String baseUrl = "http://localhost/api/cars";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(carController).build();

        cars = new ArrayList<>();
        cars.add(new Car(1L, "Polonez", "Caro", Color.BLACK));
        cars.add(new Car(2L, "Fiat", "125p", Color.BLACK));
    }

    @Test
    void should_get_all_cars() throws Exception {
        when(carService.getCars()).thenReturn(cars);

        mockMvc.perform(get("/api/cars"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].mark", is("Polonez")))
                .andExpect(jsonPath("$.content[0].model", is("Caro")))
                .andExpect(jsonPath("$.content[0].color", is("BLACK")))
                .andExpect(jsonPath("$.content[0].links[0].href", is(baseUrl + "/1")))
                .andExpect(jsonPath("$.content[1].id", is(2)))
                .andExpect(jsonPath("$.content[1].mark", is("Fiat")))
                .andExpect(jsonPath("$.content[1].model", is("125p")))
                .andExpect(jsonPath("$.content[1].color", is("BLACK")))
                .andExpect(jsonPath("$.content[1].links[0].href", is(baseUrl + "/2")));

        verify(carService, times(1)).getCars();
    }

    @Test
    void should_get_car_by_id() throws Exception {
        when(carService.getCarById(anyLong())).thenReturn(Optional.of(cars.get(0)));

        mockMvc.perform(get("/api/cars/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.mark", is("Polonez")))
                .andExpect(jsonPath("$.model", is("Caro")))
                .andExpect(jsonPath("$.color", is("BLACK")))
                .andExpect(jsonPath("$.links[0].href", is(baseUrl + "/1")));

        verify(carService, times(1)).getCarById(anyLong());
    }

    @Test
    void should_return_NOT_FOUND_when_no_car_of_that_id() throws Exception {
        when(carService.getCarById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/cars/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_get_all_cars_by_color() throws Exception {
        when(carService.getCarsByColor(anyString())).thenReturn(cars);

        mockMvc.perform(get("/api/cars/color/{color}", "BLACK"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].mark", is("Polonez")))
                .andExpect(jsonPath("$.content[0].model", is("Caro")))
                .andExpect(jsonPath("$.content[0].color", is("BLACK")))
                .andExpect(jsonPath("$.content[0].links[0].href", is(baseUrl + "/1")))
                .andExpect(jsonPath("$.content[1].id", is(2)))
                .andExpect(jsonPath("$.content[1].mark", is("Fiat")))
                .andExpect(jsonPath("$.content[1].model", is("125p")))
                .andExpect(jsonPath("$.content[1].color", is("BLACK")))
                .andExpect(jsonPath("$.content[1].links[0].href", is(baseUrl + "/2")));

        verify(carService, times(1)).getCarsByColor(anyString());
    }

    @Test
    void should_return_NOT_FOUND_when_no_car_of_that_color() throws Exception {
        when(carService.getCarsByColor(anyString())).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/cars/color/{color}", "PINK"))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_add_car() throws Exception {
        String exampleJson = "{\n" +
                "    \"mark\": \"Wardburg\",\n" +
                "    \"model\": \"Unknown\",\n" +
                "    \"color\": \"BLUE\"\n" +
                "}";

        Car carToBeAdded = new Car(4L, "Wardburg", "Unknown", Color.BLUE);
        when(carService.addCar(any())).thenReturn(carToBeAdded);

        mockMvc.perform(post("/api/cars")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(exampleJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mark", is("Wardburg")))
                .andExpect(jsonPath("$.model", is("Unknown")))
                .andExpect(jsonPath("$.color", is("BLUE")));

        verify(carService, times(1)).addCar(any());
    }

    @Test
    void should_update_car() throws Exception {
        String exampleJson = "{\n" +
                "    \"id\": 1,\n" +
                "    \"mark\": \"Audi\",\n" +
                "    \"model\": \"A4\",\n" +
                "    \"color\": \"BLACK\"\n" +
                "}";

        Car updatedCar = new Car(1L, "Audi", "A4", Color.BLACK);
        when(carService.updateCar(any())).thenReturn(Optional.of(updatedCar));

        mockMvc.perform(put("/api/cars")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(exampleJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.mark", is("Audi")))
                .andExpect(jsonPath("$.model", is("A4")))
                .andExpect(jsonPath("$.color", is("BLACK")));

        verify(carService, times(1)).updateCar(any());
    }

    @Test
    void should_return_NOT_FOUND_when_cannot_update_car_of_that_id() throws Exception {
        String exampleJson = "{\n" +
                "    \"id\": 1,\n" +
                "    \"mark\": \"Audi\",\n" +
                "    \"model\": \"A4\",\n" +
                "    \"color\": \"BLACK\"\n" +
                "}";

        when(carService.updateCar(any(Car.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/cars")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(exampleJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_modify_car() throws Exception {
        String exampleJson = "{\n" +
                "    \"model\": \"A4\"\n" +
                "}";

        Car modifiedCar = new Car(1L, "Audi", "A4", Color.BLACK);
        when(carService.modifyCar(anyLong(), any())).thenReturn(Optional.of(modifiedCar));

        mockMvc.perform(patch("/api/cars/{id}", 1)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(exampleJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.mark", is("Audi")))
                .andExpect(jsonPath("$.model", is("A4")))
                .andExpect(jsonPath("$.color", is("BLACK")));

        verify(carService, times(1)).modifyCar(anyLong(), any());
    }

    @Test
    void should_return_NOT_FOUND_when_cannot_modify_car_of_that_id() throws Exception {
        String exampleJson = "{\n" +
                "    \"model\": \"A4\"\n" +
                "}";

        when(carService.modifyCar(anyLong(), any(Car.class))).thenReturn(Optional.empty());

        mockMvc.perform(patch("/api/cars/{id}", 1)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(exampleJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_delete_car() throws Exception {
        when(carService.deleteCar(anyLong())).thenReturn(Optional.of(cars.get(0)));

        mockMvc.perform(delete("/api/cars/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.mark", is("Polonez")))
                .andExpect(jsonPath("$.model", is("Caro")))
                .andExpect(jsonPath("$.color", is("BLACK")));

        verify(carService, times(1)).deleteCar(anyLong());
    }

    @Test
    void should_return_NOT_FOUND_when_cannot_delete_car_of_that_id() throws Exception {
        when(carService.deleteCar(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/cars/{id}", 1))
                .andExpect(status().isNotFound());
    }
}
