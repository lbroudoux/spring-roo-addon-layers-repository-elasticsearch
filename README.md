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

```
elasticsearch setup
```

```
entity elasticsearch --class ~.domain.Tweet
```

```
repository elasticsearch --interface ~.repository.TweetRepository --entity ~.domain.Tweet
```

```
service --interface ~.service.TweetService --entity ~.domain.Tweet
```



License
=======

```
This software is licensed under the Apache 2 license, quoted below.

Copyright 2013 Laurent Broudoux

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