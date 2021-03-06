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

import static org.springframework.roo.classpath.customdata.CustomDataKeys.COUNT_ALL_METHOD;
import static org.springframework.roo.classpath.customdata.CustomDataKeys.FIND_ALL_METHOD;
import static org.springframework.roo.classpath.customdata.CustomDataKeys.FIND_ENTRIES_METHOD;
import static org.springframework.roo.classpath.customdata.CustomDataKeys.FIND_METHOD;
import static org.springframework.roo.classpath.customdata.CustomDataKeys.MERGE_METHOD;
import static org.springframework.roo.classpath.customdata.CustomDataKeys.PERSIST_METHOD;
import static org.springframework.roo.classpath.customdata.CustomDataKeys.REMOVE_METHOD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.springframework.roo.classpath.customdata.tagkeys.MethodMetadataCustomDataKey;
import org.springframework.roo.classpath.layers.LayerType;
import org.springframework.roo.classpath.layers.MethodParameter;
import org.springframework.roo.model.DataType;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;

/**
 * A method provided by the {@link LayerType#REPOSITORY} layer.
 * @author Laurent Broudoux
 */
public enum RepositoryElasticsearchLayerMethod {

   COUNT("count", COUNT_ALL_METHOD) {
      @Override
      public String getCall(List<MethodParameter> parameters, String repositoryFieldName) {
         return "count()";
      }
      @Override
      public List<JavaSymbolName> getParameterNames(JavaType entityType, JavaType idType) {
         return Collections.emptyList();
      }
      @Override
      protected List<JavaType> getParameterTypes(JavaType targetEntity, JavaType idType) {
         return Collections.emptyList();
      }
      @Override
      public JavaType getReturnType(JavaType entityType) {
         return JavaType.LONG_PRIMITIVE;
      }
      
   },
   DELETE("delete", REMOVE_METHOD) {
      @Override
      public String getCall(List<MethodParameter> parameters, String repositoryFieldName) {
         return "delete(" + parameters.get(0).getValue() + ")";
      }
      @Override
      public List<JavaSymbolName> getParameterNames(JavaType entityType, JavaType idType) {
         return Arrays.asList(JavaSymbolName.getReservedWordSafeName(entityType));
      }
      @Override
      protected List<JavaType> getParameterTypes(JavaType targetEntity, JavaType idType) {
         return Arrays.asList(targetEntity);
      }
      @Override
      public JavaType getReturnType(JavaType entityType) {
         return JavaType.VOID_PRIMITIVE;
      }
   },
   FIND("find", FIND_METHOD) {
      @Override
      public String getCall(final List<MethodParameter> parameters, String repositoryFieldName) {
         return "findOne(" + parameters.get(0).getValue() + ")";
      }
      @Override
      public List<JavaSymbolName> getParameterNames(final JavaType entityType, final JavaType idType) {
         return Arrays.asList(new JavaSymbolName("id"));
      }
      @Override
      protected List<JavaType> getParameterTypes(final JavaType entityType, final JavaType idType) {
         return Arrays.asList(idType);
      }
      @Override
      public JavaType getReturnType(final JavaType entityType) {
         return entityType;
      }
   },
   FIND_ALL("findAll", FIND_ALL_METHOD) {
      @Override
      public String getCall(final List<MethodParameter> parameters, String repositoryFieldName) {
         //return "findAll()";
         return "findAll(new org.springframework.data.domain.PageRequest("
            + "0, Math.max(1, (int)" + repositoryFieldName + ".count()))).getContent()";
      }
      @Override
      public List<JavaSymbolName> getParameterNames(final JavaType entityType, final JavaType idType) {
         return Collections.emptyList();
      }
      @Override
      protected List<JavaType> getParameterTypes(final JavaType targetEntity, final JavaType idType) {
         return Collections.emptyList();
      }
      @Override
      public JavaType getReturnType(final JavaType entityType) {
         return new JavaType(Iterable.class.getName(), 0, DataType.TYPE, null, Arrays.asList(entityType));
      }
   },
   /**
    * Finds entities starting from a given zero-based index, up to a given
    * maximum number of results. This method isn't directly implemented by
    * Spring Data JPA, so we use its findAll(Pageable) API.
    */
   FIND_ENTRIES("findEntries", FIND_ENTRIES_METHOD) {
      @Override
      public String getCall(final List<MethodParameter> parameters, String repositoryFieldName) {
          final JavaSymbolName firstResultParameter = parameters.get(0).getValue();
          final JavaSymbolName maxResultsParameter = parameters.get(1).getValue();
          final String pageNumberExpression = firstResultParameter + " / " + maxResultsParameter;
          return "findAll(new org.springframework.data.domain.PageRequest("
                  + pageNumberExpression + ", " + maxResultsParameter
                  + ")).getContent()";
      }
      @Override
      public List<JavaSymbolName> getParameterNames(final JavaType entityType, final JavaType idType) {
         return Arrays.asList(new JavaSymbolName("firstResult"), new JavaSymbolName("maxResults"));
      }
      @Override
      protected List<JavaType> getParameterTypes(final JavaType targetEntity, final JavaType idType) {
         return Arrays.asList(JavaType.INT_PRIMITIVE, JavaType.INT_PRIMITIVE);
      }
      @Override
      public JavaType getReturnType(final JavaType entityType) {
         return JavaType.listOf(entityType);
      }
   },
   /**
    * Spring Data JPA makes no distinction between
    * create/persist/save/update/merge
    */
   SAVE("save", MERGE_METHOD, PERSIST_METHOD) {
      @Override
      public String getCall(final List<MethodParameter> parameters, String repositoryFieldName) {
         return "save(" + parameters.get(0).getValue() + ")";
      }
      @Override
      public List<JavaSymbolName> getParameterNames(final JavaType entityType, final JavaType idType) {
         return Arrays.asList(JavaSymbolName.getReservedWordSafeName(entityType));
      }
      @Override
      protected List<JavaType> getParameterTypes(final JavaType targetEntity, final JavaType idType) {
         return Arrays.asList(targetEntity);
      }
      @Override
      public JavaType getReturnType(final JavaType entityType) {
         return JavaType.VOID_PRIMITIVE;
      }
   };
   
   
   /**
    * Returns the {@link RepositoryElasticsearchLayerMethod} with the given ID and parameter types.
    * @param methodId the ID to match upon
    * @param parameterTypes the parameter types to match upon
    * @param targetEntity the entity type being managed by the repository
    * @param idType specifies the ID type used by the target entity (required)
    * @return <code>null</code> if no such method exists
    */
   public static RepositoryElasticsearchLayerMethod valueOf(final String methodId,
         final List<JavaType> parameterTypes, final JavaType targetEntity, final JavaType idType) {
      for (final RepositoryElasticsearchLayerMethod method : values()) {
         if (method.ids.contains(methodId)
               && method.getParameterTypes(targetEntity, idType).equals(parameterTypes)) {
            return method;
         }
      }
      return null;
   }
   
