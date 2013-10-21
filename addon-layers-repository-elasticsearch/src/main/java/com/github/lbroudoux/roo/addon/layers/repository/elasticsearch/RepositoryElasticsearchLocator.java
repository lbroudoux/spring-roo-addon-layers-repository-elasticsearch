package com.github.lbroudoux.roo.addon.layers.repository.elasticsearch;

import java.util.Collection;

import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.model.JavaType;

/**
 * Locates Spring Data Elasticsearch Repositories within the user's project.
 * @author Laurent Broudoux
 * @since 1.2
 */
public interface RepositoryElasticsearchLocator {
   /**
    * Returns the repositories that support the given domain type
    * @param domainType the domain type for which to find the repositories; can
    * be <code>null</code>
    * @return a non-<code>null</code> collection
    */
   Collection<ClassOrInterfaceTypeDetails> getRepositories(final JavaType domainType);
}
