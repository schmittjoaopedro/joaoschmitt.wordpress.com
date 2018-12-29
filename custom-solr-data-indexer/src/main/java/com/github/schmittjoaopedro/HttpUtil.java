package com.github.schmittjoaopedro;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.util.ssl.SslContextFactory;

public class HttpUtil {

    public static String getRequest(final String url) {
        HttpClient httpClient = null;
        SslContextFactory sslContextFactory = null;
        try {
            sslContextFactory = new SslContextFactory();
            httpClient = new HttpClient(sslContextFactory);
            httpClient.start();
            ContentResponse response = httpClient.GET(url);
            return response.getContentAsString();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.stop();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }

}
