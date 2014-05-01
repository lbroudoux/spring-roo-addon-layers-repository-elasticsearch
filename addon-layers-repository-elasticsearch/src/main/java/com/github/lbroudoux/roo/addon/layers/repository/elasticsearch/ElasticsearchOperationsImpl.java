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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.addon.propfiles.PropFileOperations;
import org.springframework.roo.classpath.PhysicalTypeCategory;
import org.springframework.roo.classpath.PhysicalTypeIdentifier;
import org.springframework.roo.classpath.TypeLocationService;
import org.springframework.roo.classpath.TypeManagementService;
import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetailsBuilder;
import org.springframework.roo.classpath.details.MethodMetadataBuilder;
import org.springframework.roo.classpath.details.annotations.AnnotationAttributeValue;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadataBuilder;
import org.springframework.roo.classpath.details.annotations.ClassAttributeValue;
import org.springframework.roo.classpath.itd.InvocableMemberBodyBuilder;
import org.springframework.roo.model.DataType;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.model.RooJavaType;
import org.springframework.roo.process.manager.FileManager;
import org.springframework.roo.process.manager.MutableFile;
import org.springframework.roo.project.Dependency;
import org.springframework.roo.project.Path;
import org.springframework.roo.project.PathResolver;
import org.springframework.roo.project.ProjectOperations;
import org.springframework.roo.project.Property;
import org.springframework.roo.project.Repository;
import org.springframework.roo.project.maven.Pom;
import org.springframework.roo.support.util.FileUtils;
import org.springframework.roo.support.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Implementation of operations this add-on offers.
 * @author Laurent Broudoux
 */
@Component 
@Service
public class ElasticsearchOperationsImpl implements ElasticsearchOperations {
    
    private static final String ELASTICSEARCH_XML = "applicationContext-es.xml";
    
    @Reference private FileManager fileManager;
    @Reference private PathResolver pathResolver;
    @Reference private ProjectOperations projectOperations;
    @Reference private PropFileOperations propFileOperations;

    /**
     * Use TypeLocationService to find types which are annotated with a given annotation in the project
     */
    @Reference private TypeLocationService typeLocationService;
    
    /**
     * Use TypeManagementService to change types
     */
    @Reference private TypeManagementService typeManagementService;

    /** {@inheritDoc} */
    public boolean isElasticsearchSetupAvailable() {
        // Check if a project has been created
        return projectOperations.isFocusedProjectAvailable();
    }

    /** {@inheritDoc} */
    public void createType(JavaType classType) {
        // Use Roo's Assert type for null checks
        Validate.notNull(classType, "Class type required");

        final String classIdentifier = typeLocationService.getPhysicalTypeCanonicalPath(classType, 
              pathResolver.getFocusedPath(Path.SRC_MAIN_JAVA));
        if (fileManager.exists(classIdentifier)) {
           return; // Type exists already - nothing to do
        }
        
        final String classMdId = PhysicalTypeIdentifier.createIdentifier(classType, pathResolver.getPath(classIdentifier));
        final ClassOrInterfaceTypeDetailsBuilder cidBuilder = new ClassOrInterfaceTypeDetailsBuilder(
              classMdId, Modifier.PUBLIC, classType, PhysicalTypeCategory.CLASS);
        
        // Make this type a java bean and a toString() implementor.
        cidBuilder.addAnnotation(new AnnotationMetadataBuilder(RooJavaType.ROO_JAVA_BEAN));
        cidBuilder.addAnnotation(new AnnotationMetadataBuilder(RooJavaType.ROO_TO_STRING));
        
        final List<AnnotationAttributeValue<?>> attributes = new ArrayList<AnnotationAttributeValue<?>>();
        cidBuilder.addAnnotation(new AnnotationMetadataBuilder(new JavaType(RooElasticsearchEntity.class), attributes));
        
        typeManagementService.createOrUpdateTypeOnDisk(cidBuilder.build());
    }
    
    /** {@inheritDoc} */
    public void createRepository(JavaType interfaceType, JavaType domainType){
        // Use Roo's Assert type for null checks
        Validate.notNull(interfaceType, "Interface type required");
        Validate.notNull(domainType, "Domain type required");
        
        final String interfaceIdentifier = pathResolver.getFocusedCanonicalPath(Path.SRC_MAIN_JAVA, interfaceType);
        if (fileManager.exists(interfaceIdentifier)) {
           return; // Type exists already - nothing to do
        }
        
        // Build interface type.
        final AnnotationMetadataBuilder interfaceAnnotationMetadata = new AnnotationMetadataBuilder(
              ElasticsearchJavaType.ROO_REPOSITORY_ELASTICSEARCH);
        interfaceAnnotationMetadata.addAttribute(new ClassAttributeValue(
              new JavaSymbolName("domainType"), domainType));
        final String interfaceMdId = PhysicalTypeIdentifier.createIdentifier(
              interfaceType, pathResolver.getPath(interfaceIdentifier));
        final ClassOrInterfaceTypeDetailsBuilder cidBuilder = new ClassOrInterfaceTypeDetailsBuilder(
              interfaceMdId, Modifier.PUBLIC, interfaceType,
              PhysicalTypeCategory.INTERFACE);
        cidBuilder.addAnnotation(interfaceAnnotationMetadata.build());
        
        final JavaType listType = new JavaType(Iterable.class.getName(), 0,
              DataType.TYPE, null, Arrays.asList(domainType));
        cidBuilder.addMethod(new MethodMetadataBuilder(interfaceMdId, 0,
              new JavaSymbolName("findAll"), listType,
              new InvocableMemberBodyBuilder()));
        typeManagementService.createOrUpdateTypeOnDisk(cidBuilder.build());
    }
    
