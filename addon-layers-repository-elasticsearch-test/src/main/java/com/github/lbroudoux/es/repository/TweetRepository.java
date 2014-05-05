package com.github.lbroudoux.es.repository;

import com.github.lbroudoux.es.domain.Tweet;
import com.github.lbroudoux.roo.addon.layers.repository.elasticsearch.RooElasticsearchRepository;

@RooElasticsearchRepository(domainType = Tweet.class)
public interface TweetRepository {

    Iterable<com.github.lbroudoux.es.domain.Tweet> findAll();
}
