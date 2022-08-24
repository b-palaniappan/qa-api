package io.c12.bala;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import io.smallrye.jwt.build.Jwt;
import org.eclipse.microprofile.jwt.Claims;

import java.util.Set;

/**
 * Main method to generate JWT token for testing using private key.
 * pass vm arg `-Dsmallrye.jwt.sign.key.location=privateKey.pem` to generate JWT with private key.
 */
public class GenerateToken {
    public static void main(String[] args) {
        String token = Jwt.issuer("https://c12.io/a/issuer")
            .upn("jdoe@c12.io")
            .groups(Set.of("User", "Admin"))
            .claim(Claims.full_name.name(), "John Doe")
            .subject("hello_world")
            .issuer(NanoIdUtils.randomNanoId())
            .sign();
        System.out.println(token);
    }
}
