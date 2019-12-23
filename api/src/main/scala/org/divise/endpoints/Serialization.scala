package org.divise.endpoints

import io.circe.{Decoder, Encoder, ObjectEncoder}
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.decoding.ConfiguredDecoder
import io.circe.generic.extras.encoding.ConfiguredObjectEncoder
import shapeless.Lazy

trait Serialization {

  implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames.withDefaults

  def deriveDecoder[A](implicit decode: Lazy[ConfiguredDecoder[A]]): Decoder[A] =
    io.circe.generic.extras.semiauto.deriveDecoder[A]

  def deriveEncoder[A](implicit decode: Lazy[ConfiguredObjectEncoder[A]]): ObjectEncoder[A] =
    io.circe.generic.extras.semiauto.deriveEncoder[A]

}
