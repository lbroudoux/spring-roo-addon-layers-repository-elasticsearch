spring-roo-addon-layers-repository-elasticsearch
================================================

Elasticsearch repository addon for Spring Roo.

A Spring Roo addon that brings an entity and repository layers backed by Elasticsearch.


Build Status
------------

Travis CI [![Build Status](https://travis-ci.org/lbroudoux/spring-roo-addon-layers-repository-elasticsearch.png?branch=master)](https://travis-ci.org/lbroudoux/spring-roo-addon-layers-repository-elasticsearch)


Getting Started
===============

Installation
------------

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