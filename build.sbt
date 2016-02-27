val testshapeless = Project(id = "testshapeless", base = file("."))
  .settings(
    organization := "se.gigurra",
    version := "SNAPSHOT",

    scalaVersion := "2.11.7",
    scalacOptions ++= Seq("-feature", "-unchecked", "-deprecation"),

    libraryDependencies ++= Seq(
      "com.chuusai" 		%% "shapeless" 		  % "2.3.0",
      "org.scalatest"     %%  "scalatest"         %   "2.2.4"     %   "test",
      "org.mockito"       %   "mockito-core"      %   "1.10.19"   %   "test"
    ),

	resolvers ++= Seq(
		Resolver.sonatypeRepo("releases"),
		Resolver.sonatypeRepo("snapshots")
	)
  )
  
