package alaska.model

import cats.data.NonEmptyList

type ParseResult = Either[String, NonEmptyList[Valid.Entry]]

object Valid {
  sealed abstract class Entry
  case class Num(recNum: Integer, fieldNum: Integer, value: Integer) extends Entry
  case class Text(recNum: Integer, fieldNum: Integer, value: String) extends Entry
}
