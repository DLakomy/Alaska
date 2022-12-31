val scala3Version = "3.2.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "Alaska",
    scalaVersion := scala3Version,
  )
