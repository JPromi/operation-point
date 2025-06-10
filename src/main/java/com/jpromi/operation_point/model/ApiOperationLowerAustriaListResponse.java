package com.jpromi.operation_point.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ApiOperationLowerAustriaListResponse {

    @JsonProperty("Einsatz")
    private List<ApiOperationLowerAustriaListResponseOperation> einsatz;

    @Data
    public static class ApiOperationLowerAustriaListResponseOperation {
        private String m;
        private String a;
        private String n;
        private String o;
        private String o2;
        private String d;
        private String t;
        private String i;
        private String b;
    }

}
