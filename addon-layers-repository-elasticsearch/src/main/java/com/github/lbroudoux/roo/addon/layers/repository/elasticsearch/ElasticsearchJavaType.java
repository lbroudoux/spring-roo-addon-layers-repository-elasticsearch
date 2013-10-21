package com.github.lbroudoux.roo.addon.layers.repository.elasticsearch;

import org.springframework.roo.model.JavaType;

/**
 * Class for holding references to JavaType contained into this addon.
 * @author Laurent Broudoux
 */
public class ElasticsearchJavaType {

   public static final JavaType ROO_ELASTICSEARCH_ENTITY = new JavaType(
         "com.github.lbroudoux.roo.addon.layers.repository.elasticsearch.RooElasticsearchEntity");
   
   public static final JavaType ROO_REPOSITORY_ELASTICSEARCH = new JavaType(
         "com.github.lbroudoux.roo.addon.layers.repository.elasticsearch.RooElasticsearchRepository");
}
