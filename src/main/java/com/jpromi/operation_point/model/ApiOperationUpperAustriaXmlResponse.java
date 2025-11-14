package com.jpromi.operation_point.model;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlValue;

import java.util.List;

@XmlRootElement(name = "webext2")
public class ApiOperationUpperAustriaXmlResponse {
    @XmlAttribute
    public String version;

    @XmlElement
    public String scope;
    @XmlElement
    public String pubDate;
    @XmlElement
    public EinsatzList einsaetze;
    @XmlElement
    public ResourcesList resources;

    public static class EinsatzList {
        @XmlElement(name = "einsatz")
        public List<Einsatz> einsatz;

        public static class Einsatz {
            @XmlAttribute
            public String id;

            @XmlElement
            public String num1;
            @XmlElement
            public String startzeit;
            @XmlElement
            public String inzeit;
            @XmlElement
            public String status;
            @XmlElement
            public Double lng;
            @XmlElement
            public Double lat;
            @XmlElement
            public Integer alarmstufe;
            @XmlElement
            public String einsatzart;
            @XmlElement
            public String einsatzorg;
            @XmlElement
            public IdValueType einsatztyp;
            @XmlElement
            public IdValueType einsatzsubtyp;
            @XmlElement
            public String alarmtext;
            @XmlElement
            public Adresse adresse;
            @XmlElement
            public IdValueType bezirk;
            @XmlElement
            public EinheitenList einheiten;

            public static class Adresse {
                @XmlElement(name = "default")
                public String defaultAddress;
                @XmlElement
                public String earea;
                @XmlElement
                public String emun;
                @XmlElement
                public String efeanme;
                @XmlElement
                public String estnum;
                @XmlElement
                public String ecompl;
            }

            public static class EinheitenList {
                @XmlElement(name = "einheit")
                public List<IdValueType> einheit;
            }

        }
    }

    public static class ResourcesList {
        @XmlElement(name = "resource")
        public List<Resource> resource;

        public static class Resource {
            @XmlAttribute
            public String name;
            @XmlAttribute
            public String id;

            @XmlElement
            public IdValueType bezirk;
            @XmlElement
            public String www;
            @XmlElement
            public Double lng;
            @XmlElement
            public Double lat;
            @XmlElement
            public Usedat usedat;

            public static class Usedat {
                @XmlAttribute
                public String id;

                @XmlElement
                public String startzeit;
                @XmlElement
                public String inzeit;
            }
        }
    }

    public static class IdValueType {
        @XmlAttribute
        public String id;
        @XmlValue
        public String value;
    }
}
