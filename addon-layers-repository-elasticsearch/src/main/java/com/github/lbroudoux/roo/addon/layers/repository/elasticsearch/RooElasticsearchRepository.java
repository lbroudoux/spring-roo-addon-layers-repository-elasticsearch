package com.github.lbroudoux.roo.addon.layers.repository.elasticsearch;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks the annotated type as a Spring Data repository interface.
 * @author Laurent Broudoux
 * @since 1.2
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface RooElasticsearchRepository {

   /**
    * The name of this annotation's attribute that specifies the managed domain type.
    */
   String DOMAIN_TYPE_ATTRIBUTE = "domainType";

   /**
    * The domain type managed by the annotated repository.
    * @return a non-<code>null</code> entity type
    */
   Class<?> domainType();
}
