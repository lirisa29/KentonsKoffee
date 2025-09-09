package com.iie.st10349354.kentonskoffee

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface OrderDAO {
    @Insert
    fun insert(orderEntity: OrderEntity): Long

    @Query("SELECT * FROM orders")
    fun getAllOrders(): List<OrderEntity>
}