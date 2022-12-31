val scala3Version = "3.2.1"

lazy val genExampleFile = inputKey[Unit]("Generate example input file") := {
  import complete.DefaultParsers._
  val args = spaceDelimited("<args>").parsed
  ExampleGenerator.genExampleFile(args)
}

lazy val root = project
  .in(file("."))
  .settings(
    name := "Alaska",
    scalaVersion := scala3Version,
    genExampleFile
  )
