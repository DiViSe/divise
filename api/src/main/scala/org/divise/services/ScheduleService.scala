package org.divise.services

import cats.Monad
import cats.data.EitherT
import org.divise.AppError
import org.divise.repositories.{Schedule, ScheduleAlgebra}

import scala.language.higherKinds

class ScheduleService[F[_]: Monad](repo: ScheduleAlgebra[F]) extends ScheduleServiceAlgebra[F] {
  def findById(id: Long): Flow[Schedule] = repo.findScheduleById(id)
}

trait ScheduleServiceAlgebra[F[_]] {
  type Flow[T] = EitherT[F, AppError, T]

  def findById(id: Long): Flow[Schedule]

}

object ScheduleService {
  def apply[F[_]: Monad](userRepo: ScheduleAlgebra[F]): ScheduleService[F] = new ScheduleService[F](userRepo)
}
