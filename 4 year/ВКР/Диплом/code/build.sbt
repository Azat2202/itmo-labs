ThisBuild / scalaVersion := "2.13.14"
ThisBuild / organization := "com.example"
ThisBuild / version := "0.1.0"

lazy val root = (project in file("."))
  .settings(
    name := "feedprocessor",
    libraryDependencies ++= Seq(
      "io.temporal"                  %  "temporal-sdk"                   % "1.27.0",
      "io.temporal"                  %  "temporal-testing"               % "1.27.0" % Test,
      "dev.zio"                      %% "zio"                            % "2.1.9",
      "dev.zio"                      %% "zio-test"                       % "2.1.9"  % Test,
      "dev.zio"                      %% "zio-test-sbt"                   % "2.1.9"  % Test,
      "com.fasterxml.jackson.module" %% "jackson-module-scala"           % "2.17.2",
      "io.micrometer"                %  "micrometer-core"                % "1.13.4",
      "io.micrometer"                %  "micrometer-registry-prometheus" % "1.13.4",
      "org.typelevel"                %% "cats-core"                      % "2.12.0",
      "com.beachape"                 %% "enumeratum"                     % "1.7.4",
      "org.scala-lang.modules"       %% "scala-xml"                      % "2.3.0",
      "ch.qos.logback"               %  "logback-classic"                % "1.5.8",
      "org.slf4j"                    %  "slf4j-api"                      % "2.0.16",
      "org.scalatest"                %% "scalatest"                      % "3.2.19" % Test
    ),
    Test / testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework"),
    scalacOptions ++= Seq(
      "-deprecation",
      "-feature",
      "-unchecked",
      "-Xlint:_",
      "-Wunused:imports"
    )
  )
