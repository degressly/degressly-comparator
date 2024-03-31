# Degressly Comparator

[!["Buy Me A Coffee"](https://www.buymeacoffee.com/assets/img/custom_images/orange_img.png)](https://www.buymeacoffee.com/daniyaalk)

Degressly comparator is a differential analysis add-on for [degressly-core](https://github.com/degressly/degressly-core). 
It filters out non-deterministic differences from recorded observations and persists them in a datastore of your choosing.

---

### Supported datasources
* Log file
* MongoDB

## Quick Start

Run application using ```mvn spring-boot:run```

### Config flags (VM options)

| Flag                   | Example              | Description                                                                                                                              |
|------------------------|----------------------|------------------------------------------------------------------------------------------------------------------------------------------|
| spring.profiles.active | mongo                | Profile activation depending on persistent datasource. Mongo credentials environment variables are mandatory if using the mongo profile. |
|diff.publisher.bootstrap-servers| localhost:9092       | Bootstrap servers for consuming observations from degressly-core and degressly-downstream.                                               |
|diff.publisher.topic-name| diff_stream(default) | Topic for consuming observations from.                                                                                                   |

### Config flags (Environment variables)

| Flag           | Description            |
|----------------|------------------------|
| MONGO_URL      | Mongo DB URL           |
| MONGO_USERNAME | Mongo DB Usernam       |
| MONGO_PASSWORD | Mongo DB Password      |
| MONGO_DBNAME   | Mongo DB Database Name |

## Known Issues
* Race condition in Mongo persister if two observations of the same trace arrive at the same time.

## Support

For queries, feature requests and more details on degressly, please visit [github.com/degressly/degressly-core](https://github.com/degressly/degressly-core).