package com.github.lbroudoux.roo.addon.layers.repository.elasticsearch;

import org.springframework.roo.model.JavaType;

/**
 * Interface of operations this add-on offers. Typically used by a command type or an external add-on.
 * @author Laurent Broudoux
 * @since 1.2
 */
public interface ElasticsearchOperations {

    /**
     * Indicate commands should be available.
     * @return true if it should be available, otherwise false
     */
    boolean isElasticsearchSetupAvailable();

    /**
     * Create a Java entity type with the trigger of this add-on.
     */
    void createType(JavaType classType);
    
    /**
     * Create a Java repository type for the specified domain type.
     */
    void createRepository(JavaType interfaceType, JavaType domainType);
    
    /**
     * Setup all add-on artifacts (dependencies in this case).
     */
    void setup(Boolean local, String clusterNodes);
}