package org.devise.endpoints

import enumeratum.values.{StringEnum, StringEnumEntry}

import scala.collection.immutable.IndexedSeq

sealed abstract class Url(val value: String) extends StringEnumEntry

object Url extends StringEnum[Url] {

  case object `dictionaries/speeches` extends Url("dictionaries/speeches")

  override val values: IndexedSeq[Url] = findValues
}
