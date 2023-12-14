package rest;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class ServerLauncher extends AbstractVerticle {

  private final int portNumber = 8088;

  @Override
  public void start(Promise<Void> startPromise) {

    EngineRouter engineRouter = new EngineRouter();

    vertx.createHttpServer()
      .requestHandler(engineRouter.create(vertx))
      .listen(portNumber, "0.0.0.0");

    System.out.println("Hypermas engine started on port "+portNumber+"...");
  }

}
