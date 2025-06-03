package com.jpromi.operation_point.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ApiOperationLowerAustriaResponse {

    private String m;
    private String o;
    private String p;
    private String o2;
    private String d;
    private String t;
    private String a;
    private String n;
    private String id;

    @JsonProperty("Dispo")
    private List<ApiOperationLowerAustriaResponseDispo> dispo;

    @Data
    public static class ApiOperationLowerAustriaResponseDispo {
        private String d;
        private String s;
        private String dt;
        private String at;
        private String ot;
        private String it;
    }

}
