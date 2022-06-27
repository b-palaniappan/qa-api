package io.c12.bala.api.model.constant;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum UserStatus {

    @JsonProperty("active")
    ACTIVE,

    @JsonProperty("inactive")
    INACTIVE,

    @JsonProperty("terminated")
    TERMINATED

}
