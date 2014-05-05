package com.github.lbroudoux.es.domain;

import com.github.lbroudoux.roo.addon.layers.repository.elasticsearch.RooElasticsearchEntity;
import javax.validation.constraints.Size;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooElasticsearchEntity
public class Tweet {

    private String author;

    @Size(max = 140)
    private String content;
}
