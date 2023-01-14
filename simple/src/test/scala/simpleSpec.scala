package alaska.simple

import java.nio.file.{Files, Path}
import scala.io.Source
import scala.util.Using

class SimpleSpec extends munit.FunSuite {

  val tempDir: FunFixture[Path] = FunFixture (
    _ => Files.createTempDirectory("tests"),
    Files.deleteIfExists
  )

  tempDir.test("Regression test") { tmpDir =>

    val src = "example-files/ex1.lst"
    val srcAbs = Path.of(src).toAbsolutePath.toString

    val exPrefix = "example-files/ex1-results/"
    val exNumDest = exPrefix + "num.csv"
    val exTxtDest = exPrefix + "txt.csv"
    val exErrDest = exPrefix + "err.txt"

    val dstPrefix = tmpDir.toString
    val numDest = dstPrefix + "num.csv"
    val txtDest = dstPrefix + "txt.csv"
    val errDest = dstPrefix + "err.txt"

    program(srcAbs, numDest, txtDest, errDest)

    def safeFileToString(path: String): String =
      Using(Source.fromFile(path))(_.mkString).get

    val exampleNumCsv = safeFileToString(exNumDest)
    val exampleTxtCsv = safeFileToString(exTxtDest)
    val exampleErrLog = safeFileToString(exErrDest)
    val newNumCsv = safeFileToString(numDest)
    val newTxtCsv = safeFileToString(txtDest)
    val newErrLog = safeFileToString(errDest)

    assertEquals(exampleNumCsv, newNumCsv)
    assertEquals(exampleTxtCsv, newTxtCsv)
    assertEquals(exampleErrLog, newErrLog)
  }
}
