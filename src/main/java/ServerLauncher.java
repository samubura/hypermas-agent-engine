import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class ServerLauncher extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    EngineRouter engineRouter = new EngineRouter();

    vertx.createHttpServer()
      .requestHandler(engineRouter.create(vertx))
      .listen(8088, "localhost");

    System.out.println("Hypermas engine started on port 8088...");
  }

}
