package alaska.model

import cats.data.NonEmptyList

type ValidResult = NonEmptyList[Valid.Entry]
type ParseResult = Either[String, ValidResult]

object Valid {
  sealed abstract class Entry
  case class Num(recNum: Integer, fieldNum: Integer, value: Integer) extends Entry
  case class Text(recNum: Integer, fieldNum: Integer, value: String) extends Entry
}
