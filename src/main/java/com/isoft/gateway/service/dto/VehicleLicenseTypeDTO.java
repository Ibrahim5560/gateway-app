package com.isoft.gateway.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.isoft.gateway.domain.VehicleLicenseType} entity.
 */
@Schema(description = "VehicleLicenseType (Vehicle_license_type) entity.\n @author Ibrahim Mohamed.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VehicleLicenseTypeDTO implements Serializable {

    private Long id;

    private String name;

    private Integer rank;

    private String engName;

    private String code;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getEngName() {
        return engName;
    }

    public void setEngName(String engName) {
        this.engName = engName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VehicleLicenseTypeDTO)) {
            return false;
        }

        VehicleLicenseTypeDTO vehicleLicenseTypeDTO = (VehicleLicenseTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vehicleLicenseTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehicleLicenseTypeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", rank=" + getRank() +
            ", engName='" + getEngName() + "'" +
            ", code='" + getCode() + "'" +
            "}";
    }
}
