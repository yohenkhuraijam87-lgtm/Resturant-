package com.example.data

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VelouraRepository(private val db: AppDatabase) {

    val allFoods: Flow<List<FoodItem>> = db.foodItemDao().getAllFoodItems()
    val allShopItems: Flow<List<ShopItem>> = db.shopItemDao().getAllShopItems()
    val cartItems: Flow<List<CartItem>> = db.cartItemDao().getCartItems()
    val wishlistItems: Flow<List<WishlistItem>> = db.wishlistItemDao().getWishlistItems()
    val allReservations: Flow<List<Reservation>> = db.reservationDao().getAllReservations()
    val allOrders: Flow<List<Order>> = db.orderDao().getAllOrders()

    suspend fun initializeSeedDataIfNeeded() = withContext(Dispatchers.IO) {
        // Seed food items if database is empty
        val existingFoods = db.foodItemDao().getAllFoodItems().firstOrNull() ?: emptyList()
        if (existingFoods.isEmpty()) {
            val initialFoods = listOf(
                FoodItem(
                    name = "Truffle Glazed Risotto",
                    description = "Creamy Arborio rice elevated with a fragrant white truffle glaze, premium butter, and wild forest mushrooms. Affordably priced, globally desired.",
                    price = 249.0,
                    imageUrl = "https://images.unsplash.com/photo-1542691457-cbe4df041eb2?w=600&auto=format&fit=crop",
                    category = "Signature Cru",
                    tag = "Luxury Under ₹299",
                    calories = 410
                ),
                FoodItem(
                    name = "Lobster Mac & Cheese Elite",
                    description = "Delectable claw meat macaroni tossed with 5 premium aged cheeses. An iconic visual feast that defines cheap thrills with high prestige.",
                    price = 289.0,
                    imageUrl = "https://images.unsplash.com/photo-1544025162-d76694265947?w=600&auto=format&fit=crop",
                    category = "Signature Cru",
                    tag = "Luxury Under ₹299",
                    isCombo = true,
                    calories = 580
                ),
                FoodItem(
                    name = "Saffron Salmon Crostini",
                    description = "Affordable wild salmon cured with fresh dill, served warm over local sourdough with a drizzle of luxurious saffron cream sauce.",
                    price = 219.0,
                    imageUrl = "https://images.unsplash.com/photo-1519708227418-c8fd9a32b7a2?w=600&auto=format&fit=crop",
                    category = "Signature Cru",
                    tag = "Fine Taste Elite",
                    calories = 310
                ),
                FoodItem(
                    name = "Wagyu Slider & Herb Butter Combo",
                    description = "Mini seared premium beef slider with signature melted brie cheese, rich custom garlic herb butter, and fresh wild rocket.",
                    price = 279.0,
                    imageUrl = "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&auto=format&fit=crop",
                    category = "Combos & Sliders",
                    tag = "Premium Combo",
                    isCombo = true,
                    calories = 620
                ),
                FoodItem(
                    name = "Veloura Sunset Pearl mocktail",
                    description = "Elegant cold-press signature pairing of sweet blood orange, edible gold glitter, botanical mint extract, and organic cream. Truly Instagrammable.",
                    price = 149.0,
                    imageUrl = "https://images.unsplash.com/photo-1514362545857-3bc16c4c7d1b?w=600&auto=format&fit=crop",
                    category = "Happy Hour",
                    tag = "1-For-1 Happy Hour",
                    isHappyHour = true,
                    calories = 120
                ),
                FoodItem(
                    name = "Rose-Petal Charcoal Brew",
                    description = "Dark active charcoal espresso latte sweetened with Bulgarian rose syrup and crowned with organic dry edible pink rosebuds.",
                    price = 139.0,
                    imageUrl = "https://images.unsplash.com/photo-1645112411341-6c4fd023714a?w=600&auto=format&fit=crop",
                    category = "Happy Hour",
                    tag = "Happy Hour Elite",
                    isHappyHour = true,
                    calories = 140
                )
            )
            db.foodItemDao().insertFoodItems(initialFoods)
        }

        // Seed shop items if empty
        val existingShop = db.shopItemDao().getAllShopItems().firstOrNull() ?: emptyList()
        if (existingShop.isEmpty()) {
            val initialShop = listOf(
                ShopItem(
                    name = "Elysian Gold Infinity Band",
                    description = "Handcrafted 18K gold vermeil band featuring an intricate double twist that shimmers in candlelight. Built to look expensive, priced for comfort.",
                    price = 999.0,
                    originalPrice = 2499.0,
                    imageUrl = "https://images.unsplash.com/photo-1605100804763-247f67b3557e?w=600&auto=format&fit=crop",
                    category = "Elysian Rings",
                    isTrending = true,
                    stock = 8
                ),
                ShopItem(
                    name = "Rose Gold Crown Tiara Ring",
                    description = "Stunning queen crown silhouette set with premium grade visual cubic zirconia diamonds on a gorgeous rose-gold overlay. High-society appeal.",
                    price = 1199.0,
                    originalPrice = 2999.0,
                    imageUrl = "https://images.unsplash.com/photo-1603561591411-07134e71a2a9?w=600&auto=format&fit=crop",
                    category = "Elysian Rings",
                    isBestSeller = true,
                    stock = 5
                ),
                ShopItem(
                    name = "Emerald Halo Solitaire",
                    description = "Deep forest green crystal gemstone cradled in a contemporary halo setting. Looks like a custom high-jewelry design that cost millions.",
                    price = 1499.0,
                    originalPrice = 4599.0,
                    imageUrl = "https://images.unsplash.com/photo-1599643478518-a784e5dc4c8f?w=600&auto=format&fit=crop",
                    category = "Elysian Rings",
                    isTrending = true,
                    stock = 4
                ),
                ShopItem(
                    name = "Celestial Petite Star Choker",
                    description = "Double-layered elegant chain detailed with starry micro-crystals. Drapes beautifully over casual or evening attire.",
                    price = 899.0,
                    originalPrice = 1999.0,
                    imageUrl = "https://images.unsplash.com/photo-1535632066927-ab7c9ab60908?w=600&auto=format&fit=crop",
                    category = "Chains & Chokers",
                    isTrending = false,
                    isBestSeller = true,
                    stock = 12
                ),
                ShopItem(
                    name = "Velvet Keepsake Gifting Box",
                    description = "Luxurious royal indigo velvet padded jewelry display case with gold hot stamp branding. Perfect for presentations or engagements.",
                    price = 450.0,
                    originalPrice = 1250.0,
                    imageUrl = "https://images.unsplash.com/photo-1549439602-43ebca2327af?w=600&auto=format&fit=crop",
                    category = "Artisanal Keepsakes",
                    isTrending = false,
                    isBestSeller = false,
                    stock = 25
                )
            )
            db.shopItemDao().insertShopItems(initialShop)
        }
    }

    // Food DB actions
    suspend fun addFoodItem(item: FoodItem) = withContext(Dispatchers.IO) {
        db.foodItemDao().insertFoodItem(item)
    }

    suspend fun updateFoodItem(item: FoodItem) = withContext(Dispatchers.IO) {
        db.foodItemDao().updateFoodItem(item)
    }

    suspend fun deleteFoodItemById(id: Long) = withContext(Dispatchers.IO) {
        db.foodItemDao().deleteFoodItemById(id)
    }

    // Shop DB actions
    suspend fun addShopItem(item: ShopItem) = withContext(Dispatchers.IO) {
        db.shopItemDao().insertShopItem(item)
    }

    suspend fun updateShopItem(item: ShopItem) = withContext(Dispatchers.IO) {
        db.shopItemDao().updateShopItem(item)
    }

    suspend fun deleteShopItemById(id: Long) = withContext(Dispatchers.IO) {
        db.shopItemDao().deleteShopItemById(id)
    }

    // Cart actions
    suspend fun addOrUpdateCartItem(itemId: Long, name: String, price: Double, imageUrl: String, quantity: Int, type: String) = withContext(Dispatchers.IO) {
        val currentCart = db.cartItemDao().getCartItems().first()
        val existing = currentCart.find { it.itemId == itemId && it.type == type }
        if (existing != null) {
            val updated = existing.copy(quantity = existing.quantity + quantity)
            if (updated.quantity <= 0) {
                db.cartItemDao().deleteCartItem(existing)
            } else {
                db.cartItemDao().updateCartItem(updated)
            }
        } else if (quantity > 0) {
            db.cartItemDao().insertCartItem(
                CartItem(itemId = itemId, name = name, price = price, imageUrl = imageUrl, quantity = quantity, type = type)
            )
        }
    }

    suspend fun removeCartItem(item: CartItem) = withContext(Dispatchers.IO) {
        db.cartItemDao().deleteCartItem(item)
    }

    suspend fun clearCart() = withContext(Dispatchers.IO) {
        db.cartItemDao().clearCart()
    }

    // Wishlist actions
    suspend fun toggleWishlist(itemId: Long, name: String, price: Double, imageUrl: String, type: String) = withContext(Dispatchers.IO) {
        val isWish = db.wishlistItemDao().isWishlisted(itemId, type).first()
        if (isWish) {
            db.wishlistItemDao().deleteWishlistItemByItemId(itemId, type)
            false
        } else {
            db.wishlistItemDao().insertWishlistItem(
                WishlistItem(itemId = itemId, name = name, price = price, imageUrl = imageUrl, type = type)
            )
            true
        }
    }

    fun isWishlisted(itemId: Long, type: String): Flow<Boolean> {
        return db.wishlistItemDao().isWishlisted(itemId, type)
    }

    // Reservations
    suspend fun makeReservation(reservation: Reservation) = withContext(Dispatchers.IO) {
        db.reservationDao().insertReservation(reservation)
    }

    suspend fun updateReservationStatus(id: Long, status: String) = withContext(Dispatchers.IO) {
        db.reservationDao().updateReservationStatus(id, status)
    }

    suspend fun deleteReservation(id: Long) = withContext(Dispatchers.IO) {
        db.reservationDao().deleteReservationById(id)
    }

    // Orders
    suspend fun checkoutOrder(itemNames: String, total: Double, deliveryType: String, pointsEarned: Int) = withContext(Dispatchers.IO) {
        val order = Order(
            date = "May 22, 2026",
            totalAmount = total,
            status = "Preparing",
            itemNames = itemNames,
            pointsEarned = pointsEarned,
            deliveryType = deliveryType
        )
        db.orderDao().insertOrder(order)
        db.cartItemDao().clearCart()
    }
}
