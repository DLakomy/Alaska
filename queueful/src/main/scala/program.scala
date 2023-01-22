package alaska.queueful

import alaska.model.Valid
import alaska.parser.RecordParser
import alaska.typeclasses.{CsvStringSerializer, given}
import cats.effect.{Clock, IO, IOApp, Resource, Sync}
import cats.effect.std.Queue
import cats.syntax.traverse.*

import java.io.{BufferedReader, BufferedWriter, FileReader, FileWriter}
import java.nio.file.Path

// None means termination
type OptStrQueue = Queue[IO, Option[String]]

def handleRecords(recordsQ: OptStrQueue, numQ: OptStrQueue, txtQ: OptStrQueue, errQ: OptStrQueue): IO[Unit] =
  recordsQ.take.flatMap {
    case None => numQ.offer(None) >> txtQ.offer(None) >> errQ.offer(None)
    case Some(value) =>
      IO(RecordParser.parseRecord(value)).flatMap {
        case Left(error) => errQ.offer(Some(error))
        case Right(entries) => entries.toList.traverse { entry =>
          val serialized = CsvStringSerializer.serialize(entry)
          entry match
            case _: Valid.Num => numQ.offer(Some(serialized))
            case _: Valid.Text => txtQ.offer(Some(serialized))
        }.void
      } >> handleRecords(recordsQ, numQ, txtQ, errQ)
  }

def fileReader(filePath: String, recordsQ: OptStrQueue): IO[Unit] = {
  val fileRes = Resource.fromAutoCloseable(IO(BufferedReader(FileReader(filePath))))

  def go(source: BufferedReader, accumulator: Vector[String]): IO[Unit] =
    IO.blocking(source.readLine()).flatMap { line =>
      if (line == null) recordsQ.offer(None)
      else if (line matches "^Record.*") go(source, accumulator:+line)
      else if (line == "%") recordsQ.offer(Some((accumulator:+line).mkString("", "\n", "\n"))) >> go(source, Vector.empty)
      else if (accumulator.isEmpty) go(source, accumulator) // acc empty means we're reading garbage between records
      else go(source, accumulator:+line)
    }

  fileRes.use(go(_, Vector.empty))
}

def fileWriter(filePath: String, queue: OptStrQueue, header: Option[String] = None): IO[Unit] = {
  val fileRes = Resource.fromAutoCloseable(IO(BufferedWriter(FileWriter(filePath))))

  def go(destination: BufferedWriter): IO[Unit] =
    queue.take.flatMap {
      case Some(value) => IO.blocking(destination.write(value)) >> go(destination)
      case None => IO.unit
    }

  fileRes.use { dst =>
    (if (header.isEmpty) IO.unit else IO(dst.write(header.get))) >> go(dst)
  }
}

val bufferSize = 1000
val header = "rec,field,val\n"

// TODO too many explicit types, maybe it's possible to infer
def program(srcPath: String, numPath: String, txtPath: String, errPath: String): IO[Unit] = for {
  numQ <- Queue.bounded[IO, Option[String]](bufferSize)
  txtQ <- Queue.bounded[IO, Option[String]](bufferSize)
  errQ <- Queue.bounded[IO, Option[String]](bufferSize)
  recordsQ <- Queue.bounded[IO, Option[String]](bufferSize)
  _ <- fileReader(srcPath, recordsQ) &>
       fileWriter(txtPath, txtQ, Some(header)) &>
       fileWriter(numPath, numQ, Some(header)) &>
       fileWriter(errPath, errQ) &>
       handleRecords(recordsQ, numQ, txtQ, errQ)
} yield ()
