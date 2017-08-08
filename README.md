# vertx-redis-socket

This is a sample that uses the Vert.x(3.x) as a socket server. It can receive the redis message(you have to subscribe the redis channel first) and then send this message to the front-end.

I assumed that you have installed the redis and know how to pub/sub messages.

This program allow you to receive redis client message(can be realized by Python, Java or any other language) and resent to the front.
# dependencies

#### Maven
```xml
<dependency>
    <groupId>io.vertx</groupId>
    <artifactId>vertx-redis-client</artifactId>
    <version>3.4.2</version>
</dependency>

<dependency>
    <groupId>io.vertx</groupId>
    <artifactId>vertx-web</artifactId>
    <version>3.4.2</version>
</dependency>
```
# configuration

```java
//redis 
RedisOptions config = new RedisOptions()
            .setHost("ip")
            .setPort(6379)
            .setAuth("password");
```



