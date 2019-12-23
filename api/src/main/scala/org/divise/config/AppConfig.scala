package org.divise.config

import cats.effect.Async
import cats.effect.ContextShift
import cats.effect.Resource
import cats.effect.Sync
import cats.syntax.functor._
import doobie.hikari.HikariTransactor
import org.flywaydb.core.Flyway
import org.http4s.circe.CirceEntityDecoder._
import scala.concurrent.ExecutionContext
import scala.language.higherKinds

final case class AppConfig(db: DbConfig, server: ServerConfig, bot: BotConfig)
case class ServerConfig(host:  String, port:     Int)
case class DbConfig(
  url:                      String,
  driver:                   String,
  user:                     String,
  password:                 String,
  migrations:               String)
case class BotConfig(token: String, apiEndpoint: String)

object DbConfig {

  def dbTransactor[F[_]: Async: ContextShift](
    dbc:     DbConfig,
    connEc:  ExecutionContext,
    transEc: ExecutionContext
  ): Resource[F, HikariTransactor[F]] =
    HikariTransactor.newHikariTransactor[F](dbc.driver, dbc.url, dbc.user, dbc.password, connEc, transEc)

  def initializeDb[F[_]](cfg: DbConfig)(implicit S: Sync[F]): F[Unit] =
    S.delay {
        val fw: Flyway = {
          Flyway
            .configure()
            .locations(cfg.migrations)
            .dataSource(cfg.url, cfg.user, cfg.password)
            .load()
        }
        fw.migrate()
      }
      .as(())
}
