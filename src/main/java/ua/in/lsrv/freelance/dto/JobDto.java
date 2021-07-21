package ua.in.lsrv.freelance.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Data
public class JobDto {
    private long id;
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @PositiveOrZero
    private double price;
}
