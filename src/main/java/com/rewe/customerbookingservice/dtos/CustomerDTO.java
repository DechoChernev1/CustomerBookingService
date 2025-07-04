package com.rewe.customerbookingservice.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CustomerDTO {
    private Long id;

    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;

    @Min(value = 18, message = "Age should not be less than 18")
    @Max(value = 100, message = "Age should not exceed 100")
    private int age;

    @Email(message = "Email should be valid")
    private String email;

    private boolean active;
    private LocalDateTime created;
    private LocalDateTime updated;
    private List<BookingDTO> bookings;
}
