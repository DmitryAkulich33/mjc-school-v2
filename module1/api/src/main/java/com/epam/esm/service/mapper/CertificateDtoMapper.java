package com.epam.esm.service.mapper;

import com.epam.esm.domain.Certificate;
import com.epam.esm.model.dto.CertificateDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = TagDtoMapper.class)
public interface CertificateDtoMapper {
    List<CertificateDto> toCertificateDtoList (List<Certificate> certificates);
    CertificateDto toCertificateDto (Certificate certificate);
}
