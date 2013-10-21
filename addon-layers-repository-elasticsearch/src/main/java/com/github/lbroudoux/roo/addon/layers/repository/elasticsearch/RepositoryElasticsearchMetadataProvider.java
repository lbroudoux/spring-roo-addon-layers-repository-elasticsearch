package com.github.lbroudoux.roo.addon.layers.repository.elasticsearch;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;
import org.springframework.roo.classpath.PhysicalTypeIdentifier;
import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.customdata.taggers.CustomDataKeyDecorator;
import org.springframework.roo.classpath.details.ItdTypeDetails;
import org.springframework.roo.classpath.details.MemberHoldingTypeDetails;
import org.springframework.roo.classpath.itd.AbstractMemberDiscoveringItdMetadataProvider;
import org.springframework.roo.classpath.itd.ItdTriggerBasedMetadataProvider;
import org.springframework.roo.classpath.itd.ItdTypeDetailsProvidingMetadataItem;
import org.springframework.roo.classpath.layers.LayerTypeMatcher;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.project.LogicalPath;

/**
 * Provides {@link RepositoryElasticsearchMetadata}. This type is called by Roo to retrieve the metadata for this add-on.
 * @author Laurent Broudoux
 * @since 1.2
 */
@Component(immediate = true)
@Service
public class RepositoryElasticsearchMetadataProvider extends AbstractMemberDiscoveringItdMetadataProvider 
      implements ItdTriggerBasedMetadataProvider {

   @Reference
   private CustomDataKeyDecorator customDataKeyDecorator;
   
   private final Map<JavaType, String> domainTypeToRepositoryMidMap = new LinkedHashMap<JavaType, String>();
   private final Map<String, JavaType> repositoryMidToDomainTypeMap = new LinkedHashMap<String, JavaType>();
   
   /**
    * The activate method for this OSGi component, this will be called by the OSGi container upon bundle activation 
    * (result of the 'addon install' command) 
    * @param context the component context can be used to get access to the OSGi container (ie find out if certain bundles are active)
    */
   protected void activate(ComponentContext context) {
      super.setDependsOnGovernorBeingAClass(false);
      metadataDependencyRegistry.addNotificationListener(this);
      metadataDependencyRegistry.registerDependency(PhysicalTypeIdentifier.getMetadataIdentiferType(), getProvidesType());
      addMetadataTrigger(ElasticsearchJavaType.ROO_REPOSITORY_ELASTICSEARCH);
      registerMatchers();
   }
   
   /**
    * The deactivate method for this OSGi component, this will be called by the OSGi container upon bundle deactivation 
    * (result of the 'addon uninstall' command) 
    * @param context the component context can be used to get access to the OSGi container (ie find out if certain bundles are active)
    */
   protected void deactivate(ComponentContext context) {
      metadataDependencyRegistry.removeNotificationListener(this);
      metadataDependencyRegistry.deregisterDependency(PhysicalTypeIdentifier.getMetadataIdentiferType(), getProvidesType());
      removeMetadataTrigger(ElasticsearchJavaType.ROO_REPOSITORY_ELASTICSEARCH);
      customDataKeyDecorator.unregisterMatchers(getClass());
   }
   
   @Override
   public String getItdUniquenessFilenameSuffix() {
      return "Elasticsearch_Repository";
   }

   @Override
   public String getProvidesType() {
      return RepositoryElasticsearchMetadata.getMetadataIdentiferType();
   }

   @Override
   protected String getLocalMidToRequest(ItdTypeDetails itdTypeDetails) {      
      // Determine the governor for this ITD, and whether any metadata is even hoping to hear about changes to that JavaType and its ITDs.
      final JavaType governor = itdTypeDetails.getName();
      final String localMid = domainTypeToRepositoryMidMap.get(governor);
      if (localMid != null) {
         return localMid;
      }

      final MemberHoldingTypeDetails memberHoldingTypeDetails = typeLocationService.getTypeDetails(governor);
      if (memberHoldingTypeDetails != null) {
         for (final JavaType type : memberHoldingTypeDetails.getLayerEntities()) {
            final String localMidType = domainTypeToRepositoryMidMap.get(type);
            if (localMidType != null) {
               return localMidType;
            }
         }
      }
      return null;
   }

   @Override
   protected String createLocalIdentifier(JavaType javaType, LogicalPath path) {
      return RepositoryElasticsearchMetadata.createIdentifier(javaType, path);
   }

   @Override
   protected String getGovernorPhysicalTypeIdentifier(String metadataIdentificationString) {
      JavaType javaType = RepositoryElasticsearchMetadata.getJavaType(metadataIdentificationString);
      LogicalPath path = RepositoryElasticsearchMetadata.getPath(metadataIdentificationString);
      return PhysicalTypeIdentifier.createIdentifier(javaType, path);
   }

   @Override
   protected ItdTypeDetailsProvidingMetadataItem getMetadata(
         String metadataIdentificationString, JavaType aspectName,
         PhysicalTypeMetadata governorPhysicalTypeMetadata, String itdFilename) {
      
      final RepositoryElasticsearchAnnotationValues annotationValues = new RepositoryElasticsearchAnnotationValues(governorPhysicalTypeMetadata);
      final JavaType domainType = annotationValues.getDomainType();
      
      // Remember that this entity JavaType matches up with this metadata identification string.
      // Start by clearing any previous association.
      final JavaType oldEntity = repositoryMidToDomainTypeMap.get(metadataIdentificationString);
      if (oldEntity != null) {
         domainTypeToRepositoryMidMap.remove(oldEntity);
      }
      domainTypeToRepositoryMidMap.put(domainType, metadataIdentificationString);
      repositoryMidToDomainTypeMap.put(metadataIdentificationString, domainType);
      
      return new RepositoryElasticsearchMetadata(metadataIdentificationString, aspectName, governorPhysicalTypeMetadata, annotationValues);
   }

   @SuppressWarnings("unchecked")
   private void registerMatchers() {
       customDataKeyDecorator.registerMatchers(getClass(),
               new LayerTypeMatcher(ElasticsearchJavaType.ROO_REPOSITORY_ELASTICSEARCH, new JavaSymbolName(
                       RooElasticsearchRepository.DOMAIN_TYPE_ATTRIBUTE)));
   }
}
