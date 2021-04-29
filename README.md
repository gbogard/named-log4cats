# Named-log4Cats

A tiny wrapper around log4cats that binds a logger to specific type, allowing for named loggers to be easily passed around.

## tl;dr

Use an implicit `LoggerForType` in your code. Use the second type parameter to bind the logger
to a specific class/object in your code

```scala
class ServiceA[F[_]: Monad: MonadUsers](implicit logger: LoggerForType[F, ServiceA[F]]) {

  def run: F[Unit] = for {
    users <- MonadUsers[F].fetchUsers
    _ <- logger.info(s"Retrieved ${users.size} users")
  } yield ()
}
```

Instantiate the logger in your main

```scala
object Main extends IOApp {

  implicit def logger[Class: ClassTag]: NamedLogger[IO, Class] = LoggerForType.unsafeSlf4j

  def run(args: List[String]): IO[ExitCode] =
    new ServiceA[IO].run >> ServiceB.run[IO] >> ExitCode.Success.pure[IO]
}
```
And you will see properly named loggers in your traces

```
11:23:55.508 [io-compute-4] INFO dev.guillaumebogard.ServiceA - Hello from ServiceA!
11:23:55.511 [io-compute-4] WARN dev.guillaumebogard.ServiceA - Warning from ServiceA!
11:23:55.512 [io-compute-4] INFO dev.guillaumebogard.ServiceB$ - Hello from ServiceB!
```

See `examples/` for more examples.

## What problem does it solve?

Expressing logging as capbility of the program, using type classes, is neat. It allows us  to model programs using the
weakest constraints possible, rather than requiring powerful abilities like `cats.effect.Sync` everywhere. These programs
are in turn easier to test, because their external dependencies are easily mocked, and easier to reason about, because weaker
constraints tell us everything we need to know about a program's behaviour.

[Log4Cats](https://github.com/typelevel/log4cats) lets us abstract away the effect of logging using a type class:

```scala
def myPorgram[F[_]: MonadUser: MonadThrow: Logger]: F[Unit]
```

Here, my program can do exactly three things: manage users in some persistence layer, throw technical exceptions, and 
log things. The signature is self-documenting, neat!

However, passing a single Logger around in your program means all your traces will share a single name. If you
instantiate your logger in the Main of your application, your traces will look something like this:

```
DEBUG f.c.r.c.Main - Creating new user with id: e180ea47-f90f-4fee-8c1e-ffa21197b895 
DEBUG f.c.r.c.Main - Sending welcome e-mail for user with id: e180ea47-f90f-4fee-8c1e-ffa21197b895
```

Ideally we want the name of the logger to reflect the class where this code belongs, so the traces provide more useful
information, and we can tune our logging backend depending on the specific logger.

This is what this tiny library tries to solve.

