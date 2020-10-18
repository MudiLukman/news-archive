package com.kontrol.newsarchive.util;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;

public class ElasticClientHelper {

    private static RestHighLevelClient client;
    private static Settings settings;

    public static RestHighLevelClient getConnection(){
        settings = Settings.builder().put("cluster.name", "newsarchive")
                .put("path.home", "/").put("client.transport.ping_timeout", "60s")
                .build();

        client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http"),
                        new HttpHost("localhost", 9201, "http")));

        return client;
    }

}
