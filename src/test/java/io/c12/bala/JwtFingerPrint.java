package io.c12.bala;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static io.c12.bala.api.config.ApiConstants.NANOID_DEFAULT_ALPHABET;

public class JwtFingerPrint {
    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String userFingerPrint = "ufp_" + NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, NANOID_DEFAULT_ALPHABET, 25);

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] userFingerprintDigest = digest.digest(userFingerPrint.getBytes("utf-8"));
        String hashFingerPrint = Base64.getEncoder().encodeToString(userFingerprintDigest);

        System.out.println(" user finger print = " + userFingerPrint + " | Hash Finger print " + hashFingerPrint);
    }
}
