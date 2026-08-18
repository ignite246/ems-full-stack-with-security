package com.rahul.learning.javaguide.emsbackend.services.impl;

import com.rahul.learning.javaguide.emsbackend.dtos.OfficeDTO;
import com.rahul.learning.javaguide.emsbackend.entities.Office;
import com.rahul.learning.javaguide.emsbackend.mappers.OfficeMapper;
import com.rahul.learning.javaguide.emsbackend.repos.OfficeRepository;
import com.rahul.learning.javaguide.emsbackend.services.OfficeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OfficeServiceImpl implements OfficeService {

    private final OfficeRepository officeRepository;

    @Override
    public OfficeDTO getOfficeById(Long id) {
        final Office office = officeRepository.findById(id).orElseThrow(() -> new RuntimeException("Office not found with id= " + id));
        return OfficeMapper.mapToOfficeDTO(office);
    }

    @Override
    public List<OfficeDTO> getAllOffices() {
        final List<Office> offices = officeRepository.findAll();
        return offices.stream().map(OfficeMapper::mapToOfficeDTO).toList();
    }

    @Override
    public OfficeDTO createOffice(OfficeDTO officeDTO) {
        final Office office = OfficeMapper.mapToOffice(officeDTO);
        final Office savedOffice = officeRepository.save(office);
        return OfficeMapper.mapToOfficeDTO(savedOffice);
    }

    @Override
    public OfficeDTO updateOffice(OfficeDTO officeDTO) {
        return null;
    }

    @Override
    public String deleteOffice(Long officeId) {
        officeRepository.deleteById(officeId);
        return "Office with id= " + officeId + " has been deleted";
    }
}