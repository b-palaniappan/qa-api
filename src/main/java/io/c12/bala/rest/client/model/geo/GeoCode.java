package io.c12.bala.rest.client.model.geo;

import lombok.Data;

import java.util.Set;

@Data
public class GeoCode {
    private Set<Item> items;
}
