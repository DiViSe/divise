package org.divise
import cats.Applicative
import cats.effect.Sync
import io.circe.Decoder
import io.circe.Encoder
import org.http4s.EntityDecoder
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf
import org.http4s.circe.jsonOf

trait CirceBridge {
  /**
   * Bridge method that provide implicit EntityEncoder[Task, Entity] for any type Entity where circe's Encoder[Entity] is available
   */
  protected implicit def encoderOf[Task[_]: Applicative, Entity: Encoder]: EntityEncoder[Task, Entity] =
    jsonEncoderOf[Task, Entity]

  /**
   * Bridge method that provide implicit EntityDecoder[Task, Entity] for any type Entity where circe's Encoder[Entity] is available
   */
  protected implicit def decoderOf[Task[_]: Applicative: Sync, Entity: Decoder]: EntityDecoder[Task, Entity] =
    jsonOf[Task, Entity]
}
