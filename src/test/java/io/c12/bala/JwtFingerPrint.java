package io.c12.bala;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.google.common.hash.Hashing;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static io.c12.bala.api.config.ApiConstants.NANOID_DEFAULT_ALPHABET;

public class JwtFingerPrint {
    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        // Create unique fingerprint with nano id
        String userFingerPrint = "ufp_" + NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, NANOID_DEFAULT_ALPHABET, 25);

        // Create sha-256 digest.
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] userFingerprintDigest = digest.digest(userFingerPrint.getBytes(StandardCharsets.UTF_8));
        String hashFingerPrint = Base64.getEncoder().encodeToString(userFingerprintDigest);

        // Create has using google guava. HMACSHA-256 need kay and string to create hash
        System.out.println(Hashing.hmacSha256(userFingerPrint.getBytes(StandardCharsets.UTF_8)).hashString(userFingerPrint, StandardCharsets.UTF_8));
        System.out.println(Hashing.sha512().hashString(userFingerPrint, StandardCharsets.UTF_8));

        // Create base64 encoded sha512 message digest.
        System.out.println(Base64.getEncoder().encodeToString(Hashing.sha512().hashString(userFingerPrint, StandardCharsets.UTF_8).asBytes()));

        System.out.println(" user finger print = " + userFingerPrint + " | Hash Finger print " + hashFingerPrint);
    }
}
