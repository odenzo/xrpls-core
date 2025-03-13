import BuildSettings.MyCompileOptions.scala3Options

ThisBuild / resolvers ++= Seq(Resolver.mavenLocal, "jitpack" at "https://jitpack.io")
ThisBuild / organization := "com.odenzo"
ThisBuild / name := "xrpls"
ThisBuild / scalaVersion := "3.6.4"
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
  name := "xrpls-common",
  scalacOptions := scala3Options,
  libraryDependencies ++= Libs.stdlibs ++ Libs.bouncycastle ++ Libs.spire,
)

lazy val models = (project in file("modules/models"))
  .dependsOn(common)
  .settings(name := "xrpls-models", scalacOptions := scala3Options, libraryDependencies ++= Libs.stdlibs)

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
  .settings(name := "xrpls-communication-engine",
            scalacOptions := scala3Options,
            libraryDependencies ++= Libs.http4s ++ Libs.fs2 ++ Libs.munit,
           )

lazy val `signing-core` = (project in file("modules/signing-core"))
  .dependsOn(common, models, bincodec)
  .settings(name := "xrpls-signing-core",
            scalacOptions := scala3Options,
            libraryDependencies ++= Libs.stdlibs ++ Libs.bouncycastle,
           )

lazy val `signing-bridge` = (project in file("modules/signing-bridge"))
  .dependsOn(common, models, `signing-core`)
  .settings(name := "xrpls-signing-bridge",
            scalacOptions := scala3Options,
            libraryDependencies ++= Libs.stdlibs ++ Libs.bouncycastle,
           )
