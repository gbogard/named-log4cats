package dev.guillaumebogard

package object namedlogger {
  type NamedLogger[F[_], Class] = LoggerForType[F, Class]
}
