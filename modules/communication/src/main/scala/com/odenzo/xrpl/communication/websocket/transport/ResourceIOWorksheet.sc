import cats.effect
import cats.effect.*

import scala.concurrent.duration.*
import cats.effect.*
import cats.effect.syntax.all.given
class Main extends IOApp {
  val worker: ResourceIO[IO[OutcomeIO[Nothing]]] = IO(println("Running")).delayBy(10.seconds).foreverM.background

  val wr: Resource[ResourceIO, IO[OutcomeIO[Nothing]]] = Resource.eval(worker)

  override def run(args: List[String]): IO[ExitCode] =
    worker.use { (x: IO[OutcomeIO[Nothing]]) =>
      x.as[ExitCode.Success]
    }
}
