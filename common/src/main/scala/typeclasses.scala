package alaska.typeclasses

import alaska.model._

trait CsvStringSerializer[A] {
  def serialize(input: A): String
}

given CsvStringSerializer[Valid.Num] with {
  def serialize(input: Valid.Num): String =
    List(input.recNum, input.fieldNum, s"${input.value}").mkString(",")
}

given CsvStringSerializer[Valid.Text] with {
  def serialize(input: Valid.Text): String =
    List(input.recNum, input.fieldNum, s"\"${input.value}\"").mkString(",")
}

object CsvStringSerializer {
  def apply[A](using c: CsvStringSerializer[A]): CsvStringSerializer[A] = c
  def serialize[A](input: A)(using c: CsvStringSerializer[A]): String = c.serialize(input)
}
