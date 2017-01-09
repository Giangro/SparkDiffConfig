#!/usr/bin/bash

#/usr/bin/hdfs dfs -rm -r hello_count.txt
hdfs dfs -rm -r /user/ec2-user/config1.csv
hdfs dfs -rm -r /user/ec2-user/config2.csv

hdfs dfs -put config1.csv /user/ec2-user/config1.csv
hdfs dfs -put config2.csv /user/ec2-user/config2.csv

/usr/bin/spark-submit --class HelloSpark \
    --master yarn \
    --deploy-mode client \
    --driver-memory 1g \
    --executor-memory 1g \
    --executor-cores 1 \
    --packages com.databricks:spark-csv_2.11:1.4.0 \
    --driver-java-options "-Dlog4j.configuration=file:///home/ec2-user/dev/spark/SparkDiffConfig/log4j.properties" \
   ./target/scala-2.11/*.jar
