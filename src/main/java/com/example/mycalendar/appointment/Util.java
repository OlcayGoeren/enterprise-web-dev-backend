package com.example.mycalendar.appointment;

import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class Util {

    public String getCSV(Appointment appointment) throws IOException {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost("http://localhost:3300/download");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("title", appointment.getTitle()));
        params.add(new BasicNameValuePair("details", appointment.getDetails()));
        params.add(new BasicNameValuePair("ort", appointment.getOrt()));
        params.add(new BasicNameValuePair("von", appointment.getVon()));
        params.add(new BasicNameValuePair("bis", appointment.getBis()));

        request.addHeader("content-type", "application/x-www-form-urlencoded");
        request.setEntity(new UrlEncodedFormEntity(params));
        HttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        InputStream inputStream = entity.getContent();
        String result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        inputStream.close();
        return result;
    }

}
