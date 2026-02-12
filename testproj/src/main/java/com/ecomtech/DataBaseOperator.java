package com.ecomtech;
import com.ecomtech.DataTemplate.*;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import static org.apache.spark.sql.functions.*;
import org.apache.spark.sql.expressions.Window;
import org.apache.spark.sql.expressions.WindowSpec;

public class DataBaseOperator {
    private final SparkSession spark;

    DataBaseOperator(String endpoint, String accessKey, String secretKey) {
         this.spark = SparkSession.builder()
                                        .appName("MinioETL")
                                        .master("local[*]")
                                        // Настройки S3A
                                        .config("fs.s3a.endpoint", endpoint) 
                                        .config("fs.s3a.access.key", accessKey)
                                        .config("fs.s3a.secret.key", secretKey)
                                        .config("fs.s3a.path.style.access", "true") 
                                        .config("fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
                                        .config("fs.s3a.connection.ssl.enabled", "false")
                                        .config("spark.hadoop.fs.s3a.native", "false")
                                        .getOrCreate();
    }

    public void sendRequest() {
        //TODO возможно вынести в другой метод
        Dataset<User> users = spark.read().parquet("s3a://warehouse/input/user").as(Encoders.bean(User.class));
        Dataset<Order> orders = spark.read().parquet("s3a://warehouse/input/order").as(Encoders.bean(Order.class));
        Dataset<Store> stores = spark.read().parquet("s3a://warehouse/input/store").as(Encoders.bean(Store.class));

        Dataset<User> users2025 = users.filter(year(col("created_at")).equalTo(2025));//возможно изменить на filter(col("created_at").between("2025-01-01", "2025-12-31"))

        //объединяем и агргируем данные
        Dataset<Row> result = orders 
            .join(users2025, orders.col("user_id").equalTo(users2025.col("id"))) //**
            .join(stores, orders.col("store_id").equalTo(stores.col("id")))
            .groupBy(stores.col("city"), stores.col("name").as("store_name"))
            .agg(sum("amount").as("target_amount")); 

        //группируем и сортируем данные
        WindowSpec window = Window.partitionBy("city").orderBy(col("target_amount").desc());

        Dataset<ResultData> top3 = result 
            .withColumn("rank", rank().over(window))
            .filter(col("rank").leq(3))
            .drop("rank").as(Encoders.bean(ResultData.class)); 

        top3.write().mode("overwrite").parquet("s3a://warehouse/output/result");
    }
    public void generateData(int userCount, int storeCount, int orderCount) {
        DataGenerator.generateRandom(this.spark, userCount, storeCount, orderCount);
    }
    public void generateData(){
        DataGenerator.generateHandMade(this.spark);
    }

}
