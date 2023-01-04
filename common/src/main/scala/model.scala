package alaska.model

type ValidParse = Either[String, List[Valid.Entry]]

object Valid {
  sealed trait Entry
  case class Num(recNum: Integer, fieldNum: Integer, value: Integer) extends Entry
  case class Text(recNum: Integer, fieldNum: Integer, value: String) extends Entry
}
