package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class VelouraViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: VelouraRepository
    
    // UI state streams
    val allFoods: StateFlow<List<FoodItem>>
    val allShopItems: StateFlow<List<ShopItem>>
    val cartItems: StateFlow<List<CartItem>>
    val wishlistItems: StateFlow<List<WishlistItem>>
    val allReservations: StateFlow<List<Reservation>>
    val allOrders: StateFlow<List<Order>>

    // Cart analytics
    val cartSubtotal: StateFlow<Double>
    val cartItemCount: StateFlow<Int>

    // Navigation state
    private val _currentScreen = MutableStateFlow("home") // "home", "dining", "shop", "reservations", "profile", "cart", "admin", "details"
    val currentScreen: StateFlow<String> = _currentScreen.asStateFlow()

    // Navigation stack tracking to allow back navigation
    private val navigationStack = mutableListOf("home")

    // For keeping track of what is selected for details
    private val _selectedFoodItem = MutableStateFlow<FoodItem?>(null)
    val selectedFoodItem = _selectedFoodItem.asStateFlow()

    private val _selectedShopItem = MutableStateFlow<ShopItem?>(null)
    val selectedShopItem = _selectedShopItem.asStateFlow()

    // User profile state
    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile = _userProfile.asStateFlow()

    // Active coupon
    private val _appliedCoupon = MutableStateFlow<String?>(null)
    val appliedCoupon = _appliedCoupon.asStateFlow()

    // Toast and notification alerts
    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage = _userMessage.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = VelouraRepository(database)

        // Run seed check in background
        viewModelScope.launch {
            repository.initializeSeedDataIfNeeded()
        }

        allFoods = repository.allFoods.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        allShopItems = repository.allShopItems.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        cartItems = repository.cartItems.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        wishlistItems = repository.wishlistItems.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        allReservations = repository.allReservations.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        allOrders = repository.allOrders.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        // Derive subtotal
        cartSubtotal = repository.cartItems
            .map { list -> list.sumOf { it.price * it.quantity } }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = 0.0
            )

        // Derive item count
        cartItemCount = repository.cartItems
            .map { list -> list.sumOf { it.quantity } }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = 0
            )
    }

    fun navigateTo(screen: String) {
        if (_currentScreen.value != screen) {
            navigationStack.add(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun navigateBack() {
        if (navigationStack.isNotEmpty()) {
            val prev = navigationStack.removeAt(navigationStack.size - 1)
            _currentScreen.value = prev
        } else {
            _currentScreen.value = "home"
        }
    }

    fun selectFoodAndNavigate(food: FoodItem) {
        _selectedFoodItem.value = food
        _selectedShopItem.value = null
        navigateTo("details")
    }

    fun selectShopItemAndNavigate(item: ShopItem) {
        _selectedShopItem.value = item
        _selectedFoodItem.value = null
        navigateTo("details")
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }

    fun triggerMessage(msg: String) {
        _userMessage.value = msg
    }

    // Cart actions
    fun addFoodToCart(food: FoodItem, quantity: Int = 1) {
        viewModelScope.launch {
            repository.addOrUpdateCartItem(
                itemId = food.id,
                name = food.name,
                price = food.price,
                imageUrl = food.imageUrl,
                quantity = quantity,
                type = "FOOD"
            )
            triggerMessage("Added `${food.name}` to elite dining bag!")
        }
    }

    fun addShopItemToCart(item: ShopItem, quantity: Int = 1) {
        viewModelScope.launch {
            repository.addOrUpdateCartItem(
                itemId = item.id,
                name = item.name,
                price = item.price,
                imageUrl = item.imageUrl,
                quantity = quantity,
                type = "SHOP"
            )
            triggerMessage("Added `${item.name}` to luxury vault bag!")
        }
    }

    fun updateCartItemQuantity(item: CartItem, delta: Int) {
        viewModelScope.launch {
            repository.addOrUpdateCartItem(
                itemId = item.itemId,
                name = item.name,
                price = item.price,
                imageUrl = item.imageUrl,
                quantity = delta,
                type = item.type
            )
        }
    }

    fun removeCartItem(item: CartItem) {
        viewModelScope.launch {
            repository.removeCartItem(item)
            triggerMessage("Removed `${item.name}` from bag.")
        }
    }

    fun applyCoupon(code: String): Boolean {
        return if (userProfile.value.couponCodes.contains(code)) {
            _appliedCoupon.value = code
            triggerMessage("Premium Coupon `$code` Applied Successfully!")
            true
        } else {
            triggerMessage("Invalid Coupon Code.")
            false
        }
    }

    fun removeCoupon() {
        _appliedCoupon.value = null
        triggerMessage("Coupon removed.")
    }

    // Wishlist actions
    fun toggleWishlist(itemId: Long, name: String, price: Double, imageUrl: String, type: String) {
        viewModelScope.launch {
            val wasAdded = repository.toggleWishlist(itemId, name, price, imageUrl, type)
            if (wasAdded) {
                triggerMessage("Added `$name` to luxury wishlist.")
            } else {
                triggerMessage("Removed `$name` from wishlist.")
            }
        }
    }

    fun isItemWishlisted(itemId: Long, type: String): Flow<Boolean> {
        return repository.isWishlisted(itemId, type)
    }

    // Reservations
    fun bookTable(
        name: String,
        email: String,
        phone: String,
        date: String,
        time: String,
        guests: Int,
        section: String,
        branch: String,
        pointsToUse: Int = 0
    ) {
        viewModelScope.launch {
            if (name.isBlank() || phone.isBlank() || email.isBlank()) {
                triggerMessage("Please fill in contact credentials.")
                return@launch
            }
            val reservation = Reservation(
                name = name,
                email = email,
                phone = phone,
                date = date,
                time = time,
                guests = guests,
                section = section,
                branchName = branch,
                rewardPointsUsed = pointsToUse
            )
            repository.makeReservation(reservation)
            
            // Adjust reward points
            val currentPoints = _userProfile.value.rewardPoints
            val modifier = if (pointsToUse > 0) -pointsToUse else 30 // earn 30 points on booking
            _userProfile.value = _userProfile.value.copy(
                rewardPoints = (currentPoints + modifier).coerceAtLeast(0)
            )

            triggerMessage("Reservation Confirmed! Reserved table at $branch for $guests guests.")
            navigateTo("profile")
        }
    }

    fun deleteReservation(id: Long) {
        viewModelScope.launch {
            repository.deleteReservation(id)
            triggerMessage("Reservation canceled successfully.")
        }
    }

    fun approveReservationInAdmin(id: Long) {
        viewModelScope.launch {
            repository.updateReservationStatus(id, "Approved")
            triggerMessage("Reservation Approved successfully.")
        }
    }

    // User Profile Settings Updates
    fun updateProfile(name: String, email: String, phone: String) {
        _userProfile.value = _userProfile.value.copy(name = name, email = email, phone = phone)
        triggerMessage("Profile credentials updated.")
    }

    // Checkout
    fun checkout(deliveryType: String) {
        viewModelScope.launch {
            val list = cartItems.value
            if (list.isEmpty()) {
                triggerMessage("Bag is empty.")
                return@launch
            }
            val names = list.joinToString { "${it.name} (x${it.quantity})" }
            val baseSubtotal = cartSubtotal.value
            val couponDiscount = if (_appliedCoupon.value == "VELOURA100") 100.0 else if (_appliedCoupon.value == "VIPMEMBER") baseSubtotal * 0.15 else 0.0
            val deliveryFee = if (deliveryType == "Delivery") 49.0 else 0.0
            val total = (baseSubtotal - couponDiscount + deliveryFee).coerceAtLeast(0.0)

            val earnPoints = (total * 0.1).toInt() // earn 10% points
            repository.checkoutOrder(names, total, deliveryType, earnPoints)

            // Update user profile points
            val profile = _userProfile.value
            _userProfile.value = profile.copy(
                rewardPoints = profile.rewardPoints + earnPoints
            )

            // Reset coupon
            _appliedCoupon.value = null
            triggerMessage("Elite Order Submitted Successfully! Total Paid: ₹${"%.2f".format(total)}")
            navigateTo("profile")
        }
    }

    // Admin Panel updates
    fun addNewFoodFromAdmin(
        name: String,
        desc: String,
        price: Double,
        imageUrl: String,
        category: String,
        tag: String,
        isCombo: Boolean,
        isHappyHour: Boolean
    ) {
        viewModelScope.launch {
            val item = FoodItem(
                name = name,
                description = desc,
                price = price,
                imageUrl = imageUrl,
                category = category,
                tag = tag,
                isCombo = isCombo,
                isHappyHour = isHappyHour
            )
            repository.addFoodItem(item)
            triggerMessage("Added premium dish `$name` successfully!")
        }
    }

    fun addNewShopItemFromAdmin(
        name: String,
        desc: String,
        price: Double,
        originalPrice: Double,
        imageUrl: String,
        category: String,
        isTrending: Boolean,
        isBestSeller: Boolean
    ) {
        viewModelScope.launch {
            val item = ShopItem(
                name = name,
                description = desc,
                price = price,
                originalPrice = originalPrice,
                imageUrl = imageUrl,
                category = category,
                isTrending = isTrending,
                isBestSeller = isBestSeller,
                stock = 10
            )
            repository.addShopItem(item)
            triggerMessage("Added luxury product `$name` successfully!")
        }
    }

    fun deleteFoodItem(id: Long) {
        viewModelScope.launch {
            repository.deleteFoodItemById(id)
            triggerMessage("Dish item has been removed from active inventory.")
        }
    }

    fun deleteShopItem(id: Long) {
        viewModelScope.launch {
            repository.deleteShopItemById(id)
            triggerMessage("Shop accessory has been removed from active vault.")
        }
    }
}
