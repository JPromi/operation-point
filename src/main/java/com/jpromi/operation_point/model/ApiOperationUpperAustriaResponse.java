package com.jpromi.operation_point.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Data
public class ApiOperationUpperAustriaResponse {
    private Boolean webext2;
    private String version;
    private String title;
    private String pubDate;

    @JsonProperty("cnt_einsaetze")
    private Long cntEinsaetze;

    @JsonProperty("cnt_feuerwehren")
    private Long cntFeuerwehren;

    private Map<String, ApiOperationUpperAustriaResponseOperationWrapper> einsaetze;

    @Data
    public static class ApiOperationUpperAustriaResponseOperationWrapper {
        private ApiOperationUpperAustriaResponseOperation einsatz;
    }

    @Data
    public static class ApiOperationUpperAustriaResponseOperation {
        private String num1;
        private String einsatzort;
        private String startzeit;
        private String inzeit;
        private String status;
        private String alarmstufe;
        private String einsatzart;
        private ApiOperationUpperAustriaResponseOperationTyp einsatztyp;
        private ApiOperationUpperAustriaResponseOperationTyp einsatzsubtyp;
        private ApiOperationUpperAustriaResponseOperationAdresse adresse;
        private ApiOperationUpperAustriaResponseOperationWgs84 wgs84;
        private ApiOperationUpperAustriaResponseOperationTyp bezirk;
        private Map<String, ApiOperationUpperAustriaResponseOperationFeuerwehr> feuerwehren;
        private Map<String, ApiOperationUpperAustriaResponseOperationFeuerwehrArray> feuerwehrenarray;
        private Long cntfeuerwehren;

        @Data
        public static class ApiOperationUpperAustriaResponseOperationTyp {
            private String id;
            private String text;
        }

        @Data
        public static class ApiOperationUpperAustriaResponseOperationAdresse {
            @JsonProperty("default")
            private String adresseDefault;
            private String earea;
            private String emun;
            private String efeanme;
            private String estnum;
            private String ecompl;
        }

        @Data
        public static class ApiOperationUpperAustriaResponseOperationWgs84 {
            private Double lng;
            private Double lat;
        }

        @Data
        public static class ApiOperationUpperAustriaResponseOperationFeuerwehr {
            private String feuerwehr;
        }

        @Data
        public static class ApiOperationUpperAustriaResponseOperationFeuerwehrArray {
            private Long fwnr;
            private String fwname;
        }
    }

}
