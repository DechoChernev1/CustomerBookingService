package com.rewe.customerbookingservice.dtos;

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
    private String name;
    private String address;
    private String shortCode;
}
