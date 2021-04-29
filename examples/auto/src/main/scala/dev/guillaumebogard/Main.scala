package dev.guillaumebogard

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import dev.guillaumebogard.namedlogger.{LoggerForType, NamedLogger}

import scala.reflect.ClassTag

object Main extends IOApp {

  implicit def logger[Class: ClassTag]: NamedLogger[IO, Class] = LoggerForType.unsafeSlf4j

  def run(args: List[String]): IO[ExitCode] =
    new ServiceA[IO].run >> ServiceB.run[IO] >> ExitCode.Success.pure[IO]
}
