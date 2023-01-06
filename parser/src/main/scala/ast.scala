package alaska.parser.ast

import cats.data.NonEmptyList

sealed abstract class Field(id: Integer)
case class TextField(id: Integer, value: String) extends Field(id)
case class NumField(id: Integer, value: Integer) extends Field(id)

case class Record(id: Integer, fields: NonEmptyList[Field])
