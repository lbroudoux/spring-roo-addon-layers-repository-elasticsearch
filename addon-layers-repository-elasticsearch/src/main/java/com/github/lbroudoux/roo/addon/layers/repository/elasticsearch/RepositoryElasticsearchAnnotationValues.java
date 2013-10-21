package com.github.lbroudoux.roo.addon.layers.repository.elasticsearch;

import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.details.annotations.populator.AbstractAnnotationValues;
import org.springframework.roo.classpath.details.annotations.populator.AutoPopulate;
import org.springframework.roo.classpath.details.annotations.populator.AutoPopulationUtils;
import org.springframework.roo.model.JavaType;

/**
 * The values of a {@link RooElasticsearchRepository} annotation.
 * @author Laurent Broudoux
 */
public class RepositoryElasticsearchAnnotationValues extends AbstractAnnotationValues {

   @AutoPopulate
   private JavaType domainType;
   
   /**
    * Constructor.
    * @param governorPhysicalTypeMetadata the metadata to parse (required)
    */
   public RepositoryElasticsearchAnnotationValues(final PhysicalTypeMetadata governorPhysicalTypeMetadata){
      super(governorPhysicalTypeMetadata, ElasticsearchJavaType.ROO_REPOSITORY_ELASTICSEARCH);
      AutoPopulationUtils.populate(this, annotationMetadata);
   }
   
   /**
    * Returns the domain type managed by the annotated repository
    * @return a non-<code>null</code> type
    */
    public JavaType getDomainType(){
       return domainType;
    }
}
