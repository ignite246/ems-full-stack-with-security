package com.rahul.learning.javaguide.emsbackend.mappers;

import com.rahul.learning.javaguide.emsbackend.dtos.OfficeDTO;
import com.rahul.learning.javaguide.emsbackend.entities.Office;

public class OfficeMapper {
    private OfficeMapper() {
        /* This utility class should not be instantiated */
    }

    public static OfficeDTO mapToOfficeDTO(Office office) {
        return new OfficeDTO(
                office.getOfficeId(),
                office.getName(),
                office.getOfficeEmail(),
                office.getSeatingCapacity(),
                office.getPointOfContact(),
                office.getAddress(),
                office.getCreatedAt(),
                office.getUpdatedAt()
        );
    }

    public static Office mapToOffice(OfficeDTO officeDTO) {
        return new Office(
                officeDTO.getOfficeId(),
                officeDTO.getName(),
                officeDTO.getOfficeEmail(),
                officeDTO.getSeatingCapacity(),
                officeDTO.getPointOfContact(),
                officeDTO.getAddress(),
                officeDTO.getCreatedAt(),
                officeDTO.getUpdatedAt()
        );
    }
}
