package com.example.demo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;


import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;

public class JwtUtil {

    private static final Key SECRET_KEY = Keys.hmacShaKeyFor("bankingsecretkey12345678901234567890".getBytes());
    private static final long EXPIRY_TIME = 3600000; // 1 hour

    // generate token
    public static String generateToken(String identity) {
        return Jwts.builder()
                .setSubject(identity)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRY_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    // verify token and get accno from it
    public static String validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public static boolean isTokenExpired(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
            
            return false; // no exception → not expired!
        }
        catch(ExpiredJwtException e) {
            return true; // expired!
        }
    }

    public static List<String> tokenCleanUp(String old_black_list_string)  throws IOException{

        ObjectMapper mapper = new ObjectMapper();
        List<String> tokenList = mapper.readValue(old_black_list_string, new TypeReference<List<String>>() {});

        List<String> new_tokenList = new ArrayList<>();

                    

        for(String cur_token : tokenList){
            if(!JwtUtil.isTokenExpired(cur_token) ){
                            //hoo its a valid token - if need to retain it - add to our new list
                new_tokenList.add(cur_token);
              
            }
                                   
        }
        

        //now return the updated token list
        return new_tokenList;

    }


    public static boolean isTokenInBlacklist(String black_list_string,String token) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode black_list = mapper.readTree(black_list_string);

        for (JsonNode node : black_list) {
            String cur_token = node.asText();

                    // now compare with our token

            if(token.equals(cur_token)){
                return true;// hoo it is present in the blacklist - reject the request
            }
                    
        }
        
        return false; // token not present in the blacklist at all - its valid token

    }
}
