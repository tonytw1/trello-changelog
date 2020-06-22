name := "trello-changelog"
lazy val `trello-changelog` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.1"

libraryDependencies += ws
libraryDependencies += guice

libraryDependencies += "io.lemonlabs" %% "scala-uri" % "2.2.2"
libraryDependencies += "com.typesafe.play" %% "play-json-joda" % "2.9.0"

libraryDependencies += "org.specs2" %% "specs2-core" % "4.10.0" % "test"

enablePlugins(DockerPlugin)
dockerBaseImage := "openjdk:10-jre"
dockerExposedPorts in Docker := Seq(9000)

enablePlugins(GitVersioning)
