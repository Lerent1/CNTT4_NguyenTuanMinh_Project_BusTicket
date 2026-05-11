package org.example.project_busticket.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "buses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "License Plate không được để trống")
    @Size(min = 5, max = 20,
            message = "License Plate phải từ 5-20 ký tự")
    @Column(unique = true)
    private String licensePlate;

    @NotBlank(message = "Bus Type không được để trống")
    private String busType;

    @NotNull(message = "Seats không được để trống")
    @Min(value = 1, message = "Seats phải lớn hơn 0")
    private Integer totalSeats;

    @NotBlank(message = "Company không được để trống")
    private String company;

    @NotBlank(message = "Driver Name không được để trống")
    private String driverName;
}
