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

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.shell.CliAvailabilityIndicator;
import org.springframework.roo.shell.CliCommand;
import org.springframework.roo.shell.CliOption;
import org.springframework.roo.shell.CommandMarker;

/**
 * Commands for the Elasticsearch repository add-on.
 * @author Laurent Broudoux
 */
@Component
@Service
public class ElasticsearchCommands implements CommandMarker {
    
    /**
     * Get a reference to the ElasticsearchOperations from the underlying OSGi container
     */
    @Reference
    private ElasticsearchOperations operations;
    
    @CliAvailabilityIndicator({ "elasticsearch setup" })
    public boolean isElasticsearchSetupAvailable() {
        return operations.isElasticsearchSetupAvailable();
    }
    
    @CliAvailabilityIndicator({ "entity elasticsearch", "repository elasticsearch" })
    public boolean isEntityCommandAvailable() {
        return operations.isElasticsearchSetupAvailable();
    }
    
    @CliCommand(value = "elasticsearch setup", help = "Configures the project for Elasticsearch persistence.")
    public void setup(
          @CliOption(key = "local", mandatory = false, help = "Use a local instance of Elasticsearch") final Boolean local,
          @CliOption(key = "clusterNodes", mandatory = false, help = "List of cluster nodes with coma as separation") final String clusterNodes
          ) {
        operations.setup(local, clusterNodes);
    }
    
    @CliCommand(value = "entity elasticsearch", help = "Creates a domain entity which can be backed by an Elasticsearch repository")
    public void entity(
          @CliOption(key = "class", mandatory = true, optionContext = "update,project", help = "Implementation class for the specified interface") final JavaType classType
          ){
       operations.createType(classType);
    }
    
    @CliCommand(value = "repository elasticsearch", help = "Adds @RooElasticsearchRepository annotation to target type")
    public void repository(
          @CliOption(key = "interface", mandatory = true, help = "The java interface to apply this annotation to") final JavaType interfaceType,
          @CliOption(key = "entity", mandatory = true, help = "The domain entity this repository should expose") final JavaType domainType
          ){
       operations.createRepository(interfaceType, domainType);
    }
}