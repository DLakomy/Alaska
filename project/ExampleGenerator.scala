import scala.util.{Success, Try}
import scala.annotation.tailrec
import java.io._

object ExampleGenerator {

  private def exampleSegment(recNum: Integer): String =
    if (recNum%2==0)
      s"""Record: $recNum
         |P01: 321
         |P02: "sample text"
         |P03:  "sth"
         |%
         |""".stripMargin
    else
      s"""Record: $recNum
         |P02: "another sample text"
         |P01: -321
         |%
         |""".stripMargin

  private def intGenExampleFile(samplesCnt: Int, resultFilePath: String) = {
    val file = new File(resultFilePath)
    if (file.exists()) {
      throw new IOException(s"File ${file.getName} already exists in the given location")
    }

    val writer = new BufferedWriter(new PrintWriter(file))

    writer.write("Something to filter out at the beginning\n")

    @tailrec
    def go(num: Int): Unit =
      if (num < samplesCnt) {
        writer.write(exampleSegment(num+1))
        go(num+1)
      } else {
        writer.write("Something to filter out at the end\n")
        writer.close()
      }

    go(0)
  }

  def genExampleFile(args: Seq[String]): Unit = {

    val samplesCnt = Try(args(0).toInt).getOrElse(0)

    if (args.length==2 && samplesCnt > 0) {
      intGenExampleFile(samplesCnt, args(1))
    } else {
      throw new RuntimeException("Usage: genExampleFile <numberOfSamplesToConcat (>0)> <pathToResultFile>")
    }
  }
}
