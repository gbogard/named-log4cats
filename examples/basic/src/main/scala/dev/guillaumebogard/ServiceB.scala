package dev.guillaumebogard

import dev.guillaumebogard.namedlogger._

object ServiceB {

  def run[F[_]](implicit logger: NamedLogger[F, ServiceB.type]): F[Unit] =
    logger.info("Hello from ServiceB!")
}
