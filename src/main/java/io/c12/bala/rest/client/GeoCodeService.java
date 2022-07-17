package io.c12.bala.rest.client;

import io.c12.bala.rest.client.model.geo.GeoCode;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@Path("/v1/geocode")
@RegisterRestClient(configKey = "here-api")
public interface GeoCodeService {

    @GET
    Uni<GeoCode> getGeoCodeByAddress(@QueryParam("q") String address, @QueryParam("apiKey") String apiKey, @QueryParam("in") String countryCodeFilter);
}
