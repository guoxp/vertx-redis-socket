package com.wangrupeng.main;

import com.wangrupeng.util.FileLogger;
import com.wangrupeng.util.Runner;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeEventType;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;

/*
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class VertSocket extends AbstractVerticle {
  private static FileLogger fileLogger = new FileLogger("/home/hadoop/wrp/redis-listener/redis-log/VertSocket.log");
  private static String TAG = "VertSocket";
  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(VertSocket.class);
  }

  @Override
  public void start() throws Exception {
    int port = 3000;
    String channel = "realtime";
    RedisOptions config = new RedisOptions()
            .setHost("192.168.1.6")
            .setPort(6379)
            .setAuth("hadoop@oceanai");

    Router router = Router.router(vertx);

    BridgeOptions options = new BridgeOptions().addOutboundPermitted(new PermittedOptions().setAddress("news-feed"));
    router.route("/eventbus/*").handler(SockJSHandler.create(vertx).bridge(options, event -> {
      if (event.type() == BridgeEventType.SOCKET_CREATED) {
        //System.out.println("A socket was created");
        fileLogger.log(TAG, "A socket was created");
      }
      // This signals that it's ok to process the event
      event.complete(true);
    }));

    // Serve the static resources
    router.route().handler(StaticHandler.create());
    vertx.createHttpServer().requestHandler(router::accept).listen(port);
    vertx.eventBus().<JsonObject>consumer("io.vertx.redis." + channel, received -> {
      JsonObject value = received.body().getJsonObject("value");
      //System.out.println(value);
      fileLogger.log(TAG, "Value : " + value);
      vertx.eventBus().publish("news-feed", value.getValue("message"));

    });

    //redis client subscribe a channel
    RedisClient redisClient = RedisClient.create(vertx, config);

    redisClient.subscribe(channel, res -> {
      if (res.succeeded()) {
        //System.out.println("Subscribe channel : " + channel + ", address is http://192.168.1.6:" + port + "/eventbus" );
        fileLogger.log(TAG, "Subscribe channel : " + channel + ", address is http://192.168.1.6:" + port + "/eventbus" );
      }
      //System.out.println(res.result());
      fileLogger.log(TAG, "Result : " + res.result());
    });
  }
}
