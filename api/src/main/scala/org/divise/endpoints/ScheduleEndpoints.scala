package org.divise.endpoints

import authentikat.jwt.JsonWebToken
import cats.data.EitherT
import cats.effect.Effect
import io.circe.{Decoder, Encoder}
import org.divise.{AppError, CirceBridge}
import org.divise.repositories.Schedule
import org.divise.services.ScheduleService
import org.http4s.{Header, HttpRoutes, Response}
import org.http4s.dsl.Http4sDsl
import cats.implicits._
import scala.language.higherKinds

class ScheduleEndpoints[F[_]: Effect](scheduleService: ScheduleService[F])
  extends Http4sDsl[F] with Serialization with CirceBridge{


  type Flow[T] = EitherT[F, AppError, T]
  implicit val schedulDecoder: Decoder[Schedule] = deriveDecoder
  implicit val scheduleEncoder: Encoder[Schedule] = deriveEncoder
  implicit val errEncoder: Encoder[AppError] = deriveEncoder

  def routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / "hello" / "ok"     =>
      getResp(scheduleService.findById(1))
  }

  def getResp[T](res: Flow[T])(implicit e: Encoder[T]): F[Response[F]] =
    res.value.flatMap(
      r =>
        r.fold(
          err => {
            Forbidden(err)
          },
          user =>
            Ok(user)
        )
    )

  case class AppVersion(version: String)

  def endpoints: HttpRoutes[F] = routes
}

object ScheduleEndpoints {
  def endpoints[F[_]: Effect](scheduleService: ScheduleService[F]): HttpRoutes[F] =
    new ScheduleEndpoints[F](scheduleService).endpoints
}