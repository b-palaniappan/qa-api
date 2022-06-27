package io.c12.bala.api.config;

import org.modelmapper.ModelMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;

@Dependent
public class ModelMapperConfig {

    @Produces
    @ApplicationScoped
    ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