    /** {@inheritDoc} */
    public void setup(Boolean local, String clusterNodes) {
       final String moduleName = projectOperations.getFocusedModuleName();
       writeProperties(clusterNodes, moduleName);
       manageDependencies(moduleName);
       manageAppCtx(local, clusterNodes, moduleName);
    }
    
    private void writeProperties(String clusterNodes, final String moduleName){
       // Write properties only if remote mode is specified.
       if (StringUtils.isNotBlank(clusterNodes)){
          final Map<String, String> properties = new HashMap<String, String>();
          properties.put("elasticsearch.clusterNodes", clusterNodes);
          propFileOperations.addProperties(Path.SPRING_CONFIG_ROOT
                  .getModulePathId(projectOperations.getFocusedModuleName()),
                  "database.properties", properties, true, false);
       }
    }
    
    private void manageDependencies(final String moduleName){
       final Element configuration = XmlUtils.getConfiguration(getClass());

       final List<Dependency> dependencies = new ArrayList<Dependency>();
       final List<Element> springDependencies = XmlUtils.findElements(
               "/configuration/spring-data-elasticsearch/dependencies/dependency",
               configuration);
       for (final Element dependencyElement : springDependencies) {
           dependencies.add(new Dependency(dependencyElement));
       }

       final List<Repository> repositories = new ArrayList<Repository>();
       final List<Element> repositoryElements = XmlUtils.findElements(
               "/configuration/spring-data-elasticsearch/repositories/repository",
               configuration);
       for (final Element repositoryElement : repositoryElements) {
           repositories.add(new Repository(repositoryElement));
       }

       projectOperations.addRepositories(moduleName, repositories);
       projectOperations.addDependencies(moduleName, dependencies);
       
       // spring-data-elasticsearch-1.0.0-M1 and other needs spring-context-3.2.5 or other.
       // However Spring ROO 1.2.2 is just on spring-context-3.1.1. We need to upgrade this if
       // user has not already pick an up-to-date version of Spring ...
       final Pom pom = projectOperations.getPomFromModuleName(moduleName);
       final Document document = XmlUtils.readXml(fileManager.getInputStream(pom.getPath()));
       final Element root = document.getDocumentElement();
       final Element value = XmlUtils.findFirstElement("/project/properties/spring.version", root);
       // Use a lexicographic comparison in case of already set.
       if (value == null || value.getTextContent().compareTo("3.2.5.RELEASE") < 0){
          projectOperations.addProperty(moduleName, new Property("spring.version", "3.2.5.RELEASE"));
       }
    }
    
    private void manageAppCtx(final Boolean local, final String clusterNodes, final String moduleName){
      final String appCtxId = pathResolver.getFocusedIdentifier(Path.SPRING_CONFIG_ROOT, ELASTICSEARCH_XML);
      
      if (!fileManager.exists(appCtxId)) {
         InputStream inputStream = null;
         OutputStream outputStream = null;
         try {
            inputStream = FileUtils.getInputStream(getClass(),
                  ELASTICSEARCH_XML);
            final MutableFile mutableFile = fileManager.createFile(appCtxId);
            String input = IOUtils.toString(inputStream);
            input = input.replace("TO_BE_CHANGED_BY_ADDON", projectOperations
                  .getTopLevelPackage(moduleName)
                  .getFullyQualifiedPackageName());
            outputStream = mutableFile.getOutputStream();
            IOUtils.write(input, outputStream);
         } catch (final IOException e) {
            throw new IllegalStateException("Unable to create file " + appCtxId);
         } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
         }
      }
      
      final Document document = XmlUtils.readXml(fileManager.getInputStream(appCtxId));
      final Element root = document.getDocumentElement();
      Element esClient = null;
      
      if (StringUtils.isNotBlank(clusterNodes)){
         esClient = document.createElement("elasticsearch:transport-client");
         esClient.setAttribute("id", "client");
         esClient.setAttribute("cluster-nodes", clusterNodes);
         root.appendChild(esClient);
      } else {
         esClient = document.createElement("elasticsearch:node-client");
         esClient.setAttribute("id", "client");
         esClient.setAttribute("local", "true");
         root.appendChild(esClient);
      }
      
      fileManager.createOrUpdateTextFileIfRequired(appCtxId, XmlUtils.nodeToString(document), false);
    }
}