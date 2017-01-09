//
// Spark Diff
// Version 1.0
//

import com.typesafe.config._
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.sql._

object DiffSpark {

  /* Configuration
   * =============
  private val config =  ConfigFactory.load()
  private lazy val simple_lib = config.getConfig("simple-lib")
  private val foo = simple_lib.getString("foo")
  */

  val conf = new SparkConf().setAppName("Spark Diff Config")
  val sc = new SparkContext(conf)

  def wordCount() = {
    val textFile = sc.textFile("hdfs:///user/ec2-user/hello.txt")
    val counts = textFile.flatMap(line => line.split(" "))
                   .map(word => (word, 1))
                   .reduceByKey((a,b)=>a+b)
    counts.foreach(println)
    counts.saveAsTextFile("hdfs:///user/ec2-user/hello_count.txt")
  }

  def diff() = {
    val sqlContext = new SQLContext(sc)

    val df1 = sqlContext.read
    .format("com.databricks.spark.csv")
    .option("header", "true") // Use first line of all files as header
    .option("inferSchema", "true") // Automatically infer data types
    .option("delimiter", ",")
    .load("hdfs:///user/ec2-user/config1.csv")

    //df1.printSchema
    //df1.registerTempTable("config1")

    //val group_1 = sqlContext.sql("select Param,Value from config1").collect().foreach(println)

    val df2 = sqlContext.read
    .format("com.databricks.spark.csv")
    .option("header", "true") // Use first line of all files as header
    .option("inferSchema", "true") // Automatically infer data types
    .option("delimiter", ",")
    .load("hdfs:///user/ec2-user/config2.csv")

    // si poteva ottenere lo stesso risultato utilizzando direttamente
    // sui dataframe il metodo except
    // esempio: df1.except(...)

    //val dfintersection = df1.intersect (df2):
    //val dfdiff=df1.except(dfintersection)

    //df1.registerTempTable("config1")
    //df2.registerTempTable("config2")
    //val group_1 = sqlContext.sql("select c.Param,c.Value from config1 c join ")

    val df3 = df1.join(df2,Seq("Node","Param"))

    //df3.printSchema

    df3.filter("Value!=Value2").collect.foreach(println)

    //val rddintersect = df1.rdd.intersection(df2.rdd)
    //val rdddiff = df1.rdd.subtract(rddintersect)
    //rdddiff.collect.foreach(println)

  }

  def main(args: Array[String]) = {
    //wordCount
    diff
    sc.stop()
  }

}
