package com.rewe.customerbookingservice.dtos;

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
    private String name;
    private String email;
    private boolean active;
    private int age;
    private LocalDateTime created;
    private LocalDateTime updated;
    private List<BookingDTO> bookings;
}
