package com.iie.st10349354.kentonskoffee

import android.content.Context
import com.google.gson.Gson
import org.json.JSONObject

object JsonUtlis {
    private val gson = Gson();

    fun orderToJson(order: OrderEntity): String {
        return gson.toJson(order)
    }

    fun jsonToOrder(json: String): OrderEntity {
        return gson.fromJson(json, OrderEntity::class.java)
    }


    fun saveOrderPreferences(context: Context, order: OrderEntity) {
        val jsonString = orderToJson(order)

        val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

        with(sharedPreferences.edit())
        {
            putString("order_key", jsonString)
            apply()
        }
    }

    fun getOrderFromPreferences(context: Context): Order? {
        val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val jsonString = sharedPreferences.getString("order_key", null)

        val JsonObject = JSONObject(jsonString)

        val returnedOrder = Order(JsonObject.getString("productName"),JsonObject.getString("customerName"), JsonObject.getString("customerCell"),JsonObject.getString("orderDate"))
        return returnedOrder
    }
}