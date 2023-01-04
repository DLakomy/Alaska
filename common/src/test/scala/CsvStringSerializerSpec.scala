package alaska.model

import alaska.typeclasses.{_, given}

class CsvStringSerializerSpec extends munit.FunSuite {
  test("Serializing number row") {
    val input = Valid.Num(1,23,66)
    val obtained = CsvStringSerializer.serialize(input)
    val expected = "1,23,66"
    assertEquals(obtained, expected)
  }

  test("Serializing text row") {
    val input = Valid.Text(1, 23, "66")
    val obtained = CsvStringSerializer.serialize(input)
    val expected = "1,23,\"66\""
    assertEquals(obtained, expected)
  }
}
