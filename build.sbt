import com.typesafe.sbt.packager.docker.{DockerChmodType, DockerPermissionStrategy}

name := "trello-changelog"
lazy val `trello-changelog` = (project in file(".")).enablePlugins(PlayScala)

version := "1.0"

scalaVersion := "2.13.15"

libraryDependencies += ws
libraryDependencies += guice

libraryDependencies += "io.lemonlabs" %% "scala-uri" % "2.2.2"
libraryDependencies += "com.typesafe.play" %% "play-json-joda" % "2.9.1"

libraryDependencies += "org.specs2" %% "specs2-core" % "4.10.0" % "test"
