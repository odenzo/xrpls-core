//  Switching over to the typelevel ones. which I am sure will make tears
import BuildSettings.MyCompileOptions.scala3Options

ThisBuild / organizationName := "odenzo consulting"
ThisBuild / organization := "com.odenzo"
ThisBuild / startYear := Some(2025)
ThisBuild / tlSitePublishBranch := Some("main")
ThisBuild / tlBaseVersion := "0.4"
ThisBuild / licenses := Seq(License.Apache2)
ThisBuild / developers ++= List(
  tlGitHubDev("odenzo", "Steve Franks")
)

ThisBuild / scalaVersion := "3.6.4"
ThisBuild / semanticdbEnabled := true
ThisBuild / resolvers ++= Seq(Resolver.mavenLocal, "jitpack" at "https://jitpack.io")
Test / logBuffered := true
Test / parallelExecution := false

addCommandAlias("dev-docs", "docos/mdoc;docos/laikaPreview")
addCommandAlias("test-container", ";clean;compile;testOnly -- --include-tags=TC;")
addCommandAlias("test-integration", ";clean;compile;testOnly -- --include-tags=Integration;")
addCommandAlias("test-unit", ";clean;compile;testOnly -- --exclude-tags=Integration,TC,DB;")
addCommandAlias("docker-stage", "app/docker:stage;")

// Used by GitLab to build GitLab Pages
addCommandAlias("build-docs", "root/unidoc;docos/mdoc;docos/laikaSite")

lazy val xrplv2 = (project in file("."))
  .aggregate(common, models, communications, `signing-bridge`, `signing-core`, unidocs)
  .settings(publish / skip := true, name := "XRPLS")

//lazy val macros = (project in file("modules/xrpl-macros")).settings(
//  name := "xrpl-macros",
//  scalacOptions := scala3Options,
//  libraryDependencies ++= Libs.stdlibs ++ Libs.echopraxia ++ Libs.bouncycastle,
//)

lazy val common = (project in file("modules/common")).settings(
  name := "xrpls-common",
  scalacOptions := scala3Options,
  libraryDependencies ++= Libs.stdlibs ++ Libs.spire,
)

lazy val models = (project in file("modules/models"))
  .dependsOn(common)
  .settings(name := "xrpls-models", scalacOptions := scala3Options, libraryDependencies ++= Libs.stdlibs)

lazy val communications = (project in file("modules/communication"))
  .dependsOn(common, models)
  .settings(name := "xrpls-communication-engine",
            scalacOptions := scala3Options,
            libraryDependencies ++= Libs.http4s ++ Libs.fs2 ++ Libs.munit,
           )

lazy val `signing-core` = (project in file("modules/signing-core"))
  .dependsOn(common, models, communications)
  .settings(name := "xrpls-signing-core",
            scalacOptions := scala3Options,
            libraryDependencies ++= Libs.stdlibs ++ Libs.bouncycastle,
           )

lazy val `signing-bridge` = (project in file("modules/signing-bridge"))
  .dependsOn(common, models, `signing-core`, communications)
  .settings(name := "xrpls-signing-bridge",
            scalacOptions := scala3Options,
            libraryDependencies ++= Libs.stdlibs ++ Libs.bouncycastle,
           )

lazy val application = (project in file("modules/application"))
  .dependsOn(common, models, communications)
  .settings(name := "applications",
            scalacOptions := scala3Options,
            libraryDependencies ++= Libs.stdlibs ++ Libs.bouncycastle,
           )

lazy val documentations = (project in file("modules/documentation"))
  .dependsOn(common, models, communications, `signing-core`, `signing-bridge`)
  .enablePlugins(TypelevelSitePlugin)
  .settings(name := "documentation")
  .settings(
    //   tlBaseVersion := "0.0",
    tlSiteIsTypelevelProject := None,
    mdocVariables := Map(
      "VERSION" -> version.value
    ),
  )

/** Not sure unidoc is best idea for now but */
lazy val unidocs = project
  .in(file("modules/unidocs"))
  .enablePlugins(TypelevelUnidocPlugin) // also enables the ScalaUnidocPlugin
  .settings(
    name := "xrpls-docs",
    ScalaUnidoc / unidoc / unidocProjectFilter := inProjects(common, models, communications),
  )
