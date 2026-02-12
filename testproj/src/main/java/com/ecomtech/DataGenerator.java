package com.ecomtech;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DataGenerator {

    //region Данные для генерации
    private static final List <String> names = Arrays.asList("Ivan", "Irina", "Petr", "Olga", "Sidr", "Mariya", "Anna", "Oleg", "Denis", "Elena");
    private static final List <String> phoneNumbers = Arrays.asList("79998881234", "79995554321", "79991112233", "79992223344", "79993334455", "79994445566", "79996667788", "79997778899", "79998880011", "79990001122");
    private static final  List <String> timestamps = Arrays.asList("2025-11-15 10:00:00", "2024-05-20 12:30:00", "2026-01-10 09:15:00", "2025-12-01 14:45:00", "2025-03-22 16:20:00", "2025-10-30 11:10:00", "2024-07-18 13:55:00", "2026-02-05 08:05:00", "2025-11-25 17:35:00", "2023-12-12 15:25:00");

    private static final  List <String> storeNames = Arrays.asList("Magnit", "Pyaterochka", "Lenta", "Dixy", "Auchan", "Metro", "Okey", "Karusel", "Spar", "VkusVill");
    private static final List <String> cities = Arrays.asList("Moscow", "Saint-Petersburg", "Novosibirsk");//, "Yekaterinburg", "Kazan", "Nizhny Novgorod", "Chelyabinsk", "Samara", "Omsk", "Rostov-on-Don");
    //endregion

    public static void generateRandom(SparkSession spark, int userCount, int storeCount, int orderCount ) {
        
        //Пользователи
        List<DataTemplate.User> users = new java.util.ArrayList<>();
        for (int i = 1; i <= userCount; i++) {
            String name = names.get(ThreadLocalRandom.current().nextInt(names.size()));
            String phone = phoneNumbers.get(ThreadLocalRandom.current().nextInt(phoneNumbers.size()));
            String createdAt = timestamps.get(ThreadLocalRandom.current().nextInt(timestamps.size()));
            DataTemplate.User user = new DataTemplate.User(i, name, phone, createdAt);
            users.add(user);
        }


        // Магазины
        List<DataTemplate.Store> stores = new java.util.ArrayList<>();
        for (int i = 1; i <= storeCount; i++) {
            String storeName = storeNames.get(ThreadLocalRandom.current().nextInt(storeNames.size()));
            String city = cities.get(ThreadLocalRandom.current().nextInt(cities.size()));
            DataTemplate.Store store = new DataTemplate.Store(100 + i, storeName, city);
            stores.add(store);
        }


        // Заказы
        List<DataTemplate.Order> orders = new java.util.ArrayList<>();
        for (int i = 1; i <= orderCount; i++) {
            double amount = ThreadLocalRandom.current().nextDouble(100.0, 5000.0);
            int userId = ThreadLocalRandom.current().nextInt(1, userCount + 1);
            int storeId = 100 + ThreadLocalRandom.current().nextInt(1, storeCount + 1);
            DataTemplate.Order order = new DataTemplate.Order(i, amount, userId, storeId, "COMPLETED");
            orders.add(order);
        }


        // Сохранение в Parquet 
        saveAsParquet(spark, users, DataTemplate.User.class, "s3a://warehouse/input/user");
        saveAsParquet(spark, stores, DataTemplate.Store.class, "s3a://warehouse/input/store");
        saveAsParquet(spark, orders, DataTemplate.Order.class, "s3a://warehouse/input/order");
    }

    public static void generateHandMade(SparkSession spark){
        // пользователи
         List<DataTemplate.User> users = Arrays.asList(
             new DataTemplate.User(1, "Ivan", "7999123", "2025-01-15 10:00:00"),
             new DataTemplate.User(2, "Oleg", "7999456", "2024-12-20 12:00:00")
         );

         List<DataTemplate.Store> stores = Arrays.asList(
             new DataTemplate.Store(101, "Magnit", "Moscow"),
             new DataTemplate.Store(102, "Pyaterochka", "Moscow"),
             new DataTemplate.Store(103, "Lenta", "Moscow"),
             new DataTemplate.Store(104, "Dixy", "Moscow")
         );

         List<DataTemplate.Order> orders = Arrays.asList(
             new DataTemplate.Order(1, 500.0, 1, 101, "COMPLETED"),
             new DataTemplate.Order(2, 1000.0, 1, 102, "COMPLETED"),
             new DataTemplate.Order(3, 1500.0, 1, 103, "COMPLETED"),
             new DataTemplate.Order(4, 200.0, 1, 104, "COMPLETED"),
             new DataTemplate.Order(5, 5000.0, 2, 101, "COMPLETED")
         );

        // Сохранение в Parquet
        saveAsParquet(spark, users, DataTemplate.User.class, "s3a://warehouse/input/user");
        saveAsParquet(spark, stores, DataTemplate.Store.class, "s3a://warehouse/input/store");
        saveAsParquet(spark, orders, DataTemplate.Order.class, "s3a://warehouse/input/order");

    }

    private static <T> void saveAsParquet(SparkSession spark, List<T> data, Class<T> beanClass, String path) {
        Dataset<T> ds = spark.createDataset(data, org.apache.spark.sql.Encoders.bean(beanClass));
        ds.coalesce(1).write().mode("overwrite").parquet(path);
    }
}
