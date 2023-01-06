package alaska.parser

import ast.*
import cats.data.NonEmptyList
import cats.parse.Parser

class RecordParserSpec extends munit.FunSuite {
  test("Parsing text field") {
    val input = "P02: \"sample text\""
    val obtained = RecordParser.textField.parseAll(input)
    val expected = Right(TextField(2, "sample text"))
    assertEquals(obtained, expected)
  }

  test("Parsing number field") {
    val input = "P13: 81"
    val obtained = RecordParser.numField.parseAll(input)
    val expected = Right(NumField(13, 81))
    assertEquals(obtained, expected)
  }

  test("Parsing multiple fields") {
    val input =
      """P12: "sample text"
        |P33: 66
        |P123: "another sample text"
        |""".stripMargin
    val obtained = RecordParser.fields.parseAll(input)
    val expected = Right(
      NonEmptyList.of(
        TextField(12, "sample text"),
        NumField(33, 66),
        TextField(123, "another sample text")
      )
    )
    assertEquals(obtained, expected)
  }

  test("Parsing a full record") {
    val input =
      """Record: 42
        |P12: "sample text"
        |P33: 66
        |P123: "another sample text"
        |%
        |""".stripMargin
    val obtained = RecordParser.record.parseAll(input)
    val expected = Right(
      Record( 42,
        NonEmptyList.of(
          TextField(12, "sample text"),
          NumField(33, 66),
          TextField(123, "another sample text")
        )
      )
    )
    assertEquals(obtained, expected)
  }
}
