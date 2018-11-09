name := "trello-changelog"
lazy val `trello-changelog` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.12"
libraryDependencies += ws
libraryDependencies += guice

libraryDependencies += "com.netaporter" %% "scala-uri" % "0.4.12"
libraryDependencies += "com.google.guava" % "guava" % "27.0-jre"
libraryDependencies += "com.typesafe.play" %% "play-json-joda" % "2.6.10"

enablePlugins(DockerPlugin)
dockerBaseImage := "openjdk:10-jre"
dockerExposedPorts in Docker := Seq(9000)

enablePlugins(GitVersioning)
