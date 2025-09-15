package com.iie.st10349354.kentonskoffee

import android.content.Context
import com.google.gson.Gson

object JsonUtils {
    private val gson = Gson()

    fun orderToJson(order: OrderEntity): String {
        return gson.toJson(order)
    }

    fun jsonToOrder(json: String): OrderEntity {
        return gson.fromJson(json, OrderEntity::class.java)
    }

    fun saveOrderToPreferences(context: Context, order: OrderEntity) {
        val jsonString = orderToJson(order)
        val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()){
            putString("order_key", jsonString)
            apply()
        }
    }

    fun getOrderFromPreferences(context: Context): OrderEntity? {
        val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val jsonString = sharedPreferences.getString("order_key", null)
        return jsonString?.let { jsonToOrder(it) }
    }
}