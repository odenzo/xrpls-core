import BuildSettings.MyCompileOptions.scala3Options

ThisBuild / resolvers ++= Seq(Resolver.mavenLocal, "jitpack" at "https://jitpack.io")
ThisBuild / organization := "com.odenzo"
ThisBuild / name := "ripple-binary-codec"
ThisBuild / scalaVersion := "3.6.3"
ThisBuild / semanticdbEnabled := true

Test / logBuffered := true
Test / parallelExecution := false

lazy val xrplv2 =
  (project in file(".")).aggregate(common, models, bincodec, communications).settings(publish / skip := true)

//lazy val macros = (project in file("modules/xrpl-macros")).settings(
//  name := "xrpl-macros",
//  scalacOptions := scala3Options,
//  libraryDependencies ++= Libs.stdlibs ++ Libs.echopraxia ++ Libs.bouncycastle,
//)

lazy val common = (project in file("modules/common")).settings(
  name := "common-libs",
  scalacOptions := scala3Options,
  libraryDependencies ++= Libs.stdlibs ++ Libs.bouncycastle ++ Libs.spire,
)

lazy val models = (project in file("modules/models"))
  .dependsOn(common)
  .settings(name := "xrpl-models", scalacOptions := scala3Options, libraryDependencies ++= Libs.stdlibs)

lazy val bincodec = (project in file("modules/binary-codecs"))
  .dependsOn(common, models)
  .settings(
    name := "binary-codec",
    description := "Binary Decoders with SCODEC, plus txjson Encoders",
    scalacOptions := scala3Options,
    libraryDependencies ++= Libs.stdlibs,
  )

lazy val communications = (project in file("modules/communication"))
  .dependsOn(common, models, bincodec)
  .settings(name := "xrpl-communication-engine",
            scalacOptions := scala3Options,
            libraryDependencies ++= Libs.http4s ++ Libs.fs2 ++ Libs.munit,
           )

lazy val signing = (project in file("modules/signing"))
  .dependsOn(common, models, bincodec)
  .settings(name := "xrpl-signing",
            scalacOptions := scala3Options,
            libraryDependencies ++= Libs.stdlibs ++ Libs.bouncycastle,
           )
