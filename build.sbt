name := "trello-changelog"
lazy val `trello-changelog` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.1"

libraryDependencies += ws
libraryDependencies += guice

libraryDependencies += "io.lemonlabs" %% "scala-uri" % "2.2.2"
libraryDependencies += "com.typesafe.play" %% "play-json-joda" % "2.9.0"

libraryDependencies += "org.specs2" %% "specs2-core" % "4.10.0" % "test"

mainClass in assembly := Some("play.core.server.ProdServerStart")
fullClasspath in assembly += Attributed.blank(PlayKeys.playPackageAssets.value)

assemblyMergeStrategy in assembly := {
  case manifest if manifest.contains("MANIFEST.MF") =>
    // We don't need manifest files since sbt-assembly will create
    // one with the given settings
    MergeStrategy.discard
  case manifest if manifest.contains("module-info.class") =>
    MergeStrategy.discard
  case referenceOverrides if referenceOverrides.contains("reference-overrides.conf") =>
    // Keep the content for all reference-overrides.conf files
    MergeStrategy.concat
  case x =>
    // For all the other files, use the default sbt-assembly merge strategy
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}
