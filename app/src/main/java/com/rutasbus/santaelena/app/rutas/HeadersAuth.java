package com.rutasbus.santaelena.app.rutas;

import android.annotation.TargetApi;
import android.os.Build;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

public class HeadersAuth {

    /**
     *
     * @param user
     * @param password
     * @return Autrizacion Basic para poder acceder a las distintas consultas en el web services
     */
    @TargetApi(Build.VERSION_CODES.O)
    public HttpHeaders createHttpHeaders(String user, String password){
        String notEncoded = user + ":" + password;

     /*  String encodedAuth = Base64.getEncoder().encodeToString(notEncoded.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Basic " + encodedAuth);
        return headers;*/

        byte[] data;
        try {
            data = notEncoded.getBytes("UTF-8");
            String base64Sms = Base64.encodeToString(data, Base64.DEFAULT);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Basic " + base64Sms);
            return headers;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;

    }




}
