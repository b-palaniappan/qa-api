package io.c12.bala.api.config;

import org.modelmapper.ModelMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

@Singleton
public class ModelMapperConfig {

    @Produces
    @ApplicationScoped
    ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
