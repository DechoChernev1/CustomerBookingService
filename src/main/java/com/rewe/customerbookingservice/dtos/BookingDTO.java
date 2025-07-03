package com.rewe.customerbookingservice.dtos;

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
