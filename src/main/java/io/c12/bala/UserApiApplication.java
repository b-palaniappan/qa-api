package io.c12.bala;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@OpenAPIDefinition(tags = {@Tag(name = "user", description = "User operations.")},
    info = @Info(
        title = "UserAPI",
        version = "0.1.0",
        contact = @Contact(
            name = "User API Support",
            url = "https://c12.io/contact",
            email = "techsupport@c12.io"),
        license = @License(
            name = "Apache 2.0",
            url = "https://www.apache.org/licenses/LICENSE-2.0.html"))
)
@ApplicationPath("/v1")
public class UserApiApplication extends Application {
    // For swagger generation.
}
