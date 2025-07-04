package com.rewe.customerbookingservice.dtos;

import jakarta.validation.constraints.Size;
import lombok.*;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class BrandDTO {
    private Long id;

    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;
    private String address;
    private String shortCode;
}
