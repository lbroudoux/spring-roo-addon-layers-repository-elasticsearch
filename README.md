spring-roo-addon-layers-repository-elasticsearch
================================================

Elasticsearch repository addon for Spring Roo.

A Spring Roo addon that brings an entity and repository layers backed by [Elasticsearch](http://www.elasticsearch.org).


Build Status
------------

Travis CI [![Build Status](https://travis-ci.org/lbroudoux/spring-roo-addon-layers-repository-elasticsearch.png?branch=master)](https://travis-ci.org/lbroudoux/spring-roo-addon-layers-repository-elasticsearch)


Getting Started
===============

Installation
------------

For now, start fetching the code from Github using this Git Url : https://www.github.com/lbroudoux/spring-roo-addon-layers-repository-elasticsearch.git. Then build addon using Maven with this command :

```sh
mvn clean install
```

This should add the addon artifact to your local Maven repository (`$MAVEN_REPO` in the following).

Once addon built, start a new Roo project or use an existing one. Install addon by typing in a Roo shell :

```sh
roo> osgi start --url file://$MAVEN_REPO/com/github/lbroudoux/roo/addon/com.github.lbroudoux.roo.addon.layers.repository.elasticsearch/0.1.0.BUILD-SNAPSHOT/com.github.lbroudoux.roo.addon.layers.repository.elasticsearch-0.1.0.BUILD-SNAPSHOT.jar
```

Check that addon is successfully installed by typing :

```sh
roo> osgi ps
```

you should see a line like this one telling that addon is installed :

```sh
...
[  75] [Active     ] [    1] RooElasticsearch (0.1.0.BUILD-SNAPSHOT)
...
```


Commands
--------

In order to explain commands, the easiest is to illustrate their usage through a fully working example. So here it is : the classical Twitter example ! 

First step : create a new Spring roo project using the project command within the Roo shell.
```
project --topLevelPackage com.github.lbroudoux.es
```

Now, setup this plugin using this simple command. This command assume the default, quick starting options : Elasticsearch will be ran in local mode within your Spring Roo app JVM. 
```
elasticsearch setup
```
However, you may want a way more elaborate (and recommanded !) configuration that employs a cluster of Elasticsearch nodes. Just use this variation :
```
elasticsearch setup --local false --clusterNodes mynode1:9300,mynode2:9300
```
Tadam ! The configuration is handled for you !


Next step consists in declaring the domain class you want to persist within Elasticsearch. For that, you'll just have to use the `entity` command of Roo, specifying you want an Elasticsearch entity like that :
```
entity elasticsearch --class ~.domain.Tweet
```
You can now use traditional Roo commands for defining fields and constraints applying to the domain objects you will store :
```
field string --fieldName content --sizeMax 140
``` 

For now, we'll create the repository layer for our `Tweet` domain objects like any other repository in Roo, just telling we want an Elasticsearch one :
```
repository elasticsearch --interface ~.repository.TweetRepository --entity ~.domain.Tweet
```

Finaly, if we wan't a CRUD service layer for our domain object (usefull if you want to start with Roo web scaffolding), you'll have to add this 3 commands :  
```
service --interface ~.service.TweetService --entity ~.domain.Tweet
web mvc setup
web mvc all --package ~.web
```

Now just start your Roo application within your favorite container (or via `mvn tomcat:run`), open your browser and go to `http://localhost:8080/es` : you have a fully workingSpring application that persists its domain objects within Elasticsearch !  


License
=======

```
This software is licensed under the Apache 2 license, quoted below.

Copyright 2014 Laurent Broudoux

Licensed under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License. You may obtain a copy of
the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
License for the specific language governing permissions and limitations under
the License.
```