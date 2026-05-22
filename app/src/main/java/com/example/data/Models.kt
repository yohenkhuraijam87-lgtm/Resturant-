package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_items")
data class FoodItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val category: String,
    val tag: String,
    val isCombo: Boolean = false,
    val isHappyHour: Boolean = false,
    val calories: Int = 320
)

@Entity(tableName = "shop_items")
data class ShopItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String,
    val price: Double,
    val originalPrice: Double,
    val imageUrl: String,
    val category: String,
    val isTrending: Boolean = false,
    val isBestSeller: Boolean = false,
    val stock: Int = 10
)

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val itemId: Long,
    val name: String,
    val price: Double,
    val imageUrl: String,
    val quantity: Int,
    val type: String // "FOOD" or "SHOP"
)

@Entity(tableName = "wishlist_items")
data class WishlistItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val itemId: Long,
    val name: String,
    val price: Double,
    val imageUrl: String,
    val type: String // "FOOD" or "SHOP"
)

@Entity(tableName = "reservations")
data class Reservation(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val email: String,
    val phone: String,
    val date: String,
    val time: String,
    val guests: Int,
    val section: String, // "VIP Lounge", "Rooftop Sky deck", "Main Candlelit Room"
    val branchName: String,
    val status: String = "Confirmed", // "Confirmed", "Pending", "Cancelled"
    val rewardPointsUsed: Int = 0
)

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val totalAmount: Double,
    val status: String, // "Preparing", "On the Way", "Completed"
    val itemNames: String, // List of item names comma separated
    val pointsEarned: Int,
    val deliveryType: String // "Delivery" or "Pickup"
)

data class UserProfile(
    val name: String = "Aria Sterling",
    val email: String = "aria.sterling@veloura.social",
    val phone: String = "+91 98765 43210",
    val tier: String = "Gold Elite VIP",
    val rewardPoints: Int = 450,
    val couponCodes: List<String> = listOf("VELOURA100", "VIPMEMBER")
)
