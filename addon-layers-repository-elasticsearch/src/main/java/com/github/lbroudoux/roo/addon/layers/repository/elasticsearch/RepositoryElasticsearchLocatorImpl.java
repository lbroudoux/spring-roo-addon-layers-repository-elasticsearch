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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.classpath.TypeLocationService;
import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.DefaultPhysicalTypeMetadata;
import org.springframework.roo.model.JavaType;

/**
 * Locates Spring Data Elasticsearch Repositories within the user's project.
 * @author Laurent Broudoux
 */
@Component
@Service
public class RepositoryElasticsearchLocatorImpl implements RepositoryElasticsearchLocator {

   @Reference 
   private TypeLocationService typeLocationService;
   
   private final Map<JavaType, Set<ClassOrInterfaceTypeDetails>> cacheMap = new HashMap<JavaType, Set<ClassOrInterfaceTypeDetails>>();
   
   @Override
   public Collection<ClassOrInterfaceTypeDetails> getRepositories(JavaType domainType){
      if (!cacheMap.containsKey(domainType)) {
         cacheMap.put(domainType, new HashSet<ClassOrInterfaceTypeDetails>());
      }
      
      final Set<ClassOrInterfaceTypeDetails> existing = cacheMap.get(domainType);
      final Set<ClassOrInterfaceTypeDetails> located = typeLocationService
            .findClassesOrInterfaceDetailsWithAnnotation(ElasticsearchJavaType.ROO_REPOSITORY_ELASTICSEARCH);
      
      if (existing.containsAll(located)){
         return existing;
      }
      
      final Map<String, ClassOrInterfaceTypeDetails> toReturn = new HashMap<String, ClassOrInterfaceTypeDetails>();
      for (final ClassOrInterfaceTypeDetails cid : located) {
         final RepositoryElasticsearchAnnotationValues annotationValues = new RepositoryElasticsearchAnnotationValues(
               new DefaultPhysicalTypeMetadata(
                     cid.getDeclaredByMetadataId(),
                     typeLocationService.getPhysicalTypeCanonicalPath(cid.getDeclaredByMetadataId()), cid)
               );
         if (annotationValues.getDomainType() != null 
               && annotationValues.getDomainType().equals(domainType)) {
            toReturn.put(cid.getDeclaredByMetadataId(), cid);
         }
      }
      return toReturn.values();
   }
}
