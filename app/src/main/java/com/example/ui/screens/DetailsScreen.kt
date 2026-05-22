package com.example.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.VelouraViewModel

@Composable
fun DetailsScreen(
    viewModel: VelouraViewModel,
    modifier: Modifier = Modifier
) {
    val selectedFood by viewModel.selectedFoodItem.collectAsState()
    val selectedShop by viewModel.selectedShopItem.collectAsState()
    val wishlist by viewModel.wishlistItems.collectAsState()

    var quantity by remember { mutableStateOf(1) }
    var selectedRingSize by remember { mutableStateOf("7") } // default size

    // Decide what we are viewing
    val isFood = selectedFood != null

    val name = selectedFood?.name ?: selectedShop?.name ?: "Unknown Artifact"
    val imageUrl = selectedFood?.imageUrl ?: selectedShop?.imageUrl ?: ""
    val description = selectedFood?.description ?: selectedShop?.description ?: ""
    val price = selectedFood?.price ?: selectedShop?.price ?: 0.0
    val originalPrice = selectedShop?.originalPrice ?: price
    val tag = selectedFood?.tag ?: (if (selectedShop?.isBestSeller == true) "Best Seller" else if (selectedShop?.isTrending == true) "Trending" else "Sought Couture")
    val category = selectedFood?.category ?: selectedShop?.category ?: "Veloura Essence"
    val itemId = selectedFood?.id ?: selectedShop?.id ?: 0L

    val isWishlisted = remember(wishlist, itemId, isFood) {
        val typeStr = if (isFood) "FOOD" else "SHOP"
        wishlist.any { it.itemId == itemId && it.type == typeStr }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(VelouraBlack)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            
            // CINEMATIC ENLARGED DISPLAY
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp)
            ) {
                SmoothImage(
                    url = imageUrl,
                    contentDescription = name,
                    modifier = Modifier.fillMaxSize()
                )

                // Atmospheric shadows
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(0.4f),
                                    Color.Transparent,
                                    VelouraBlack
                                )
                            )
                        )
                )

                // Header overlays button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(0.5f))
                            .clickable { viewModel.navigateBack() }
                            .testTag("details_back_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Go back", tint = Color.White)
                    }

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(0.5f))
                            .clickable {
                                viewModel.toggleWishlist(
                                    itemId = itemId,
                                    name = name,
                                    price = price,
                                    imageUrl = imageUrl,
                                    type = if (isFood) "FOOD" else "SHOP"
                                )
                            }
                            .testTag("details_wish_toggle"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isWishlisted) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorite status toggle",
                            tint = if (isWishlisted) Color.Red else Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // PRIMARY DETAIL WRAPPING INSETS
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 6.dp)
                ) {
                    PremiumIndicatorPill(
                        text = tag,
                        containerColor = if (isFood) VelouraOrange else VelouraGold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "•  $category",
                        color = VelouraChampagne,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = name,
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.testTag("details_title_text")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Pricing Row
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "\u20B9${price.toInt()}",
                        color = VelouraGold,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    if (!isFood && originalPrice > price) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "\u20B9${originalPrice.toInt()}",
                            color = TextMuted,
                            fontSize = 16.sp,
                            style = LocalTextStyle.current.copy(textDecoration = TextDecoration.LineThrough)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4))
                                .background(VelouraOrange.copy(alpha = 0.2f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "${(100 - (price/originalPrice*100)).toInt()}% COUTURE SAVINGS",
                                color = VelouraOrange,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Detailed reviews stars
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(VelouraCharcoal)
                        .padding(10.dp)
                ) {
                    repeat(5) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = VelouraGold, modifier = Modifier.size(16.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "4.9 (42 custom reviews)", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "100% Social Friendly", color = VelouraOrange, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "THE CONCEPT STORY",
                    color = VelouraGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.5.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    color = TextMuted,
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    textAlign = TextAlign.Justify
                )

                Spacer(modifier = Modifier.height(20.dp))

                // SELECTION CHANGER ACCORDING TO MODEL TYPE
                if (isFood) {
                    Text(
                        text = "CULINARY INDEX METRICS",
                        color = VelouraGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.5.sp,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatMetricBlock(
                            label = "Estimated Caloric Load",
                            value = "${selectedFood?.calories ?: 320} kCal",
                            modifier = Modifier.weight(1f)
                        )
                        StatMetricBlock(
                            label = "Prep standard time",
                            value = "15-20 Mins",
                            modifier = Modifier.weight(1f)
                        )
                    }
                } else {
                    // Shop size select
                    Text(
                        text = "SELECT RING SIZE (METALLIC FIT)",
                        color = VelouraGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.5.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        listOf("6", "7", "8", "9", "10 VIP").forEach { size ->
                            val isSelectedSize = selectedRingSize == size
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelectedSize) VelouraGold else VelouraCharcoal)
                                    .border(1.dp, if (isSelectedSize) Color.Transparent else SoftGlassBorder, RoundedCornerShape(8.dp))
                                    .clickable { selectedRingSize = size }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = size,
                                    color = if (isSelectedSize) Color.Black else Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // QUANTITY CONTROLLER BLOCK
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "ORDERING QUANTITY:",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color(0x11FFFFFF))
                                .clickable { if (quantity > 1) quantity-- },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("-", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }

                        Text(
                            text = quantity.toString(),
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.widthIn(min = 20.dp),
                            textAlign = TextAlign.Center
                        )

                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color(0x11FFFFFF))
                                .clickable { quantity++ },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("+", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // SUBMIT CART DETAILS
                GlowingGoldButton(
                    onClick = {
                        if (isFood) {
                            selectedFood?.let { viewModel.addFoodToCart(it, quantity) }
                        } else {
                            selectedShop?.let { viewModel.addShopItemToCart(it, quantity) }
                        }
                        viewModel.navigateBack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                        .testTag("details_add_to_cart_confirm")
                ) {
                    Icon(Icons.Default.AddShoppingCart, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Incorporate $quantity Unit into Bag  •  \u20B9${(price * quantity).toInt()}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(96.dp))
            }
        }
    }
}
