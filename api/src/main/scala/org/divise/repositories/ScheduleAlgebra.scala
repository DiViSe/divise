package org.divise.repositories

import cats.data.EitherT
import org.divise.AppError

import scala.language.higherKinds

trait ScheduleAlgebra[F[_]]{
  type Flow[T] = EitherT[F, AppError, T]

  def findScheduleById(id: Long): Flow[Schedule]

  def getSchedules: Flow[Seq[Schedule]]
}
