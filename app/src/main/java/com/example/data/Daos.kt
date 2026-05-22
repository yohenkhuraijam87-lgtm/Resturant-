package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodItemDao {
    @Query("SELECT * FROM food_items ORDER BY id DESC")
    fun getAllFoodItems(): Flow<List<FoodItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodItem(item: FoodItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodItems(items: List<FoodItem>)

    @Update
    suspend fun updateFoodItem(item: FoodItem)

    @Query("DELETE FROM food_items WHERE id = :id")
    suspend fun deleteFoodItemById(id: Long)
}

@Dao
interface ShopItemDao {
    @Query("SELECT * FROM shop_items ORDER BY id DESC")
    fun getAllShopItems(): Flow<List<ShopItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShopItem(item: ShopItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShopItems(items: List<ShopItem>)

    @Update
    suspend fun updateShopItem(item: ShopItem)

    @Query("DELETE FROM shop_items WHERE id = :id")
    suspend fun deleteShopItemById(id: Long)
}

@Dao
interface CartItemDao {
    @Query("SELECT * FROM cart_items ORDER BY id ASC")
    fun getCartItems(): Flow<List<CartItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: CartItem)

    @Update
    suspend fun updateCartItem(item: CartItem)

    @Delete
    suspend fun deleteCartItem(item: CartItem)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}

@Dao
interface WishlistItemDao {
    @Query("SELECT * FROM wishlist_items ORDER BY id DESC")
    fun getWishlistItems(): Flow<List<WishlistItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWishlistItem(item: WishlistItem)

    @Delete
    suspend fun deleteWishlistItem(item: WishlistItem)

    @Query("DELETE FROM wishlist_items WHERE itemId = :itemId AND type = :type")
    suspend fun deleteWishlistItemByItemId(itemId: Long, type: String)

    @Query("SELECT EXISTS(SELECT * FROM wishlist_items WHERE itemId = :itemId AND type = :type)")
    fun isWishlisted(itemId: Long, type: String): Flow<Boolean>
}

@Dao
interface ReservationDao {
    @Query("SELECT * FROM reservations ORDER BY id DESC")
    fun getAllReservations(): Flow<List<Reservation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReservation(reservation: Reservation)

    @Query("UPDATE reservations SET status = :status WHERE id = :id")
    suspend fun updateReservationStatus(id: Long, status: String)

    @Query("DELETE FROM reservations WHERE id = :id")
    suspend fun deleteReservationById(id: Long)
}

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders ORDER BY id DESC")
    fun getAllOrders(): Flow<List<Order>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order)
}
