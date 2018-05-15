name := "leanakka"

version := "0.2-SNAPSHOT"
organization :="com.akkademy-db"

scalaVersion := "2.12.2"

lazy val akkaVersion = "2.5.12"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-remote" % akkaVersion,
  "com.syncthemall" % "boilerpipe" % "1.2.2",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)

mappings in (Compile, packageBin) ~= {_.filterNot { case (_, name) =>
    Seq("application.conf").contains(name)
}}