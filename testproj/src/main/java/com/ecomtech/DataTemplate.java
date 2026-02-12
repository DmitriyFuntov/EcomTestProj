package com.ecomtech;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class DataTemplate {
    public static class Store implements Serializable {
        private int id;
        private String name;
        private String city;

        public Store() {} 

        public Store(int id, String name, String city) {
            this.id = id; this.name = name; this.city = city;
        }

        public int getId() { return id; }
        public String getName() { return name; }    
        public String getCity() { return city; }

        public void setId(int id) { this.id = id; }
        public void setName(String name) { this.name = name; }
        public void setCity(String city) { this.city = city; }
    }

    public static class User implements Serializable {
        private int id;
        private String name;
        private String phone;
        private Timestamp created_at;

        public User() {}

        public User(int id, String name, String phone, String createdAt) {
            this.id = id; this.name = name; this.phone = phone;
            this.created_at = Timestamp.valueOf(createdAt);
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getPhone() { return phone; }
        public Timestamp getCreated_at() { return created_at; }

        public void setId(int id) { this.id = id; }
        public void setName(String name) { this.name = name; }
        public void setPhone(String phone) { this.phone = phone; }
        public void setCreated_at(Timestamp created_at) { this.created_at = created_at; }
    }

    public static class Order implements Serializable {
        private int id;
        private BigDecimal amount;
        private int user_id;
        private int store_id;
        private String status;
        private Timestamp created_at;

        public Order() {}

        public Order(int id, double amount, int user_id, int store_id, String status) {
            this.id = id;
            this.amount = BigDecimal.valueOf(amount);
            this.user_id = user_id;
            this.store_id = store_id;
            this.status = status;
            this.created_at = new Timestamp(System.currentTimeMillis());
        }

        public int getId() { return id; }
        public BigDecimal getAmount() { return amount; }
        public int getUser_id() { return user_id; }
        public int getStore_id() { return store_id; }
        public String getStatus() { return status; }
        public Timestamp getCreated_at() { return created_at; }

        public void setId(int id) { this.id = id; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public void setUserId(int user_id) { this.user_id = user_id; }
        public void setStoreId(int store_id) { this.store_id = store_id; }
        public void setStatus(String status) { this.status = status; }
        public void setCreated_at(Timestamp created_at) { this.created_at = created_at; }
    }

    public static class ResultData implements Serializable {
        private String city;
        private String store_name;
        private BigDecimal target_amount;

        public ResultData() {}

        public ResultData(String city, String store_name, double target_amount) {
            this.city = city; this.store_name = store_name; 
            this.target_amount = BigDecimal.valueOf(target_amount);
        }

        public String getCity() { return city; }
        public String getStore_name() { return store_name; }
        public BigDecimal getTarget_amount() { return target_amount; }

        public void setCity(String city) { this.city = city; }
        public void setStore_name(String store_name) { this.store_name = store_name; }
        public void setTarget_amount(BigDecimal target_amount) { this.target_amount = target_amount; }
    }
}
