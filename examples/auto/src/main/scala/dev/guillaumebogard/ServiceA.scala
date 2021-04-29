package dev.guillaumebogard

import cats.Monad
import dev.guillaumebogard.namedlogger.LoggerForType
import cats.implicits._

class ServiceA[F[_]: Monad](implicit logger: LoggerForType[F, ServiceA[F]]) {

  def run: F[Unit] = {
    // One possible syntax is to use .apply
    LoggerForType[F, ServiceA[F]].info("Hello from ServiceA!") >>
    // Otherwise, use the named parameter
    logger.warn("Warning from ServiceA!")
  }

}
