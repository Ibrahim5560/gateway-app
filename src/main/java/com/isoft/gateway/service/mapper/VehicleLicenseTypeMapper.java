package com.isoft.gateway.service.mapper;

import com.isoft.gateway.domain.VehicleLicenseType;
import com.isoft.gateway.service.dto.VehicleLicenseTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VehicleLicenseType} and its DTO {@link VehicleLicenseTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface VehicleLicenseTypeMapper extends EntityMapper<VehicleLicenseTypeDTO, VehicleLicenseType> {}
