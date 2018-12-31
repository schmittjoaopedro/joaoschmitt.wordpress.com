package com.github.schmittjoaopedro;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.util.ssl.SslContextFactory;

public class HttpUtil {

    private static final String REPO_URL = "https://raw.githubusercontent.com/schmittjoaopedro/joaoschmitt.wordpress.com/master/custom-solr-data-indexer/assets/#FILE.json";

    public static String getRequest(final String file) {
        HttpClient httpClient = null;
        String url;
        SslContextFactory sslContextFactory = null;
        try {
            sslContextFactory = new SslContextFactory();
            httpClient = new HttpClient(sslContextFactory);
            httpClient.start();
            url = REPO_URL.replaceAll("#FILE", file);
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
