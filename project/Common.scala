import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import sbt.Keys._
import sbt._

object Common {

  val settings: Seq[Def.Setting[_]] = Seq(
    version := mkVersion,
    scalaVersion := "2.12.8",
    javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
    scalacOptions ++= Seq("-Ypartial-unification", "-unchecked"),
    resolvers += Opts.resolver.mavenLocalFile,
    resolvers ++= Seq(
      DefaultMavenRepository,
      Resolver.defaultLocal,
      Resolver.mavenLocal,
      // Resolver.mavenLocal has issues - hence the duplication
      "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository",
      "Apache Staging" at "https://repository.apache.org/content/repositories/staging/",
      Classpaths.typesafeReleases,
      "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
      "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
      "Java.net Maven2 Repository" at "http://download.java.net/maven/2/",
      Classpaths.sbtPluginReleases,
      "Eclipse repositories" at "https://repo.eclipse.org/service/local/repositories/egit-releases/content/",
      "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
    )
  )

  def mkVersion: String = {
    val timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
    Files.write(Paths.get("api/src/main/resources/version"), timestamp.getBytes)
    timestamp
  }
}
