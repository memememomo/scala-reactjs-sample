name := "scalaReactjsSample"

version := "1.0"

lazy val `scalareactjssample` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc ,
  cache ,
  ws   ,
  specs2 % Test,
  "org.webjars" %% "webjars-play" % "2.4.0-1",
  "org.webjars" % "react" % "0.14.7"
)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
