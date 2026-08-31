package com.nandini.ledgersplit;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;

public class JWTKeyGenerator {

    public static void main(String[] args) {

        var key = Jwts.SIG.HS256.key().build();

        var secret = Encoders.BASE64.encode(key.getEncoded());

        System.out.println(secret);
    }
}