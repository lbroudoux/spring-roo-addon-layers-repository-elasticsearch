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

import org.springframework.roo.model.JavaType;

/**
 * Interface of operations this add-on offers. Typically used by a command type or an external add-on.
 * @author Laurent Broudoux
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