package com.example.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ShopItem
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.VelouraViewModel
import kotlinx.coroutines.flow.first

@Composable
fun ShopScreen(
    viewModel: VelouraViewModel,
    modifier: Modifier = Modifier
) {
    val shopItems by viewModel.allShopItems.collectAsState()
    val wishlist by viewModel.wishlistItems.collectAsState()

    var selectedCategory by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }

    val categories = listOf("All", "Elysian Rings", "Chains & Chokers", "Artisanal Keepsakes")

    val filteredItems = remember(shopItems, selectedCategory, searchQuery) {
        shopItems.filter { item ->
            val matchesCategory = selectedCategory == "All" || item.category == selectedCategory
            val matchesSearch = item.name.contains(searchQuery, ignoreCase = true) ||
                    item.description.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(VelouraBlack)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            
            // VAULT HEADER
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(VelouraCharcoal)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "THE METALLIC VAULT",
                    color = VelouraGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "High-End Jewelry Boutique",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search accessories...", color = TextMuted) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = VelouraGold) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Close, contentDescription = null, tint = Color.White)
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = VelouraGold,
                        unfocusedBorderColor = SoftGlassBorder,
                        focusedContainerColor = VelouraBlack,
                        unfocusedContainerColor = VelouraBlack,
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("shop_search_input"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Category List
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    categories.forEach { category ->
                        val isSelected = selectedCategory == category
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(if (isSelected) VelouraGold else Color(0x11FFFFFF))
                                .border(1.dp, if (isSelected) Color.Transparent else SoftGlassBorder, RoundedCornerShape(50))
                                .clickable { selectedCategory = category }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .testTag("shop_category_tab_$category")
                        ) {
                            Text(
                                text = category,
                                color = if (isSelected) Color.Black else Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // EXCLUSIVE VALUE BAR
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0F121C)) // luxury royal blue-ish tint shadow
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Diamond, contentDescription = null, tint = VelouraGold, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "LUXURY UNDER BUDGET FLASH SALE",
                        color = VelouraChampagne,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocalShipping, contentDescription = null, tint = VelouraGold, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Secure Delivery",
                        color = TextMuted,
                        fontSize = 10.sp
                    )
                }
            }

            // MAIN GRID
            if (filteredItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.AllInbox,
                            contentDescription = "Empty boutique",
                            tint = TextMuted,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Awaiting handcrafted drop of style. Please check other tabs!",
                            color = TextMuted,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 32.dp)
                        )
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(filteredItems) { item ->
                        val isWishlisted = wishlist.any { it.itemId == item.id && it.type == "SHOP" }

                        ShopGridCard(
                            item = item,
                            isWishlisted = isWishlisted,
                            onClick = { viewModel.selectShopItemAndNavigate(item) },
                            onToggleWishlist = {
                                viewModel.toggleWishlist(
                                    itemId = item.id,
                                    name = item.name,
                                    price = item.price,
                                    imageUrl = item.imageUrl,
                                    type = "SHOP"
                                )
                            },
                            onAddToCart = { viewModel.addShopItemToCart(item) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(88.dp)) // layout cushion
        }
    }
}

@Composable
fun ShopGridCard(
    item: ShopItem,
    isWishlisted: Boolean,
    onClick: () -> Unit,
    onToggleWishlist: () -> Unit,
    onAddToCart: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = VelouraCharcoal),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, SoftGlassBorder),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("shop_item_card_${item.id}")
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(124.dp)
            ) {
                SmoothImage(
                    url = item.imageUrl,
                    contentDescription = item.name,
                    modifier = Modifier.fillMaxSize()
                )

                // Wishlist circle overlay
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                        .clickable { onToggleWishlist() }
                        .testTag("wish_toggle_${item.id}"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isWishlisted) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite toggle",
                        tint = if (isWishlisted) Color.Red else Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Promo tag
                if (item.isBestSeller) {
                    PremiumIndicatorPill(
                        text = "Best Seller",
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp),
                        containerColor = VelouraGold
                    )
                } else if (item.isTrending) {
                    PremiumIndicatorPill(
                        text = "Trending",
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp),
                        containerColor = VelouraOrange
                    )
                }
                
                // High-End stock alert
                if (item.stock <= 5) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(6.dp)
                            .clip(RoundedCornerShape(4))
                            .background(Color(0xE6EF4444)) // crimson
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("Only ${item.stock} left", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = item.name,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Genuine 18K / Handmade",
                    color = TextMuted,
                    fontSize = 10.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "\u20B9${item.price.toInt()}",
                                color = VelouraGold,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "\u20B9${item.originalPrice.toInt()}",
                                color = TextMuted,
                                fontSize = 9.sp,
                                style = LocalTextStyle.current.copy(textDecoration = TextDecoration.LineThrough)
                            )
                        }
                        
                        Text(
                            text = "${(100 - (item.price/item.originalPrice*100)).toInt()}% OFF SAVINGS",
                            color = VelouraOrange,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(VelouraGold)
                            .clickable { onAddToCart() }
                            .testTag("add_to_cart_shop_${item.id}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = "Add to bag",
                            tint = Color.Black,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
