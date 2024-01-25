package com.example.shorturlpro;

import com.google.common.net.InternetDomainName;
import jakarta.persistence.Convert;
import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.util.Random;

@Component
public class UrlRandomLinkGenerator {
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*_-";
    public static String generateRandomString(String urlString) throws URISyntaxException {
        URI uri = new URI(urlString);
        String host = uri.getHost();
        SecureRandom random1 = new SecureRandom();
        Random random2 = new Random();
        int length = random2.nextInt(15 - 8 + 1) + 8;
        StringBuilder randomString = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = random1.nextInt(CHARACTERS.length());
            randomString.append(CHARACTERS.charAt(randomIndex));
        }
        String randomHref = String.valueOf(randomString);

        return host + randomHref;

    }
}
