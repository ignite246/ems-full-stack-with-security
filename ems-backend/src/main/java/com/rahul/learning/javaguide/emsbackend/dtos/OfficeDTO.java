package com.rahul.learning.javaguide.emsbackend.dtos;
import com.rahul.learning.javaguide.emsbackend.entities.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Office Information")
public class OfficeDTO {
    private Long officeId;
    private String name;
    private String officeEmail;
    private Long seatingCapacity;
    private String pointOfContact;
    private Address address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}