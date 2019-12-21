package org.devise

import cats.effect._
import cats.implicits._
import com.typesafe.config.ConfigFactory
import doobie.util.ExecutionContexts
import io.circe.config.parser
import io.circe.generic.auto._
import org.devise.config.{AppConfig, DbConfig}
import org.devise.endpoints.ScheduleEndpoints
import org.http4s.circe.{CirceEntityDecoder, CirceEntityEncoder}
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware._
import org.http4s.server.{Router, Server => H4Server}
import slogging.{LogLevel, LoggerConfig, PrintLoggerFactory, StrictLogging}
import org.http4s.implicits._
import scala.language.higherKinds

object AppServer extends IOApp with CirceEntityDecoder with CirceEntityEncoder with StrictLogging {

  import org.slf4j.bridge.SLF4JBridgeHandler

  SLF4JBridgeHandler.install()
  LoggerConfig.factory = PrintLoggerFactory()
  LoggerConfig.level = LogLevel.DEBUG

  def createServer[F[_]: ContextShift: ConcurrentEffect: Timer](args: List[String]): Resource[F, H4Server[F]] = {
    logger.debug(s"Creating server using config: ${args.head}")

    for {
      conf   ← Resource.liftF(parser.decodePathF[F, AppConfig](ConfigFactory.load(args.head), "app"))
      te     ← ExecutionContexts.cachedThreadPool[F]
      ce     ← ExecutionContexts.fixedThreadPool[F](32)
      xa     ← DbConfig.dbTransactor(conf.db, ce, te)
      client ← BlazeClientBuilder[F](ce).resource
      services = ScheduleEndpoints.endpoints[F]()

      httpApp = Router("/" → CORS(services)).orNotFound
      _      ← Resource.liftF(DbConfig.initializeDb(conf.db))
      server ← BlazeServerBuilder[F].bindHttp(conf.server.port, conf.server.host).withHttpApp(httpApp).resource
    } yield server
  }

  def run(args: List[String]): IO[ExitCode] =
    createServer(args).use(_ ⇒ IO.never).as(ExitCode.Success)
}
