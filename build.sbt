name := "trello-changelog"
lazy val `trello-changelog` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.12"
libraryDependencies += ws
libraryDependencies += guice

libraryDependencies += "com.netaporter" %% "scala-uri" % "0.4.12"

enablePlugins(DockerPlugin)
dockerBaseImage := "openjdk:10-jre"
dockerExposedPorts in Docker := Seq(9000)

enablePlugins(GitVersioning)
