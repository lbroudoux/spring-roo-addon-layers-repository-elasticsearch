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

import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.model.JavaType;

/**
 * Locates Spring Data Elasticsearch Repositories within the user's project.
 * @author Laurent Broudoux
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
