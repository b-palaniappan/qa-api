package io.c12.bala.rest.client.model.geo;

import lombok.Data;

@Data
public class Item {
    private String title;
    private String id;
    private String resultType;
    private String houseNumberType;
    private Address address;
    private Position position;
}
