import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import jacamo.JacamoManager;
import jacamo.MasDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EngineRouter {

  private static final String APPLICATION_JSON = "application/json";

  private final JacamoManager jacamoManager;

  public EngineRouter(){
    jacamoManager = JacamoManager.getInstance();
  }

  public Router create(Vertx vertx){
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());

    router.get("/")
      .produces("text/html")
      .handler(ctx -> ctx.response().end("Hypermedia Agent Engine is running..."));

    router.post("/agents")
      .consumes(APPLICATION_JSON)
      .produces(APPLICATION_JSON)
      .handler(this::handlePostAgent);

    router.get("/agents/:adentId")
      .produces(APPLICATION_JSON)
      .handler(this::handleGetAgent);

    router.post("/mas")
      .consumes(APPLICATION_JSON)
      .produces(APPLICATION_JSON)
      .handler(this::handlePostMas);

    router.get("/mas/:masId")
      .produces(APPLICATION_JSON)
      .handler(this::handleGetMas);

    router.put("/mas/:masId/run")
      .produces(APPLICATION_JSON)
      .handler(this::handleRunMas);

    router.put("/mas/:masId/stop")
      .produces(APPLICATION_JSON)
      .handler(this::handleStopMas);

    return router;
  }

  private void handlePostAgent(RoutingContext ctx) {
    JsonObject body = ctx.getBodyAsJson();
    String id = body.getString("id");
    String code = body.getString("code");
    String xml = body.getString("xml");

    if(!jacamoManager.acceptAgentId(id)){
      ctx.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
    }

    if(!jacamoManager.getStorage().saveAgentCode(id, code)){
      ctx.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
    }

    JsonObject res = new JsonObject().clear()
      .put("id", id)
      .put("code", code)
      .put("xml", xml);

    ctx.response()
      .putHeader("Location", ctx.normalizedPath()+"/"+id)
      .setStatusCode(HttpResponseStatus.CREATED.code())
      .end(res.encodePrettily());
  }

  private void handleGetAgent(RoutingContext ctx) {
    String agentId = ctx.pathParam("agentId");

    Optional<String> code = jacamoManager.getStorage().getAgentCode(agentId);
    if(code.isEmpty()){
        ctx.response().setStatusCode(HttpResponseStatus.NOT_FOUND.code()).end();
    }
    String xml = "<xml></xml>"; //TODO retrieve this

    JsonObject res = new JsonObject().clear()
      .put("agentName", agentId)
      .put("code", code.get())
      .put("xml", xml);

    ctx.response()
      .setStatusCode(HttpResponseStatus.OK.code())
      .end(res.encodePrettily());
  }

  private void handlePostMas(RoutingContext ctx){
    JsonObject body = ctx.getBodyAsJson();
    String id = body.getString("id");
    JsonArray agents = body.getJsonArray("agents");
    List<String> agentNames = new ArrayList<>();
    for (int i = 0; i < agents.size(); i++) {
      agentNames.add(agents.getString(i));
    }
    if(agentNames.isEmpty()){
      ctx.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
    }
    if(!jacamoManager.acceptMasId(id)){
      ctx.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end();
    }
    if(this.jacamoManager.getStorage().saveMas(new MasDefinition(id, agentNames))){
      JsonObject res = new JsonObject()
        .put("id", id)
        .put("agents", agents)
        .put("running", false);
      ctx.response()
        .putHeader("Location", ctx.normalizedPath()+"/"+id)
        .setStatusCode(HttpResponseStatus.OK.code())
        .end();
    } else {
      ctx.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
    }
  }

  private void handleGetMas(RoutingContext ctx) {
    String masId = ctx.pathParam("masId");

    Optional<MasDefinition> mas = jacamoManager.getStorage().getMas(masId);
    if(mas.isEmpty()){
      ctx.response().setStatusCode(HttpResponseStatus.NOT_FOUND.code()).end();
    }

    JsonObject res = new JsonObject().clear()
      .put("masId", masId)
      .put("agents", mas.get().getAgents())
      .put("running", false); //TODO retrieve the status from the JacamoManager

    ctx.response()
      .setStatusCode(HttpResponseStatus.OK.code())
      .end(res.encodePrettily());
  }

  private void handleRunMas(RoutingContext ctx) {
    ctx.response().setStatusCode(HttpResponseStatus.NOT_IMPLEMENTED.code()).end();
  }

  private void handleStopMas(RoutingContext ctx) {
    ctx.response().setStatusCode(HttpResponseStatus.NOT_IMPLEMENTED.code()).end();
  }


}
