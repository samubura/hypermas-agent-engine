import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class EngineRouter {

  private static final String APPLICATION_JSON = "application/json";

  public EngineRouter(){

  }
  public Router create(Vertx vertx){
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());

    router.get("/")
      .produces("text/html")
      .handler(ctx -> {
        ctx.response().end("Hypermedia Agent Engine is running...");
      });

    router.post("/agent/:agentName")
      .consumes(APPLICATION_JSON)
      .produces(APPLICATION_JSON)
      .handler(this::handlePostAgent);

    router.get("/agent/:agentName")
      .produces(APPLICATION_JSON)
      .handler(this::handleGetAgent);
    return router;
  }

  private void handlePostAgent(RoutingContext ctx) {
    JsonObject body = ctx.getBodyAsJson();
    String agentName = ctx.pathParam("agentName");
    System.out.println("POST: " + agentName + "\n" + body.encodePrettily());

    //TODO actually do something

    JsonObject res = new JsonObject().clear()
      .put("agentName", agentName)
      .put("code", body.getValue("code"))
      .put("xml", body.getValue("xml"));

    ctx.response()
      .setStatusCode(HttpResponseStatus.CREATED.code())
      .end(res.encodePrettily());
  }
  private void handleGetAgent(RoutingContext ctx) {
    String agentName = ctx.pathParam("agentName");
    System.out.println("GET: " + agentName + "\n");

    //TODO retrieve this values
    String code = "...agentcode...";
    String xml = "<xml></xml>";

    JsonObject res = new JsonObject().clear()
      .put("agentName", agentName)
      .put("code", code)
      .put("xml", xml);

    ctx.response()
      .setStatusCode(HttpResponseStatus.OK.code())
      .end(res.encodePrettily());
  }
}
