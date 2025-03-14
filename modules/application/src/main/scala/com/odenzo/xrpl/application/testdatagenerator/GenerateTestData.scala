package com.odenzo.xrpl.application.testdatagenerator

import cats.effect
import cats.effect.*
import cats.effect.std.Supervisor
import cats.effect.syntax.all.given

import scala.concurrent.duration.*

/**
  * This is an application to generate request and response data and save to
  * disk to allow some offline testing.
  */
object GenerateTestData extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    val supervisor: Resource[IO, Supervisor[IO]] = Supervisor[IO](await = false)
    val worker: IO[Nothing]                      =
      IO(println("Running")).delayBy(5.seconds).foreverM.onCancel(IO(println("Worker Canneled")))

    val result: IO[Fiber[IO, Throwable, Nothing]] = supervisor.use { (x: Supervisor[IO]) =>
      val supervized: IO[Fiber[IO, Throwable, Nothing]] = x.supervise(worker)
      supervized <* IO.sleep(20.seconds) <* IO(println("Main Worker Done"))
    }

    result *> IO(println("Outside of Resource Usage. Going to Sleep 15 Seconds")).as(ExitCode.Success)
  }

}

class DummyEngine() {
  def doSend: IO[Unit] = IO(println("sending Somethine"))
}
