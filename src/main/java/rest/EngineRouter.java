package rest;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import mas.SingleMasManager;
import mas.exceptions.AgentNotDefinedException;
import mas.exceptions.MasNotValidException;
import mas.jacamo.JacamoManager;
import mas.model.AgentDefinition;
import mas.model.AgentSource;
import mas.model.MasDefinition;
import mas.model.exceptions.AgentNameNotUniqueException;
import mas.runtime.bridge.exception.FailedCommunicationException;
import mas.runtime.exceptions.MasAlreadyRunningException;
import mas.runtime.exceptions.MasStartFailureException;
import mas.runtime.exceptions.NoMasRunningException;
import mas.storage.exceptions.ReservedIdException;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class EngineRouter {

  private static final String APPLICATION_JSON = "application/json";

  private final SingleMasManager masManager;

  public EngineRouter(){
    masManager = JacamoManager.getInstance();
  }

  public Router create(Vertx vertx){
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    router.get("/")
      .produces("text/html")
      .handler(ctx -> ctx.response().end("Hypermedia Agent Engine is running..."));

    //From now on only JSON endpoints
    router.route()
      .handler(ctx -> {
        ctx.response().putHeader("content-type", APPLICATION_JSON);
        ctx.next();
      });

    router.get("/agents")
      .produces(APPLICATION_JSON)
      .handler(this::handleGetAllAgents);

    router.get("/agents/:id")
      .produces(APPLICATION_JSON)
      .handler(this::handleGetAgent);

    router.put("/agents/:id")
      .produces(APPLICATION_JSON)
      .consumes(APPLICATION_JSON)
      .handler(this::handlePutAgent);

    router.get("/runtime")
      .produces(APPLICATION_JSON)
      .handler(this::handleGetRuntime);

    router.delete("/runtime")
      .produces(APPLICATION_JSON)
      .handler(this::handleDeleteRuntime);

    router.post("/runtime")
      .produces(APPLICATION_JSON)
      .consumes(APPLICATION_JSON)
      .handler(this::handlePostRuntime);

    router.get("/runtime/agents")
      .produces(APPLICATION_JSON)
      .handler(this::handleGetRuntimeAgents);

    router.post("/runtime/agents")
      .produces(APPLICATION_JSON)
      .consumes(APPLICATION_JSON)
      .blockingHandler(this::handleAddRuntimeAgent);

    router.get("/runtime/agents/:name")
      .produces(APPLICATION_JSON)
      .handler(this::handleGetRuntimeAgentByName);

    router.delete("/runtime/agents/:name")
      .produces(APPLICATION_JSON)
      .blockingHandler(this::handleDeleteRuntimeAgentByName);

    return router;
  }

  private void handleGetAllAgents(RoutingContext ctx) {
    JsonArray result = new JsonArray();
    masManager.getAvailableAgents().forEach(result::add);
    ctx.response()
      .setStatusCode(HttpResponseStatus.OK.code())
      .end(result.encode());
  }

  private void handleGetAgent(RoutingContext ctx) {
    try {
      Optional<AgentSource> result = masManager.getAgent(ctx.pathParam("id"));
      result.ifPresentOrElse(r -> ctx.response()
        .setStatusCode(HttpResponseStatus.OK.code())
        .end(getAgentSourceDTO(r).encode()),
        //not present
        ()-> ctx.response().setStatusCode(HttpResponseStatus.NOT_FOUND.code()).end()
      );
    } catch (IOException e) { //present but error reading
      ctx.response()
        .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
        .setStatusMessage("Error while retrieving the agent")
        .end();
    }
  }

  private void handlePutAgent(RoutingContext ctx) {
    AgentSource agent = new AgentSource(ctx.pathParam("id"),
      ctx.getBodyAsJson().getString("code"));
    try {
      masManager.saveAgent(agent);
      ctx.response()
        .setStatusCode(HttpResponseStatus.OK.code())
        .end(getAgentSourceDTO(agent).encode());
    } catch (IOException e) { //error writing
      ctx.response()
        .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
        .setStatusMessage("Error while saving the agent")
        .end();
    } catch (ReservedIdException e) { //cannot write on this id
      ctx.response()
        .setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
        .setStatusMessage("Invalid agent id: "+e.id)
        .end();
    }
  }

  private void handleGetRuntime(RoutingContext ctx) {
    this.masManager.getRuntime().ifPresentOrElse(
      mas -> ctx.response().setStatusCode(HttpResponseStatus.OK.code())
        .end(getMasDTO(mas).encode()),
      () -> ctx.response().setStatusCode(HttpResponseStatus.NOT_FOUND.code())
        .setStatusMessage("No runtime is running at the moment")
        .end()
    );
  }

  private void handlePostRuntime(RoutingContext ctx) {
    MasDefinition mas = masFromJSON(ctx.getBodyAsJson());
    try {
      this.masManager.startRuntime(mas);
      ctx.response().setStatusCode(HttpResponseStatus.OK.code()).end();
    } catch (MasAlreadyRunningException e) {
      ctx.response().setStatusCode(HttpResponseStatus.FORBIDDEN.code())
        .setStatusMessage("Runtime already running")
        .end();
    } catch (MasStartFailureException e) {
      ctx.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
        .setStatusMessage("Runtime failed to run")
        .end();
    } catch (IOException e) {
      ctx.response().setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
        .setStatusMessage("Cannot save run configuration")
        .end();
    } catch (MasNotValidException e) {
      ctx.response().setStatusCode(HttpResponseStatus.FORBIDDEN.code())
        .setStatusMessage(e.getMessage())
        .end();
    }
  }

  private void handleDeleteRuntime(RoutingContext ctx) {
    try {
      this.masManager.stopRuntime();
      ctx.response().setStatusCode(HttpResponseStatus.OK.code()).end();
    } catch (NoMasRunningException e) {
      ctx.response().setStatusCode(HttpResponseStatus.FORBIDDEN.code())
        .setStatusMessage("No runtime is running at the moment")
        .end();
    }
  }


  private void handleGetRuntimeAgents(RoutingContext ctx) {
    try {
      Set<AgentDefinition> agents = this.masManager.getRuntimeAgents();
      JsonArray array = new JsonArray();
      agents.forEach(a -> array.add(getAgentDefinitionDTO(a)));
      ctx.response().setStatusCode(HttpResponseStatus.OK.code())
        .end(array.encode());
    } catch (NoMasRunningException e) {
      ctx.response().setStatusCode(HttpResponseStatus.FORBIDDEN.code())
        .setStatusMessage("No runtime is running at the moment")
        .end();
    }
  }

  private void handleAddRuntimeAgent(RoutingContext ctx) {
    AgentDefinition agent = agentDefinitionFromJSON(ctx.getBodyAsJson());
    try {
      this.masManager.addAgentToRuntime(agent);
      ctx.response().setStatusCode(HttpResponseStatus.OK.code())
        .end(getAgentDefinitionDTO(agent).encode());
    } catch (NoMasRunningException e) {
      ctx.response().setStatusCode(HttpResponseStatus.FORBIDDEN.code())
        .setStatusMessage("No runtime is running at the moment")
        .end();
    } catch (AgentNameNotUniqueException e) {
      ctx.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
        .setStatusMessage("Name must be unique")
        .end();
    } catch (AgentNotDefinedException e) {
      ctx.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
        .setStatusMessage(e.agentType + " is not defined")
        .end();
    } catch (FailedCommunicationException e) {
      ctx.response().setStatusCode(HttpResponseStatus.FAILED_DEPENDENCY.code())
        .setStatusMessage("Failed to add the agent to the runtime")
        .end();
    }
  }

  private void handleGetRuntimeAgentByName(RoutingContext ctx) {
    try{
    Optional<AgentDefinition> agent = this.masManager.getRuntimeAgents().stream()
      .filter(a -> a.getName().equals(ctx.pathParam("name")))
      .findFirst();
    if(agent.isPresent()){
      ctx.response().setStatusCode(HttpResponseStatus.OK.code())
        .end(getAgentDefinitionDTO(agent.get()).encode());
    } else {
      ctx.response().setStatusCode(HttpResponseStatus.NOT_FOUND.code())
        .setStatusMessage("No agent with the given name is running")
        .end();
    }
    } catch (NoMasRunningException e) {
      ctx.response().setStatusCode(HttpResponseStatus.FORBIDDEN.code())
        .setStatusMessage("No runtime is running at the moment")
        .end();
    }
  }

  private void handleDeleteRuntimeAgentByName(RoutingContext ctx) {
    try {
      Optional<AgentDefinition> agent = this.masManager.getRuntimeAgents().stream()
        .filter(a -> a.getName().equals(ctx.pathParam("name")))
        .findFirst();
      if(agent.isPresent()) {
        this.masManager.removeAgentFromRuntime(ctx.pathParam("name"));
        ctx.response().setStatusCode(HttpResponseStatus.OK.code()).end();
      } else {
        ctx.response().setStatusCode(HttpResponseStatus.NOT_FOUND.code())
          .setStatusMessage("No agent with the given name is running")
          .end();
      }
    } catch (NoMasRunningException e) {
      ctx.response().setStatusCode(HttpResponseStatus.FORBIDDEN.code())
        .setStatusMessage("No runtime is running at the moment")
        .end();
    } catch (FailedCommunicationException e) {
      ctx.response().setStatusCode(HttpResponseStatus.FAILED_DEPENDENCY.code())
        .setStatusMessage("Failed to remove agent from runtime")
        .end();
    }
  }

  private MasDefinition masFromJSON(JsonObject json) {
    JsonArray array = json.getJsonArray("agents");
    Set<AgentDefinition> agents = new HashSet<>();
    for (int i = 0; i < array.size(); i++) {
      agents.add(agentDefinitionFromJSON(array.getJsonObject(i)));
    }
    return new MasDefinition(json.getString("id"), agents);
  }

  private AgentDefinition agentDefinitionFromJSON(JsonObject json) {
    return new AgentDefinition(json.getString("name"), json.getString("type"));
  }

  private AgentSource agentSourceFromJSON(JsonObject json) {
    return new AgentSource(json.getString("id"), json.getString("code"));
  }


  private JsonObject getMasDTO(MasDefinition mas){
    JsonArray agents = new JsonArray();
    mas.getAgents().stream().map(this::getAgentDefinitionDTO).forEach(agents::add);
    return new JsonObject()
      .put("id", mas.getId())
      .put("agents", agents);
  }

  private JsonObject getAgentDefinitionDTO(AgentDefinition agent){
    return new JsonObject()
      .put("name", agent.getName())
      .put("type", agent.getType());
  }

  private JsonObject getAgentSourceDTO(AgentSource agent){
    return new JsonObject()
      .put("id", agent.getId())
      .put("code", agent.getCode());
  }


}
