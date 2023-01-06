ThisBuild / scalaVersion := "3.2.1"

lazy val cats = "org.typelevel" %% "cats-core" % "2.9.0"
lazy val munit = "org.scalameta" %% "munit" % "1.0.0-M7" % Test

lazy val commonDeps = Seq(cats, munit)

lazy val genExampleFile = inputKey[Unit]("Generate example input file") := {
  import complete.DefaultParsers._
  val args = spaceDelimited("<args>").parsed
  ExampleGenerator.genExampleFile(args)
}

lazy val root = project
  .in(file("."))
  .aggregate(common, parser)
  .settings(
    name := "Alaska",
    genExampleFile
  )

lazy val common = project
  .in(file("common"))
  .settings(libraryDependencies ++= commonDeps)

lazy val parser = project
  .in(file("parser"))
  .settings(libraryDependencies ++= commonDeps:+"org.typelevel" %% "cats-parse" % "0.3.9")
  .dependsOn(common)
