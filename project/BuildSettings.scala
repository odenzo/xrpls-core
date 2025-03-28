import laika.config.{ MessageFilters, SyntaxHighlighting, Version, Versions }
import laika.format.Markdown
import laika.helium.Helium
import laika.sbt.LaikaConfig

import scala.Seq
import scala.io.Codec

object BuildSettings {

  object MyCompileOptions {

    val scala3Options = Seq(
      "-source",
      "3.3",
//      "-semanticdb-text",
      "-new-syntax",
      "-pagewidth=120",
//      "-experimental",
//      "-language:implicitConversions",
//      "-language:experimental.betterFors",
//      "-language:experimental.namedTuples",
//      "-language:experimental.genericNumberLiterals",
//      "-language:strictEquality",
      "-deprecation", // emit warning and location for usages of deprecated APIs
      "-explain", // explain errors in more detail
      "-explain-types", // explain type errors in more detail
      "-explain-cyclic",
      "-feature", // emit warning and location for usages of features that should be imported explicitly
      "-indent", // allow significant indentation.
      "-print-lines", // show source code line numbers.
      "-unchecked", // enable additional warnings where generated code depends on assumptions
      // "-Xkind-projector", // allow `*` as wildcard to be compatible with kind projector
      //   "-Xfatal-warnings", // fail the compilation if there are any warnings
      // "-Xmigration"       // warn about constructs whose behavior may have changed since version
      "-Wall",
      "-Wvalue-discard",
      "-Wunused:all",
      "-WunstableInlineAccessors",
      "-Wshadow:all",
      "-Wsafe-init",
      "-Wnonunit-statement",
    )
  }

  object LaikaConfigs {
    import laika.config.LaikaKeys

    val versions = Versions
      .forCurrentVersion(Version("0.0.x", "0.0.2").setCanonical)
      .withOlderVersions(
        //        Version("0.0.1", "0.41"),
        //      Version("0.40.x", "0.40").withFallbackLink("toc.html"),
      )
      .withNewerVersions(
        Version("0.0.3", "0.43").withLabel("dev")
      )

    val helium: Helium = Helium.defaults.site.versions(versions)

    val laikaExtensions = Seq(Markdown.GitHubFlavor, SyntaxHighlighting)

    val laikaConfig = LaikaConfig
      .defaults
      .withEncoding(Codec.UTF8)
      .withConfigValue(LaikaKeys.siteBaseURL, "https://my-docs/site")
      .withConfigValue("targetFormat", List("html", "pdf"))
      .withMessageFilters(MessageFilters.forVisualDebugging)

  }
}
