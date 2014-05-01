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

import static org.springframework.roo.model.SpringJavaType.AUTOWIRED;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetailsBuilder;
import org.springframework.roo.classpath.details.FieldMetadataBuilder;
import org.springframework.roo.classpath.details.annotations.AnnotationMetadataBuilder;
import org.springframework.roo.classpath.layers.CoreLayerProvider;
import org.springframework.roo.classpath.layers.LayerType;
import org.springframework.roo.classpath.layers.MemberTypeAdditions;
import org.springframework.roo.classpath.layers.MethodParameter;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.support.util.CollectionUtils;
import org.springframework.roo.support.util.PairList;

/**
 * A provider of the {@link LayerType#REPOSITORY} layer.
 * @author Laurent Broudoux
 */
@Component
@Service
public class RepositoryElasticsearchLayerProvider extends CoreLayerProvider {

   @Reference 
   private RepositoryElasticsearchLocator repositoryLocator;

   @Override
   public int getLayerPosition() {
      return LayerType.REPOSITORY.getPosition() - 2;
   }

   @Override
   public MemberTypeAdditions getMemberTypeAdditions(String callerMID,
         String methodIdentifier, JavaType targetEntity, JavaType idType,
         MethodParameter... methodParameters) {
      
      Validate.notBlank(callerMID, "Caller's metadata ID required");
      Validate.notBlank(methodIdentifier, "Method identifier required");
      Validate.notNull(targetEntity, "Target enitity type required");
      Validate.notNull(idType, "Enitity Id type required");

      // Look for a repository layer method with this ID and parameter types.
      final List<JavaType> parameterTypes = new PairList<JavaType, JavaSymbolName>(methodParameters).getKeys();
      final RepositoryElasticsearchLayerMethod method = RepositoryElasticsearchLayerMethod.valueOf(methodIdentifier, 
            parameterTypes, targetEntity, idType);
      if (method == null) {
         return null;
      }
      
      // Look for repositories that support this domain type.
      final Collection<ClassOrInterfaceTypeDetails> repositories = repositoryLocator.getRepositories(targetEntity);
      if (CollectionUtils.isEmpty(repositories)) {
         return null;
      }
      
      // Use the first such repository (could refine this later).
      final ClassOrInterfaceTypeDetails repository = repositories.iterator().next();
      
      // Return the additions the caller needs to make.
      return getMethodAdditions(callerMID, method, repository.getName(), Arrays.asList(methodParameters));
   }
   
   /**
    * Returns the additions that the caller needs to make in order to invoke the given method.
    * @param callerMID the caller's metadata ID (required)
    * @param method the method being called (required)
    * @param repositoryType the type of repository being called
    * @param parameterNames the parameter names used by the caller
    * @return a non-<code>null</code> set of additions
    */
   private MemberTypeAdditions getMethodAdditions(final String callerMID, final RepositoryElasticsearchLayerMethod method, 
         final JavaType repositoryType, final List<MethodParameter> parameters) {
      // Create a builder to hold the repository field to be copied into the caller.
      final ClassOrInterfaceTypeDetailsBuilder cidBuilder = new ClassOrInterfaceTypeDetailsBuilder(callerMID);
      final AnnotationMetadataBuilder autowiredAnnotation = new AnnotationMetadataBuilder(AUTOWIRED);
      final String repositoryFieldName = StringUtils.uncapitalize(repositoryType.getSimpleTypeName());
      cidBuilder.addField(new FieldMetadataBuilder(callerMID, 0, Arrays
              .asList(autowiredAnnotation), new JavaSymbolName(
              repositoryFieldName), repositoryType));

      // Create the additions to invoke the given method on this field
      final String methodCall = repositoryFieldName + "." + method.getCall(parameters, repositoryFieldName);
      return new MemberTypeAdditions(cidBuilder, method.getName(),
              methodCall, false, parameters);
   }
}
