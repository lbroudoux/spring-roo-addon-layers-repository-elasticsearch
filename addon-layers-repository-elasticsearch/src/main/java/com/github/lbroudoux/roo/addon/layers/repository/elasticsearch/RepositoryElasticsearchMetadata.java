package com.github.lbroudoux.roo.addon.layers.repository.elasticsearch;

import java.util.Arrays;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.roo.classpath.PhysicalTypeIdentifierNamingUtils;
import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.itd.AbstractItdTypeDetailsProvidingMetadataItem;
import org.springframework.roo.metadata.MetadataIdentificationUtils;
import org.springframework.roo.model.DataType;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.model.SpringJavaType;
import org.springframework.roo.project.LogicalPath;

/**
 * Creates metadata for repository layer ITDs (annotated with {@link RooElasticsearchRepository}.
 * @author Laurent Broudoux
 * @since 1.2
 */
public class RepositoryElasticsearchMetadata extends AbstractItdTypeDetailsProvidingMetadataItem {

   private static final String PROVIDES_TYPE_STRING = RepositoryElasticsearchMetadata.class.getName();
 
   private static final String PROVIDES_TYPE = MetadataIdentificationUtils.create(PROVIDES_TYPE_STRING);
 
   private static final String SPRING_DATA_REPOSITORY = "org.springframework.data.repository.PagingAndSortingRepository";
   
   public static String createIdentifier(final JavaType javaType, final LogicalPath path) {
      return PhysicalTypeIdentifierNamingUtils.createIdentifier(PROVIDES_TYPE_STRING, javaType, path);
   }

   public static JavaType getJavaType(final String metadataIdentificationString) {
      return PhysicalTypeIdentifierNamingUtils.getJavaType(PROVIDES_TYPE_STRING, metadataIdentificationString);
   }

   public static String getMetadataIdentiferType() {
      return PROVIDES_TYPE;
   }

   public static LogicalPath getPath(final String metadataIdentificationString) {
      return PhysicalTypeIdentifierNamingUtils.getPath(PROVIDES_TYPE_STRING, metadataIdentificationString);
   }

   public static boolean isValid(final String metadataIdentificationString) {
      return PhysicalTypeIdentifierNamingUtils.isValid(PROVIDES_TYPE_STRING, metadataIdentificationString);
   }
   
   /**
    * 
    * @param identifier
    * @param aspectName
    * @param governorPhysicalTypeMetadata
    */
   protected RepositoryElasticsearchMetadata(String identifier, JavaType aspectName, 
         PhysicalTypeMetadata governorPhysicalTypeMetadata,
         final RepositoryElasticsearchAnnotationValues annotationValues){
      super(identifier, aspectName, governorPhysicalTypeMetadata);
      
      Validate.notNull(annotationValues, "Annotation values required");
      
      // Make the user's Repository interface extend Spring Data's Repository
      // interface if it doesn't already.
      ensureGovernorExtends(new JavaType(SPRING_DATA_REPOSITORY, 0,
              DataType.TYPE, null, Arrays.asList(
                      annotationValues.getDomainType(), JavaType.STRING)));

      builder.addAnnotation(getTypeAnnotation(SpringJavaType.REPOSITORY));
      
      // Build the ITD.
      itdTypeDetails = builder.build();
   }
   
   @Override
   public String toString() {
       final ToStringBuilder builder = new ToStringBuilder(this);
       builder.append("identifier", getId());
       builder.append("valid", valid);
       builder.append("aspectName", aspectName);
       builder.append("destinationType", destination);
       builder.append("governor", governorPhysicalTypeMetadata.getId());
       builder.append("itdTypeDetails", itdTypeDetails);
       return builder.toString();
   }
}
