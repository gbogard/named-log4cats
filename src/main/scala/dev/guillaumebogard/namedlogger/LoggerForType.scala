package dev.guillaumebogard.namedlogger

import cats.effect.kernel.Sync
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.{ErrorLogger, Logger, MessageLogger}

import scala.reflect.ClassTag

final case class LoggerForType[F[_], T](logger: Logger[F]) extends Logger[F] {
  def error(message: => String): F[Unit] = logger.error(message)
  def warn(message: => String): F[Unit] = logger.warn(message)
  def info(message: => String): F[Unit] = logger.info(message)
  def debug(message: => String): F[Unit] = logger.debug(message)
  def trace(message: => String): F[Unit] = logger.trace(message)
  def error(t: Throwable)(message: => String): F[Unit] =
    logger.error(t)(message)
  def warn(t: Throwable)(message: => String): F[Unit] = logger.warn(t)(message)
  def info(t: Throwable)(message: => String): F[Unit] = logger.info(t)(message)
  def debug(t: Throwable)(message: => String): F[Unit] =
    logger.debug(t)(message)
  def trace(t: Throwable)(message: => String): F[Unit] =
    logger.trace(t)(message)
}

object LoggerForType {
  def apply[F[_], T](implicit
      loggerForClass: LoggerForType[F, T]
  ): LoggerForType[F, T] = loggerForClass

  def unsafeSlf4j[F[_]: Sync, T](implicit
      tag: ClassTag[T]
  ): LoggerForType[F, T] =
    LoggerForType(Slf4jLogger.getLoggerFromClass(tag.runtimeClass))

  def safeSlf4j[F[_]: Sync, T](implicit
      tag: ClassTag[T]
  ): F[LoggerForType[F, T]] =
    Sync[F].delay(unsafeSlf4j[F, T])
}
