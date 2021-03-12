
// Let 'sbt clean' remove files that may cause the editor to get out-of-sync
// with the compiler.
//cleanFiles += new java.io.File(".dotty-ide.json")
//cleanFiles += new java.io.File(".dotty-ide-artifact")

lazy val root = project
  .in(file("."))
  .settings(
    name := "dotty-simple",
    version := "0.1.0",

    scalaVersion := "3.0.0-RC1",
    scalacOptions ++= Seq("-Yindent-colons"
                          //"-Yexplicit-nulls"
    )


  )
