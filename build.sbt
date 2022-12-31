ThisBuild / scalaVersion := "3.2.1"

lazy val genExampleFile = inputKey[Unit]("Generate example input file") := {
  import complete.DefaultParsers._
  val args = spaceDelimited("<args>").parsed
  ExampleGenerator.genExampleFile(args)
}

lazy val root = project
  .in(file("."))
  .aggregate(common)
  .settings(
    name := "Alaska",
    genExampleFile
  )

lazy val common = project
  .in(file("common"))
