val testshapeless = Project(id = "testshapeless", base = file("."))
  .settings(
    organization := "se.gigurra",
    version := "SNAPSHOT",

    scalaVersion := "2.11.7",
    scalacOptions ++= Seq("-feature", "-unchecked", "-deprecation"),

    libraryDependencies ++= Seq(

      // Shapeless
      "com.chuusai" %% "shapeless" % "2.3.0",

      // Scalaz
      "org.scalaz" %% "scalaz-core" % "7.2.0",

      // Cats
      "org.typelevel" %% "cats" % "0.4.1",

      // Monocle (Uses scalaz)
      "com.github.julien-truffaut" %% "monocle-core" % "1.2.0",
      "com.github.julien-truffaut" %% "monocle-generic" % "1.2.0",
      "com.github.julien-truffaut" %% "monocle-macro" % "1.2.0",

      // Simulacrum (part of cats)
      "com.github.mpilquist" %% "simulacrum" % "0.7.0",

      // Test frameworks
      "org.scalatest" %% "scalatest" % "2.2.4" % "test"
    ),

    resolvers ++= Seq(
      Resolver.sonatypeRepo("releases"),
      Resolver.sonatypeRepo("snapshots")
    )

  )

// for @Lenses macro support
addCompilerPlugin("org.scalamacros" %% "paradise" % "2.1.0" cross CrossVersion.full)
