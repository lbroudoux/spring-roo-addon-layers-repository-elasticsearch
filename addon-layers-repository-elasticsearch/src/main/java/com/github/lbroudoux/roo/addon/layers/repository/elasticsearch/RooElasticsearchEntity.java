package com.github.lbroudoux.roo.addon.layers.repository.elasticsearch;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks the annotated type as a Spring Data Elasticsearch domain entity.
 * @author Laurent Broudoux
 * @since 1.2
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface RooElasticsearchEntity {
}

