package com.isoft.gateway.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.isoft.gateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VehicleLicenseTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleLicenseTypeDTO.class);
        VehicleLicenseTypeDTO vehicleLicenseTypeDTO1 = new VehicleLicenseTypeDTO();
        vehicleLicenseTypeDTO1.setId(1L);
        VehicleLicenseTypeDTO vehicleLicenseTypeDTO2 = new VehicleLicenseTypeDTO();
        assertThat(vehicleLicenseTypeDTO1).isNotEqualTo(vehicleLicenseTypeDTO2);
        vehicleLicenseTypeDTO2.setId(vehicleLicenseTypeDTO1.getId());
        assertThat(vehicleLicenseTypeDTO1).isEqualTo(vehicleLicenseTypeDTO2);
        vehicleLicenseTypeDTO2.setId(2L);
        assertThat(vehicleLicenseTypeDTO1).isNotEqualTo(vehicleLicenseTypeDTO2);
        vehicleLicenseTypeDTO1.setId(null);
        assertThat(vehicleLicenseTypeDTO1).isNotEqualTo(vehicleLicenseTypeDTO2);
    }
}
