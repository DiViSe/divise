package org.devise.endpoints

import cats.effect.Effect
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

import scala.language.higherKinds

class ScheduleEndpoints[F[_]: Effect]()
    extends Http4sDsl[F] {

  def routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / "hello" / "ok"     => Ok("Works fine\n")
  }
  case class AppVersion(version: String)

  def endpoints: HttpRoutes[F] = routes
}

object ScheduleEndpoints {
  def endpoints[F[_]: Effect](): HttpRoutes[F] =
    new ScheduleEndpoints[F]().endpoints
}