package com.rewe.customerbookingservice.dtos;

import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class BookingDTO {
    private Long id;
    @Size(min = 3, max = 100, message = "title must be between 3 and 100 characters")
    private String title;
    private String description;
    private boolean active;
    private LocalDateTime created;
    private LocalDateTime updated;
    private LocalDate startDate;
    private LocalDate endDate;
    private BrandDTO brand;
    private CustomerDTO customer;
}
