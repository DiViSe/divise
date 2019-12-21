name := "divise"
version := "0.0.1"
import com.typesafe.sbt.SbtNativePackager.autoImport.NativePackagerHelper._
import com.typesafe.sbt.packager.docker.Cmd

lazy val api = project
  .settings(libraryDependencies ++= Dependencies.common)
  .settings(libraryDependencies ++= Dependencies.api)
  .settings(Common.settings: _*)
  .enablePlugins(JavaAppPackaging, SbtTwirl)
  .settings(
    packageName := "api",
    mappings in Universal := {
      val universalMappings = (mappings in Universal).value
      val fatJar            = (assembly in Compile).value
      val filtered          = universalMappings
      mappings in Universal ++= directory(target.value / "scala-2.12")
      mappings in (Compile, packageDoc) := Seq()
      filtered :+ (fatJar -> ("lib/" + fatJar.getName))
    },
    dockerRepository := Some("registry.hub.docker.com"),
    dockerUsername := Some("sshevchyk"),
    dockerExposedPorts ++= Seq(8080),
    dockerCommands := Seq(
      Cmd("FROM", "openjdk:alpine"),
      Cmd("ENV", "CONFIG", "app.test.conf"),
      Cmd("ENV", "DEBUG_PORT", "5010"),
      Cmd("RUN", "mkdir", "-p", "/usr/app"),
      Cmd("WORKDIR", "/usr/app"),
      Cmd("COPY", "/opt/docker/lib/api.jar", "/usr/app/api.jar"),
      Cmd("ENTRYPOINT",
          "java",
          "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=$DEBUG_PORT",
          "-jar",
          "api.jar",
          "$CONFIG")
    ),
    addCompilerPlugin("org.scalamacros" % "paradise" % paradiseVersion cross CrossVersion.full)
  )
val paradiseVersion = "2.1.1"
lazy val root = Project(id = "ermine", base = file("."))
  .aggregate(api)
  .settings(
    dockerRepository := Some("registry.hub.docker.com"),
    dockerUsername := Some("sshevchyk"),
    skip in publish := true,
    shellPrompt := { s: State =>
      import scala.Console._
      val blue  = s"$RESET$BLUE$BOLD"
      val white = s"$RESET$BOLD"

      val projectName = Project.extract(s).currentProject.id

      s"$blue$projectName$white $YELLOW▶$RESET $BOLD"
    }
  )

val deploy     = taskKey[Unit]("Build project and deploy to AWS")
val buildLocal = taskKey[Int]("Build project and deploy locally")

excludeDependencies += "commons-logging" % "commons-logging"
