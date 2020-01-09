val dottyVersion = "0.21.0-RC1"

// Let 'sbt clean' remove files that may cause the editor to get out-of-sync
// with the compiler.
cleanFiles += new java.io.File(".dotty-ide.json")
cleanFiles += new java.io.File(".dotty-ide-artifact")

lazy val root = project
  .in(file("."))
  .settings(
    name := "dotty-simple",
    version := "0.1.0",

    scalaVersion := dottyVersion,
    // Without -Yindent-colons, the editor and compiler get out of sync for me - Per
    scalacOptions += "-Yindent-colons",

    libraryDependencies += "com.factor10" %% "intent" % "0.4.0",
    testFrameworks += new TestFramework("intent.sbt.Framework")


    //libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test"
  )
