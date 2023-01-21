package alaska.typeclasses

import alaska.model._

trait CsvStringSerializer[A] {
  def serialize(input: A): String
}

given CsvStringSerializer[Valid.Num] with {
  def serialize(input: Valid.Num): String =
    List(input.recNum, input.fieldNum, s"${input.value}").mkString("", ",", "\n")
}

given CsvStringSerializer[Valid.Text] with {
  def serialize(input: Valid.Text): String =
    List(input.recNum, input.fieldNum, s"\"${input.value}\"").mkString("", ",", "\n")
}

// TODO compiler couldn't figure this out
// CsvStringSerializer.serialize(entry) fails if entry is an instance of Valid.Entry
// this code smells a bit; at lest non exhaustive match is caught
given CsvStringSerializer[Valid.Entry] with {
  def serialize(input: Valid.Entry): String = input match
    case n: Valid.Num => CsvStringSerializer.serialize(n)
    case t: Valid.Text => CsvStringSerializer.serialize(t)
}

object CsvStringSerializer {
  def serialize[A](input: A)(using c: CsvStringSerializer[A]): String = c.serialize(input)
}
