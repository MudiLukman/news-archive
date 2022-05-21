package com.kontrol.newsarchive.util;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

public class ElasticClientHelper {

    private ElasticClientHelper() {
        throw new IllegalStateException("Private constructor must not be called");
    }

    public static ElasticsearchClient getConnection(){
        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200))
                .build();

        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        return new ElasticsearchClient(transport);
    }

}
