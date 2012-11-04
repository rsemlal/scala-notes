import sbt._
import Keys._

object CBPBuild extends Build {
  lazy val root = Project(id = "scala-notes", base = file(".")) aggregate (core, ihm)
  lazy val core = Project(id = "scala-notes-core", base = file("scala-notes-core"))
  lazy val ihm = Project(id = "scala-notes-ihm", base = file("scala-notes-ihm")) dependsOn(core)
}
