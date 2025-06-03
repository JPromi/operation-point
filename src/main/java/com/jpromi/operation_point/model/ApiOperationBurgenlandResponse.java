package com.jpromi.operation_point.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ApiOperationBurgenlandResponse {
    private List<ApiOperationBurgenlandResponseOperation> operations_current;

    @Data
    public static class ApiOperationBurgenlandResponseOperation {
        @JsonProperty("operation_id")
        private String operationId;
        private String district;
        private String code;

        @JsonProperty("code_desc")
        private String codeDesc;

        @JsonProperty("place_of_operation")
        private String placeOfOperation;

        @JsonProperty("num_vehicles")
        private String numVehicles;

        private String start;

        @JsonProperty("fire_service")
        private String fireService;

        @JsonProperty("updated_at")
        private String updatedAt;

        @JsonProperty("fw_locations")
        private List<String> fwLocations;

        private Boolean info;
    }
}
