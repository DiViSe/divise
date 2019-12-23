package org.divise.repositories

import cats.Monad
import cats.data.EitherT
import doobie._
import doobie.implicits._
import doobie.util.transactor.Transactor
import org.divise.AppError

import scala.language.higherKinds

case class ScheduleRepository[F[_]: Monad](val xa: Transactor[F]) extends ScheduleAlgebra[F] {
  import ScheduleSQL._
  override def findScheduleById(id: Long): Flow[Schedule] =
    EitherT.fromOptionF(FIND_BY_ID(id).option.transact(xa), AppError(s"Schedule with id $id is not found"))

  override def getSchedules: Flow[Seq[Schedule]] = EitherT.liftF(SELECT_ALL.to[Seq].transact(xa))

}

object ScheduleRepository {
  def apply[F[_]: Monad](xa: Transactor[F]): ScheduleRepository[F] = new ScheduleRepository[F](xa)
}

object ScheduleSQL {
  val FIELDS: Fragment =
    fr"TITLE"

  def VALUES(schedule: Schedule): Fragment = {
    import schedule._
    fr"$title"
  }

  def INSERT(u: Schedule): doobie.Update0 =
    (fr"""INSERT INTO SCHEDULE (""" ++ FIELDS ++ fr""") VALUES (""" ++ VALUES(u) ++ fr""")""").update

  def UPDATE(schedule: Schedule): Update0 = {
    import schedule._
    (fr"""UPDATE SCHEDULE SET (""" ++ FIELDS ++ fr""")=(""" ++ VALUES(schedule) ++ fr""") WHERE ID = $id""").stripMargin.update
  }

  val USER_FIELDS: Fragment = fr"SELECT ID, TITLE FROM SCHEDULE"

  def FIND_BY_ID(id: Long): Query0[Schedule] = (USER_FIELDS ++ fr"""WHERE ID = $id""").query[Schedule]

  def SELECT_ALL: Query0[Schedule] = USER_FIELDS.query[Schedule]

}


