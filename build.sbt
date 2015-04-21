name := """geoip-service"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "org.apache.storm"  %   "storm-core"      		% "0.9.3",
  "com.sanoma.cda"    %%  "maxmind-geoip2-scala"  	% "1.3.5"
)

fork in run := true