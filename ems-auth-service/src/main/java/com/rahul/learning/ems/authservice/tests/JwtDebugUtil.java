package com.rahul.learning.ems.authservice.tests;

public class JwtDebugUtil {

    public static void main(String[] args) {

        String token = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJyYWh1bCIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE3OTA5NDI4MDEsImV4cCI6MTc5MTU0NzYwMX0.UFEqxDstD3dLKJlOA_A2x2aSL22jrTHwysCBHtkroGaPT7kwFpCHyOCZWJq1OiiB";
        String[] parts = token.split("\\.");

        String payload = new String(java.util.Base64.getUrlDecoder().decode(parts[1])
        );

        System.out.println("JWT Payload:");
        System.out.println(payload);
    }
}