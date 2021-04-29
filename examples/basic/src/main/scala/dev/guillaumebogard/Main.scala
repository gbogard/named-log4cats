package dev.guillaumebogard

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import dev.guillaumebogard.namedlogger.{LoggerForType, NamedLogger}

object Main extends IOApp {

  // In the Main, we instantiate all the loggers we need
  implicit val aLogger: NamedLogger[IO, ServiceA[IO]] =
    LoggerForType.unsafeSlf4j
  implicit val bLogger: NamedLogger[IO, ServiceB.type] =
    LoggerForType.unsafeSlf4j

  def run(args: List[String]): IO[ExitCode] =
    new ServiceA[IO].run >> ServiceB.run[IO] >> ExitCode.Success.pure[IO]
}
