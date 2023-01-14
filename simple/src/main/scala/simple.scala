package alaska.simple

import alaska.model.Valid
import alaska.parser.RecordParser
import alaska.typeclasses.{CsvStringSerializer, given}

import java.io.{BufferedReader, BufferedWriter, FileReader, FileWriter}

// I've never wrote a Scala code this ugly...
// this one is meant to be simple, as a reference for the second implementation

// TODO header for csv
def program(sourcePath: String, numPath: String, txtPath: String, errPath: String): Unit = {

  val source = BufferedReader(FileReader(sourcePath))
  val numDest = BufferedWriter(FileWriter(numPath))
  val txtDest = BufferedWriter(FileWriter(txtPath))
  val errDest = BufferedWriter(FileWriter(errPath))

  def handleRecord(record: String): Unit =
    val parsed = RecordParser.parseRecord(record)
    parsed match
      case Left(error) => errDest.write(error+'\n')
      case Right(entries) => entries.toList.foreach { entry =>
        val serialized = CsvStringSerializer.serialize(entry)
        entry match
          case _: Valid.Num => numDest.write(serialized+'\n')
          case _: Valid.Text => txtDest.write(serialized+'\n')
      }

  var line = source.readLine()
  val accumulator: StringBuilder = new StringBuilder
  while (line != null) {
    if (line matches "^Record.*") accumulator.append(line)+='\n'
    else if (line == "%") {
      accumulator.append(line)+='\n'
      handleRecord(accumulator.toString())
      accumulator.clear()
    }
    // ...nonEmpty handles filtering garbage between records
    else if (accumulator.nonEmpty) accumulator.append(line)+='\n'
    line = source.readLine()
  }
  numDest.flush()
  txtDest.flush()
  errDest.flush()
}
