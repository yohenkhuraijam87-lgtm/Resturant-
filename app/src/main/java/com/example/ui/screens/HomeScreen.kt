package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.FoodItem
import com.example.data.ShopItem
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.VelouraViewModel

@Composable
fun HomeScreen(
    viewModel: VelouraViewModel,
    modifier: Modifier = Modifier
) {
    val foods by viewModel.allFoods.collectAsState()
    val shopItems by viewModel.allShopItems.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    val under299Foods = remember(foods) {
        foods.filter { it.price < 299 && it.tag.contains("Under", ignoreCase = true) }
    }

    val trendingShopItems = remember(shopItems) {
        shopItems.filter { it.isTrending }
    }

    var animateEntrance by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        animateEntrance = true
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
            // CINEMATIC HERO SECTION
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(440.dp)
            ) {
                // Background Image
                SmoothImage(
                    url = "https://images.unsplash.com/photo-1514362545857-3bc16c4c7d1b?auto=format&fit=crop&q=80",
                    contentDescription = "Veloura Luxury Lounge Scene",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Immersive gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.5f),
                                    Color.Transparent,
                                    VelouraBlack.copy(alpha = 0.95f),
                                    VelouraBlack
                                )
                            )
                        )
                )

                // Hero Content Box
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AnimatedVisibility(
                        visible = animateEntrance,
                        enter = fadeIn(animationSpec = tween(1000)) + slideInVertically(
                            animationSpec = tween(1000),
                            initialOffsetY = { 60 }
                        )
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(Color(0x33000000))
                                    .border(1.dp, VelouraGold.copy(alpha = 0.4f), RoundedCornerShape(50))
                                    .padding(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = VelouraGold,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "LUXURY EXPERIENCE • AFFORDABLE SAVINGS",
                                    color = VelouraChampagne,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.5.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "VELOURA SOCIAL",
                                color = Color.White,
                                fontSize = 42.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 3.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.testTag("app_hero_title")
                            )

                            Text(
                                text = "Where elite design meets comforting prices.",
                                color = TextMuted,
                                fontSize = 14.sp,
                                fontStyle = FontStyle.Italic,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                GlowingGoldButton(
                                    onClick = { viewModel.navigateTo("reservations") },
                                    modifier = Modifier.weight(1f).testTag("hero_book_table_button")
                                ) {
                                    Icon(Icons.Default.RestaurantMenu, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Book A Table", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }

                                Button(
                                    onClick = { viewModel.navigateTo("shop") },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0x33FFFFFF),
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(50),
                                    border = BorderStroke(1.dp, SoftGlassBorder),
                                    modifier = Modifier.weight(1.0f).height(48.dp)
                                ) {
                                    Icon(Icons.Default.Celebration, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Shop Vault", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            // QUICK NAVIGATION TABS (BENTO CARD style)
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "EXPLORE VELOURA SECTIONS",
                    color = VelouraGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp,
                    modifier = Modifier.padding(bottom = 12.dp, start = 4.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Dining Bento
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(100.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Brush.linearGradient(listOf(Color(0xFF231710), Color(0xFF140D09))))
                            .border(1.dp, Color(0x22EE7E1B), RoundedCornerShape(20.dp))
                            .clickable { viewModel.navigateTo("dining") }
                            .padding(16.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                            Icon(Icons.Default.LocalPizza, contentDescription = null, tint = VelouraOrange, modifier = Modifier.size(24.dp))
                            Text("Menu Crew", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                        Text("Under ₹299", color = VelouraOrange.copy(alpha = 0.8f), fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.TopEnd))
                    }

                    // Shop Bento
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(100.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Brush.linearGradient(listOf(Color(0xFF151821), Color(0xFF0F1116))))
                            .border(1.dp, Color(0x22D4AF37), RoundedCornerShape(20.dp))
                            .clickable { viewModel.navigateTo("shop") }
                            .padding(16.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                            Icon(Icons.Default.Diamond, contentDescription = null, tint = VelouraGold, modifier = Modifier.size(24.dp))
                            Text("Jewelry Space", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                        Text("Hot Rings", color = VelouraGold.copy(alpha = 0.8f), fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.TopEnd))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Deals Bento
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(76.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Brush.linearGradient(listOf(Color(0xFF1A1A24), Color(0xFF101016))))
                            .border(1.dp, SoftGlassBorder, RoundedCornerShape(16.dp))
                            .clickable { viewModel.navigateTo("profile") } // holds coupon lists
                            .padding(12.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CardGiftcard, contentDescription = null, tint = VelouraChampagne, modifier = Modifier.size(22.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("Gifts & Coupons", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text("Premium codes inside", color = TextMuted, fontSize = 10.sp)
                            }
                        }
                    }

                    // Admin Bento Gateway
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(76.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Brush.linearGradient(listOf(Color(0xFF261D19), Color(0xFF1A1412))))
                            .border(1.dp, VelouraOrange.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                            .clickable { viewModel.navigateTo("admin") }
                            .padding(12.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = VelouraOrange, modifier = Modifier.size(22.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("Admin System", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text("Manage inventory", color = TextMuted, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // AFFORDABLE FINE DINING BANNER SECTION
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "CULINARY EXCELLENCE",
                            color = VelouraGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 2.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Luxury Under \u20B9299",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "View Menu",
                        color = VelouraOrange,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier
                            .clickable { viewModel.navigateTo("dining") }
                            .padding(4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (under299Foods.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Awaiting gourmet selections. Check back shortly!",
                            color = TextMuted,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(under299Foods) { food ->
                            HomeFoodCard(
                                food = food,
                                onClick = { viewModel.selectFoodAndNavigate(food) },
                                onAddToCart = { viewModel.addFoodToCart(food) }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // COUTURE PRODUCTS BANNER SECTION
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "THE METALLIC VAULT",
                            color = VelouraGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 2.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Trending Rings & Couture",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "Shop All",
                        color = VelouraGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier
                            .clickable { viewModel.navigateTo("shop") }
                            .padding(4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (trendingShopItems.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Couture drop loading active soon.",
                            color = TextMuted,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(trendingShopItems) { item ->
                            HomeShopCard(
                                item = item,
                                onClick = { viewModel.selectShopItemAndNavigate(item) },
                                onAddToCart = { viewModel.addShopItemToCart(item) }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // HIGHLIGHTED LOCATIONS ("MOST INSTAGRAMMABLE SPOTS")
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)) {
                Text(
                    text = "LUXURIOUS SOCIAL SCENERY",
                    color = VelouraGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Most Instagrammable Spot",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                LocationHighlightBlock(
                    title = "Veloura Mumbai Lounge",
                    imageUrl = "https://images.unsplash.com/photo-1514362545857-3bc16c4c7d1b?w=600&auto=format&fit=crop",
                    address = "Rooftop Sky Terrace, Nariman Point, Mumbai",
                    rating = "4.9",
                    desc = "Elevated 40 stories high. Features a custom gold reflection pool, glass deck flooring, and affordable tableside candlelight cocktails. Ideal for couples and young curators.",
                    onReserve = { viewModel.navigateTo("reservations") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                LocationHighlightBlock(
                    title = "Veloura Delhi Imperial Dome",
                    imageUrl = "https://images.unsplash.com/photo-1544025162-d76694265947?w=600&auto=format&fit=crop",
                    address = "The Glass Canopy, Connaught Place, New Delhi",
                    rating = "4.8",
                    desc = "Clad with fully interactive organic botanical domes and elegant brass chandeliers. Highly aesthetic backdrop lighting designed for active content creation.",
                    onReserve = { viewModel.navigateTo("reservations") }
                )
            }

            // REWARDS PROMO MINI CALL OUT
            GlassmorphicCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                cornerRadius = 24.dp
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Stars,
                        contentDescription = null,
                        tint = VelouraGold,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "VELOURA ROYAL CLUB",
                        color = VelouraGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.5.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "You possess ${userProfile.rewardPoints} loyalty points",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Every order earns 10% cashpoints valid at any global Veloura social club. Redeem for free VIP lounge desserts or infinity band boxes.",
                        color = TextMuted,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp, bottom = 12.dp, start = 8.dp, end = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(96.dp)) // Cushion for bottom bar
        }
    }
}

@Composable
fun HomeFoodCard(
    food: FoodItem,
    onClick: () -> Unit,
    onAddToCart: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = VelouraCharcoal),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, SoftGlassBorder),
        modifier = Modifier
            .width(220.dp)
            .clickable { onClick() }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                SmoothImage(
                    url = food.imageUrl,
                    contentDescription = food.name,
                    modifier = Modifier.fillMaxSize()
                )

                PremiumIndicatorPill(
                    text = food.tag,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp),
                    containerColor = VelouraOrange
                )
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = food.name,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = food.description,
                    color = TextMuted,
                    fontSize = 11.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 15.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "\u20B9${food.price.toInt()}",
                        color = VelouraGold,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(VelouraGold)
                            .clickable { onAddToCart() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add to cart",
                            tint = Color.Black,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HomeShopCard(
    item: ShopItem,
    onClick: () -> Unit,
    onAddToCart: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = VelouraCharcoal),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, SoftGlassBorder),
        modifier = Modifier
            .width(220.dp)
            .clickable { onClick() }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                SmoothImage(
                    url = item.imageUrl,
                    contentDescription = item.name,
                    modifier = Modifier.fillMaxSize()
                )

                if (item.isBestSeller) {
                    PremiumIndicatorPill(
                        text = "Best Seller",
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(10.dp),
                        containerColor = VelouraGold
                    )
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = item.name,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.description,
                    color = TextMuted,
                    fontSize = 11.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 15.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
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
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "\u20B9${item.originalPrice.toInt()}",
                                color = TextMuted,
                                fontSize = 10.sp,
                                style = LocalTextStyle.current.copy(textDecoration = TextDecoration.LineThrough)
                            )
                        }
                        Text(
                            text = "${(100 - (item.price/item.originalPrice*100)).toInt()}% OFF",
                            color = VelouraOrange,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(VelouraGold)
                            .clickable { onAddToCart() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = "Add to cart",
                            tint = Color.Black,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LocationHighlightBlock(
    title: String,
    imageUrl: String,
    address: String,
    rating: String,
    desc: String,
    onReserve: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(VelouraCharcoal)
            .border(1.dp, SoftGlassBorder, RoundedCornerShape(24.dp))
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                SmoothImage(
                    url = imageUrl,
                    contentDescription = title,
                    modifier = Modifier.fillMaxSize()
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(20))
                        .background(Color.Black.copy(alpha = 0.7f))
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = VelouraGold, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = rating, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(10))
                        .background(VelouraOrange)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Most Instagrammable Spot",
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                    Icon(Icons.Default.Map, contentDescription = null, tint = VelouraGold, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = address, color = TextMuted, fontSize = 11.sp)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = desc,
                    color = TextMuted,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )
                Spacer(modifier = Modifier.height(14.dp))
                GlowingGoldButton(
                    onClick = onReserve,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.RestaurantMenu, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Instant Reserve Candlelight Spot", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
        }
    }
}
