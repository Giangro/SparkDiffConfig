val typesafe = "com.typesafe" % "config" % "1.3.0"
val sparkcsv = "com.databricks" % "spark-csv_2.11" % "1.4.0"
val sparkcore = "org.apache.spark" %% "spark-core" % "1.6.1"
val sparksql = "org.apache.spark" % "spark-sql_2.11" % "1.6.2"

lazy val root = (project in file(".")).
  settings(
    name := "sparketl",
    version := "1.0",
    scalaVersion := "2.11.8",    
    libraryDependencies ++= Seq(
      typesafe,sparkcsv,sparkcore,sparksql
    )
  )
