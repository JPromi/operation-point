package com.jpromi.operation_point.service.impl;

import com.jpromi.operation_point.service.LocationService;
import com.opencsv.CSVReader;
import org.springframework.data.repository.init.ResourceReader;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;


@Service
public class LocationServiceImpl implements LocationService {

    @Override
    public String getDistrictByZipCode(String zipCode) {
        try (
                InputStream is = getClass().getClassLoader().getResourceAsStream("mapping/AT-zipcode.csv");
                CSVReader reader = new CSVReader(new InputStreamReader(is))
        ) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length > 2 && nextLine[0].trim().equals(zipCode)) {
                    return nextLine[2].trim();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String getCityByZipCode(String zipCode) {
        try (
                InputStream is = getClass().getClassLoader().getResourceAsStream("mapping/AT-zipcode.csv");
                CSVReader reader = new CSVReader(new InputStreamReader(is))
        ) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length > 1 && nextLine[0].trim().equals(zipCode)) {
                    return nextLine[1].trim();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String getDistrictIdByDistrict(String district) {
        try (
                InputStream is = getClass().getClassLoader().getResourceAsStream("mapping/district-mapping.csv");
                CSVReader reader = new CSVReader(new InputStreamReader(is))
        ) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine.length > 1 && nextLine[0].trim().equalsIgnoreCase(district)) {
                    return nextLine[1].trim();
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
