/*
 * Licensed to Laurent Broudoux (the "Author") under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Author licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.github.lbroudoux.roo.addon.layers.repository.elasticsearch;

import static org.springframework.roo.classpath.customdata.CustomDataKeys.IDENTIFIER_ACCESSOR_METHOD;
import static org.springframework.roo.classpath.customdata.CustomDataKeys.IDENTIFIER_FIELD;
import static org.springframework.roo.classpath.customdata.CustomDataKeys.IDENTIFIER_MUTATOR_METHOD;
import static org.springframework.roo.classpath.customdata.CustomDataKeys.PERSISTENT_TYPE;

import java.util.Arrays;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;
import org.springframework.roo.classpath.PhysicalTypeIdentifier;
import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.customdata.taggers.AnnotatedTypeMatcher;
import org.springframework.roo.classpath.customdata.taggers.CustomDataKeyDecorator;
import org.springframework.roo.classpath.customdata.taggers.FieldMatcher;
import org.springframework.roo.classpath.customdata.taggers.MethodMatcher;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadataBuilder;
import org.springframework.roo.classpath.itd.AbstractItdMetadataProvider;
import org.springframework.roo.classpath.itd.ItdTypeDetailsProvidingMetadataItem;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.model.SpringJavaType;
import org.springframework.roo.project.LogicalPath;

/**
 * Provides {@link ElasticsearchEntityMetadata}. This type is called by Roo to retrieve the metadata for this add-on.
 * Use this type to reference external types and services needed by the metadata type. Register metadata triggers and
 * dependencies here. Also define the unique add-on ITD identifier.
 * @author Laurent Broudoux
 */
@Component
@Service
public final class ElasticsearchEntityMetadataProvider extends AbstractItdMetadataProvider {

   private static final FieldMatcher ID_FIELD_MATCHER = new FieldMatcher(IDENTIFIER_FIELD,
         AnnotationMetadataBuilder.getInstance(SpringJavaType.DATA_ID.getFullyQualifiedTypeName()));
   
   private static final MethodMatcher ID_ACCESSOR_MATCHER = new MethodMatcher(Arrays.asList(ID_FIELD_MATCHER), IDENTIFIER_ACCESSOR_METHOD, true);
   
   private static final MethodMatcher ID_MUTATOR_MATCHER = new MethodMatcher(Arrays.asList(ID_FIELD_MATCHER), IDENTIFIER_MUTATOR_METHOD, false);
   
   private static final AnnotatedTypeMatcher PERSISTENT_TYPE_MATCHER = new AnnotatedTypeMatcher(
         PERSISTENT_TYPE, ElasticsearchJavaType.ROO_ELASTICSEARCH_ENTITY);
 
    @Reference
    private CustomDataKeyDecorator customDataKeyDecorator;
   
    /**
     * The activate method for this OSGi component, this will be called by the OSGi container upon bundle activation 
     * (result of the 'addon install' command)
     * @param context the component context can be used to get access to the OSGi container (ie find out if certain bundles are active)
     */
    protected void activate(ComponentContext context) {
        metadataDependencyRegistry.registerDependency(PhysicalTypeIdentifier.getMetadataIdentiferType(), getProvidesType());
        addMetadataTrigger(new JavaType(RooElasticsearchEntity.class.getName()));
        customDataKeyDecorator.registerMatchers(getClass(),
              PERSISTENT_TYPE_MATCHER, ID_FIELD_MATCHER, ID_ACCESSOR_MATCHER,
              ID_MUTATOR_MATCHER);
    }
    
    /**
     * The deactivate method for this OSGi component, this will be called by the OSGi container upon bundle deactivation 
     * (result of the 'addon uninstall' command) 
     * @param context the component context can be used to get access to the OSGi container (ie find out if certain bundles are active)
     */
    protected void deactivate(ComponentContext context) {
        metadataDependencyRegistry.deregisterDependency(PhysicalTypeIdentifier.getMetadataIdentiferType(), getProvidesType());
        removeMetadataTrigger(new JavaType(RooElasticsearchEntity.class.getName())); 
        customDataKeyDecorator.unregisterMatchers(getClass());
    }
    
    /**
     * Return an instance of the Metadata offered by this add-on
     */
    protected ItdTypeDetailsProvidingMetadataItem getMetadata(String metadataIdentificationString, JavaType aspectName, PhysicalTypeMetadata governorPhysicalTypeMetadata, String itdFilename) {
        // Pass dependencies required by the metadata in through its constructor
        return new ElasticsearchEntityMetadata(metadataIdentificationString, aspectName, governorPhysicalTypeMetadata);
    }
    
    /**
     * Define the unique ITD file name extension, here the resulting file name will be **_ROO_Elasticsearch.aj
     */
    public String getItdUniquenessFilenameSuffix() {
        return "Elasticsearch_Entity";
    }

    protected String getGovernorPhysicalTypeIdentifier(String metadataIdentificationString) {
        JavaType javaType = ElasticsearchEntityMetadata.getJavaType(metadataIdentificationString);
        LogicalPath path = ElasticsearchEntityMetadata.getPath(metadataIdentificationString);
        return PhysicalTypeIdentifier.createIdentifier(javaType, path);
    }
    
    protected String createLocalIdentifier(JavaType javaType, LogicalPath path) {
        return ElasticsearchEntityMetadata.createIdentifier(javaType, path);
    }

    public String getProvidesType() {
        return ElasticsearchEntityMetadata.getMetadataIdentiferType();
    }
}