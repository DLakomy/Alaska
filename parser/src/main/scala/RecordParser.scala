package alaska.parser

import alaska.model._
import ast.*
import cats.data.NonEmptyList
import cats.parse.Parser as P
import cats.parse.Rfc5234.*
import cats.syntax.show._

object RecordParser {
  private val fieldId: P[Integer] = (P.char('P') *> digit.rep.string <* P.char(':') <* sp.rep.?).map(_.toInt)

  val textField: P[TextField] = (fieldId.soft ~ P.charsWhile(_!='"').string.surroundedBy(P.char('"'))).map {
    case (id, value) => TextField(id, value)
  }

  val numField: P[NumField] = (fieldId.soft ~ (P.char('-').? ~ digit.rep).string.map(_.toInt)).map {
    case (id, value) => NumField(id, value)
  }

  val fields: P[NonEmptyList[Field]] = ((textField | numField) <* lf).rep

  private val recHeader: P[Integer] = ((P.string("Record:") <* sp.rep.?) *> digit.rep.string).map(_.toInt)
  val record: P[Record] = ((recHeader <* lf) ~ fields <* P.string("%\n")).map {
    case (recId, fields) => Record(recId, fields)
  }

  private def recToModel(rec: Record): ValidResult = rec.fields.map {
    case TextField(id, value) => Valid.Text(rec.id, id, value)
    case NumField(id, value) => Valid.Num(rec.id, id, value)
  }
  def parseRecord(input: String): ParseResult = record.parseAll(input).match {
    case Right(rec) => Right(recToModel(rec))
    case Left(err) => Left(err.show)
  }
}
