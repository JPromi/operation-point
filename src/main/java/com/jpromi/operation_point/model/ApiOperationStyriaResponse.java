package com.jpromi.operation_point.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ApiOperationStyriaResponse {
    private String type;
    private List<ApiOperationStyriaResponseFeature> features;

    @Data
    public static class ApiOperationStyriaResponseFeature {
        private String type;
        private String oid;
        private ApiOperationStyriaResponseFeatureGeometry geometry;
        private ApiOperationStyriaResponseFeatureProperties properties;

        @Data
        public static class ApiOperationStyriaResponseFeatureGeometry {
            private String type;
            private List<Double> coordinates;
        }

        @Data
        public static class ApiOperationStyriaResponseFeatureProperties {
            @JsonProperty("Datum")
            private String datum;

            @JsonProperty("Im Einsatz Seit")
            private String imEinsatzSeit;

            @JsonProperty("Feuerwehr")
            private String feuerwehr;

            @JsonProperty("Typ")
            private String typ;

            @JsonProperty("Art")
            private String art;

            @JsonProperty("Wehren im Einsatz")
            private String wehrenImEinsatz;

            @JsonProperty("Instanznummer")
            private String instanznummer;

            @JsonProperty("Bereich")
            private String bereich;

            @JsonProperty("_title")
            private String title;
        }
    }
}
