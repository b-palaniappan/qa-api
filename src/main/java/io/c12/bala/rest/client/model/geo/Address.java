package io.c12.bala.rest.client.model.geo;

import lombok.Data;

@Data
public class Address {
    private String label;
    private String countryCode;
    private String countryName;
    private String stateCode;
    private String state;
    private String county;
    private String city;
    private String street;
    private String postalCode;
    private String houseNumber;
}
