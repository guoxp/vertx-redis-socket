package com.wangrupeng.main;

import com.wangrupeng.util.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;

/**
 * Created by WangRupeng on 2017/8/7 0007.
 */
public class VertRedis extends AbstractVerticle{
	public static void main(String[] args) {
		Runner.runExample(VertRedis.class);
	}

	@Override
	public void start(Future<Void> future) {
		//HttpServer server = vertx.createHttpServer();

		// register a handler for the incoming message the naming the Redis module will use is base address + '.' + redis channel
		vertx.eventBus().<JsonObject>consumer("io.vertx.redis.chatchannel", received -> {
			// do whatever you need to do with your message
			JsonObject value = received.body().getJsonObject("value");
			System.out.println(value);
			// the value is a JSON doc with the following properties
			// channel - The channel to which this message was sent
			// pattern - Pattern is present if you use psubscribe command and is the pattern that matched this message channel
			// message - The message payload
		});

		RedisOptions config = new RedisOptions()
				.setHost("192.168.1.6")
				.setPort(6379)
				.setAuth("password");

		RedisClient redisClient = RedisClient.create(vertx, config);

		redisClient.subscribe("chatchannel", res -> {
			if (res.succeeded()) {
				System.out.println("Subscribe channel : chatchannel" );
			}

			System.out.println(res.result());
		});
		/*redisClient.publish("chatchanne", res -> {

		});*/

		/*redisClient.get("chatchannel", res -> {
			if (res.succeeded()) {
				System.out.println(Json.encodePrettily(res.result()));
			}
		});*/


		//server.listen(3000);
	}
}
