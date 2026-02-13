package com.ecomtech;

public class Main {
    public static void main(String[] args) {
       
        String endpoint = System.getenv("MINIO_ENDPOINT");
        String accessKey = System.getenv("MINIO_ACCESS_KEY");
        String secretKey = System.getenv("MINIO_SECRET_KEY");

        DataBaseOperator dbOperator = new DataBaseOperator(endpoint, accessKey, secretKey);

        //рандомная генерация
        //dbOperator.generateData(100, 10, 1000);

        //обычная генерация
        dbOperator.generateData();
        dbOperator.sendRequest();
    }
}