// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.github.lbroudoux.es.web;

import com.github.lbroudoux.es.domain.Tweet;
import com.github.lbroudoux.es.service.TweetService;
import com.github.lbroudoux.es.web.ApplicationConversionServiceFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;

privileged aspect ApplicationConversionServiceFactoryBean_Roo_ConversionService {
    
    declare @type: ApplicationConversionServiceFactoryBean: @Configurable;
    
    @Autowired
    TweetService ApplicationConversionServiceFactoryBean.tweetService;
    
    public Converter<Tweet, String> ApplicationConversionServiceFactoryBean.getTweetToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.github.lbroudoux.es.domain.Tweet, java.lang.String>() {
            public String convert(Tweet tweet) {
                return new StringBuilder().append(tweet.getAuthor()).append(' ').append(tweet.getContent()).toString();
            }
        };
    }
    
    public Converter<String, Tweet> ApplicationConversionServiceFactoryBean.getIdToTweetConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.github.lbroudoux.es.domain.Tweet>() {
            public com.github.lbroudoux.es.domain.Tweet convert(java.lang.String id) {
                return tweetService.findTweet(id);
            }
        };
    }
    
    public void ApplicationConversionServiceFactoryBean.installLabelConverters(FormatterRegistry registry) {
        registry.addConverter(getTweetToStringConverter());
        registry.addConverter(getIdToTweetConverter());
    }
    
    public void ApplicationConversionServiceFactoryBean.afterPropertiesSet() {
        super.afterPropertiesSet();
        installLabelConverters(getObject());
    }
    
}