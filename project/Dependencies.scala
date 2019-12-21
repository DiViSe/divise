import play.sbt.PlayImport.guice
import sbt.ModuleID
import sbt._

object Dependencies {

  val Http4sVersion          = "0.20.0"
  val CirceVersion           = "0.11.1"
  val CirceConfigVersion     = "0.6.1"
  val DoobieVersion          = "0.6.0"
  val EnumeratumCirceVersion = "1.5.13"
  val ScalaCheckVersion      = "1.14.0"
  val FlywayVersion          = "5.2.4"
  val TelegramBot            = "4.2.0-RC1"
  val PostgreSQL             = "42.2.5"

  lazy val playJson       = "com.typesafe.play" %% "play-json"             % "2.6.13"
  lazy val playSlick      = "com.typesafe.play" %% "play-slick"            % "3.0.1"
  lazy val playEvolutions = "com.typesafe.play" %% "play-slick-evolutions" % "3.0.1"

  lazy val scalaTestPlay  = "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.1"
  lazy val scalaMock      = "org.scalamock" %% "scalamock" % "4.0.0"
  lazy val mockitoCore    = "org.mockito" % "mockito-core" % "2.26.0"
  lazy val akkaCluster    = "com.typesafe.akka" %% "akka-cluster-tools" % "2.5.25"
  lazy val monocleVersion = "2.0.0"

  val messaging: Seq[ModuleID] = Seq(
    playSlick,
    playEvolutions,
    playJson,
    guice,
    akkaCluster
  )

  val common: Seq[ModuleID] = Seq(
    "junit"                          % "junit"                   % "4.10" % "test",
    "org.scalatest"                  %% "scalatest"              % "3.0.6" % "test",
    "com.fasterxml.jackson.core"     % "jackson-databind"        % "2.9.3",
    "com.fasterxml.jackson.core"     % "jackson-core"            % "2.9.3",
    "com.fasterxml.jackson.module"   %% "jackson-module-scala"   % "2.9.2",
    "ch.qos.logback"                 % "logback-classic"         % "1.1.3",
    "org.json4s"                     %% "json4s-jackson"         % "3.5.3",
    "org.mockito"                    % "mockito-core"            % "2.13.0" % Test,
    "com.chuusai"                    %% "shapeless"              % "2.3.3",
    "biz.enef"                       %% "slogging"               % "0.6.1",
    "com.typesafe"                   % "config"                  % "1.3.4",
    "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310" % "2.9.6",
    "io.spray"                       %% "spray-json"             % "1.3.5",
    "com.typesafe.play"              %% "twirl-api"              % "1.4.2",
    scalaMock
  )

  val api: Seq[ModuleID] = Seq(
    "com.bot4s"                      %% "telegram-core"                  % TelegramBot,
    "com.bot4s"                      %% "telegram-akka"                  % TelegramBot,
    "org.http4s"                     %% "http4s-blaze-server"            % Http4sVersion,
    "org.http4s"                     %% "http4s-circe"                   % Http4sVersion,
    "org.http4s"                     %% "http4s-server"                  % Http4sVersion,
    "org.http4s"                     %% "http4s-dsl"                     % Http4sVersion,
    "org.http4s"                     %% "http4s-blaze-client"            % Http4sVersion,
    "io.circe"                       %% "circe-generic"                  % CirceVersion,
    "io.circe"                       %% "circe-literal"                  % CirceVersion,
    "io.circe"                       %% "circe-generic-extras"           % CirceVersion,
    "io.circe"                       %% "circe-parser"                   % CirceVersion,
    "io.circe"                       %% "circe-java8"                    % CirceVersion,
    "io.circe"                       %% "circe-config"                   % CirceConfigVersion,
    "org.tpolecat"                   %% "doobie-core"                    % DoobieVersion,
    "org.tpolecat"                   %% "doobie-h2"                      % DoobieVersion,
    "org.tpolecat"                   %% "doobie-scalatest"               % DoobieVersion,
    "org.tpolecat"                   %% "doobie-hikari"                  % DoobieVersion,
    "org.tpolecat"                   %% "doobie-postgres"                % DoobieVersion,
    "org.flywaydb"                   % "flyway-core"                     % FlywayVersion,
    "com.beachape"                   %% "enumeratum-doobie"              % "1.5.14",
    "com.beachape"                   %% "enumeratum-circe"               % "1.5.14",
    "org.scalacheck"                 %% "scalacheck"                     % ScalaCheckVersion % Test,
    "org.postgresql"                 % "postgresql"                      % PostgreSQL,
    "org.slf4j"                      % "slf4j-api"                       % "1.7.25",
    "org.scalatest"                  %% "scalatest"                      % "3.0.6" % "test",
    "org.typelevel"                  %% "cats-core"                      % "1.4.0",
    "org.typelevel"                  %% "cats-effect"                    % "1.3.1",
    "ch.qos.logback"                 % "logback-classic"                 % "1.1.3",
    "org.json4s"                     %% "json4s-jackson"                 % "3.5.3",
    "org.mockito"                    % "mockito-core"                    % "2.13.0" % Test,
    "com.chuusai"                    %% "shapeless"                      % "2.3.3",
    "biz.enef"                       %% "slogging"                       % "0.6.1",
    "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310"         % "2.9.6",
    "io.spray"                       %% "spray-json"                     % "1.3.5",
    "org.slf4j"                      % "jul-to-slf4j"                    % "1.7.25",
    "com.softwaremill.sttp"          %% "async-http-client-backend-cats" % "1.5.16" excludeAll ExclusionRule(
      organization = "io.netty"
    ),
    "com.github.julien-truffaut" %% "monocle-core"    % monocleVersion,
    "com.github.julien-truffaut" %% "monocle-macro"   % monocleVersion,
    "com.github.julien-truffaut" %% "monocle-law"     % monocleVersion % "test",
    "com.jason-goodwin"          %% "authentikat-jwt" % "0.4.5",
    "io.netty"                   % "netty-all"        % "4.1.33.Final"
  )
}
