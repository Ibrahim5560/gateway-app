package com.isoft.gateway.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class VehicleLicenseTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static VehicleLicenseType getVehicleLicenseTypeSample1() {
        return new VehicleLicenseType().id(1L).name("name1").rank(1).engName("engName1").code("code1");
    }

    public static VehicleLicenseType getVehicleLicenseTypeSample2() {
        return new VehicleLicenseType().id(2L).name("name2").rank(2).engName("engName2").code("code2");
    }

    public static VehicleLicenseType getVehicleLicenseTypeRandomSampleGenerator() {
        return new VehicleLicenseType()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .rank(intCount.incrementAndGet())
            .engName(UUID.randomUUID().toString())
            .code(UUID.randomUUID().toString());
    }
}