   private final List<String> ids;
   private final String name;
   
   /**
    * Constructor.
    * @param key the unique key for this method (required)
    * @param name the Java name of this method (required)
    */
   private RepositoryElasticsearchLayerMethod(final String name, final MethodMetadataCustomDataKey... keys) {
      Validate.notBlank(name, "Name is required");
      Validate.isTrue(keys.length > 0, "One or more ids are required");
      ids = new ArrayList<String>();
      for (final MethodMetadataCustomDataKey key : keys) {
         ids.add(key.name());
      }
      this.name = name;
   }
       
   /**
    * Returns the name of this method
    * @return a non-blank name
    */
   public String getName(){
      return name;
   }
   
   /**
    * Returns a Java snippet that invokes this method (minus the target)
    * @param parameters the parameters used by the caller; can be
    * <code>null</code>
    * @param repositoryFieldName The name of the repository within aspect
    * @return a non-blank Java snippet
    */
   public abstract String getCall(List<MethodParameter> parameters, String repositoryFieldName);

   /**
    * Returns the names of this method's declared parameters
    * @param entityType the type of domain entity managed by the service
    * (required)
    * @param idType specifies the ID type used by the target entity (required)
    * @return a non-<code>null</code> list (might be empty)
    */
   public abstract List<JavaSymbolName> getParameterNames(JavaType entityType, JavaType idType);

   /**
    * Instances must return the types of parameters they take
    * @param targetEntity the type of entity being managed (required)
    * @param idType specifies the ID type used by the target entity (required)
    * @return a non-<code>null</code> list
    */
   protected abstract List<JavaType> getParameterTypes(JavaType targetEntity, JavaType idType);

   /**
    * Returns this method's return type
    * @param entityType the type of entity being managed
    * @return a non-<code>null</code> type
    */
   public abstract JavaType getReturnType(JavaType entityType);
}
