import Dependencies._
import org.scoverage.coveralls.Imports.CoverallsKeys._

// Waiting for 1.2.3 or upper
// coverallsGitRepoLocation := "../"

lazy val root = (project in file(".")).
  settings(
    name := "Lambë",
    inThisBuild(List(
      organization := "org.lambe",
      scalaVersion := "2.12.4",
      version := "0.1.0-SNAPSHOT"
    )),
    libraryDependencies += scalaTest % Test,
    libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.6"
  )
