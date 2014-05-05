package edu.sjsu.cmpe283.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


public class HTTP {
    
    public static String GET(String url) throws IOException {
        // Send GET Request
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        CloseableHttpResponse response = httpclient.execute(get);
        
        // Get String
        String data = "";
        try {
            HttpEntity entity = response.getEntity();
            
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                data += line;
            }
            
            data = data.replaceAll("\\s+", " ");
            EntityUtils.consumeQuietly(entity);
        }
        finally {
            response.close();
        }
        return data;
    }
}
