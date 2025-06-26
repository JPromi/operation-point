package com.jpromi.operation_point.service;

public interface LocationService {
    String getDistrictByZipCode(String zipCode);
    String getCityByZipCode(String zipCode);
    String getDistrictIdByDistrict(String district);
}
