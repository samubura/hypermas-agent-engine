package rest;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.Json;
import io.vertx.core.net.SocketAddress;
import io.vertx.ext.web.RoutingContext;

import java.text.DateFormat;
import java.util.Date;

public class RequestLogger implements Handler<RoutingContext> {

  private DateFormat dateTimeFormat;

  public static Handler<RoutingContext> create() {
    return new RequestLogger();
  }

  public RequestLogger(){
    this.dateTimeFormat = DateFormat.getDateTimeInstance();
  }

  @Override
  public void handle(RoutingContext context) {
    long timestamp = System.currentTimeMillis();
    String remoteClient = this.getClientAddress(context.request().remoteAddress());
    HttpMethod method = context.request().method();
    String uri = context.request().uri();
    context.addBodyEndHandler((handler) -> this.log(context, timestamp, remoteClient, method, uri));

    context.next();
  }

  private void log(RoutingContext context, long timestamp, String remoteClient, HttpMethod method, String uri) {
    HttpServerRequest request = context.request();
    int status = request.response().getStatusCode();
    String body = context.getBodyAsString().replaceAll("(\\r|\\n)", "");
    body = Json.encodePrettily(Json.decodeValue(body));
    String message = String.format("\n%s - - [%s] \"%s %s\" %d  %dms\n %s",
      remoteClient,
      this.dateTimeFormat.format(new Date(timestamp)),
      method,
      uri,
      status,
      System.currentTimeMillis() - timestamp,
      body);
    if(status > 400){
      System.err.println(message);
    } else {
      System.out.println(message);
    }
  }

  private String getClientAddress(SocketAddress inetSocketAddress) {
    return inetSocketAddress == null ? null : inetSocketAddress.host();
  }


}
