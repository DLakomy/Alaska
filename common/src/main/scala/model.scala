package alaska.model

type ValidParse = Either[String, List[Valid.Entry]]

object Valid {
  sealed trait Entry
  case class Int(value: Integer) extends Entry
  case class Text(value: String) extends Entry
}
