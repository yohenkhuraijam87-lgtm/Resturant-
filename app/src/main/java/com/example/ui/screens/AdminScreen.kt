package com.example.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.VelouraViewModel

@Composable
fun AdminScreen(
    viewModel: VelouraViewModel,
    modifier: Modifier = Modifier
) {
    val foods by viewModel.allFoods.collectAsState()
    val shopItems by viewModel.allShopItems.collectAsState()
    val reservations by viewModel.allReservations.collectAsState()
    val orders by viewModel.allOrders.collectAsState()

    var activeSubTab by remember { mutableStateOf("analytics") } // "analytics", "food", "shop", "reservations"

    // Food form values
    var foodName by remember { mutableStateOf("") }
    var foodPrice by remember { mutableStateOf("") }
    var foodDesc by remember { mutableStateOf("") }
    var foodCategory by remember { mutableStateOf("Signature Cru") }
    var foodTag by remember { mutableStateOf("Luxury Under \u20B9299") }
    var foodImageUrl by remember { mutableStateOf("https://images.unsplash.com/photo-1544025162-d76694265947?w=600&auto=format&fit=crop") }
    var foodIsCombo by remember { mutableStateOf(false) }
    var foodIsHappyHour by remember { mutableStateOf(false) }

    // Shop form values
    var shopName by remember { mutableStateOf("") }
    var shopPrice by remember { mutableStateOf("") }
    var shopOrigPrice by remember { mutableStateOf("") }
    var shopDesc by remember { mutableStateOf("") }
    var shopCategory by remember { mutableStateOf("Elysian Rings") }
    var shopImageUrl by remember { mutableStateOf("https://images.unsplash.com/photo-1605100804763-247f67b3557e?w=600&auto=format&fit=crop") }
    var shopIsTrending by remember { mutableStateOf(false) }
    var shopIsBestSeller by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(VelouraBlack)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            
            // ADMIN PORTAL HEADER
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(VelouraCharcoal)
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "VELOURA CONTROL LOUNGE",
                        color = VelouraGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        text = "System Administration",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(onClick = { viewModel.navigateTo("home") }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back home", tint = Color.White)
                }
            }

            // LEVEL CONSOLE TABS
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .background(VelouraCharcoal)
                    .padding(bottom = 10.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    "analytics" to "Stats Engine",
                    "food" to "Manage Dishes",
                    "shop" to "Manage Shop",
                    "reservations" to "Reservations Master"
                ).forEach { (tabKey, label) ->
                    val isSelected = activeSubTab == tabKey
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) VelouraGold else Color(0x0FFFFFFF))
                            .clickable { activeSubTab = tabKey }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                            .testTag("admin_sub_tab_$tabKey")
                    ) {
                        Text(
                            text = label,
                            color = if (isSelected) Color.Black else Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // PRIMARY SUBPANEL CONTENT
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                when (activeSubTab) {
                    "analytics" -> {
                        Text(
                            text = "REAL-TIME METRIC SUMMARY",
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
                            StatMetricBlock(
                                label = "Consolidated Sales",
                                value = "\u20B9${452000 + orders.sumOf { it.totalAmount }.toInt()}",
                                subLabel = "+14.8% simulated",
                                icon = Icons.Default.CurrencyExchange,
                                modifier = Modifier.weight(1f)
                            )

                            StatMetricBlock(
                                label = "Active Bookings",
                                value = "${reservations.size} Seats",
                                subLabel = "Tables assigned",
                                icon = Icons.Default.EventSeat,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            StatMetricBlock(
                                label = "Menu Items count",
                                value = "${foods.size} Dishes",
                                subLabel = "Active kitchens",
                                icon = Icons.Default.Restaurant,
                                modifier = Modifier.weight(1f)
                            )

                            StatMetricBlock(
                                label = "Shop Vault count",
                                value = "${shopItems.size} Pieces",
                                subLabel = "Couture in inventory",
                                icon = Icons.Default.Diamond,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Simulated Graph
                        Text(
                            text = "SIMULATED REVENUE PROGRESS (₹ THOUSANDS)",
                            color = VelouraGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.5.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                listOf(40, 56, 82, 110, 150, 240, 310, 452).forEachIndexed { index, value ->
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Bottom,
                                        modifier = Modifier.fillMaxHeight()
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .width(18.dp)
                                                .height((value * 0.3).dp)
                                                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                                .background(if (index == 7) VelouraOrange else VelouraGold)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(text = "M${index + 1}", color = TextMuted, fontSize = 8.sp)
                                    }
                                }
                            }
                        }
                    }

                    "food" -> {
                        Text(
                            text = "DEPLOY NEW FINE CUISINE DROP",
                            color = VelouraGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.5.sp,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = foodName,
                                onValueChange = { foodName = it },
                                label = { Text("Dish Name (e.g. Caviar Sliders)", color = TextMuted) },
                                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = VelouraGold, unfocusedBorderColor = SoftGlassBorder),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).testTag("admin_food_name_input")
                            )

                            OutlinedTextField(
                                value = foodPrice,
                                onValueChange = { foodPrice = it },
                                label = { Text("Price in \u20B9 (e.g. 249)", color = TextMuted) },
                                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = VelouraGold, unfocusedBorderColor = SoftGlassBorder),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).testTag("admin_food_price_input")
                            )

                            OutlinedTextField(
                                value = foodDesc,
                                onValueChange = { foodDesc = it },
                                label = { Text("Sensorial Description Text", color = TextMuted) },
                                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = VelouraGold, unfocusedBorderColor = SoftGlassBorder),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                            )

                            OutlinedTextField(
                                value = foodImageUrl,
                                onValueChange = { foodImageUrl = it },
                                label = { Text("Unsplash HD Image URL", color = TextMuted) },
                                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = VelouraGold, unfocusedBorderColor = SoftGlassBorder),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                            )

                            OutlinedTextField(
                                value = foodCategory,
                                onValueChange = { foodCategory = it },
                                label = { Text("Category: Signature Cru, Combos & Sliders, Happy Hour", color = TextMuted) },
                                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = VelouraGold, unfocusedBorderColor = SoftGlassBorder),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                            )

                            GlowingGoldButton(
                                onClick = {
                                    val parsedPrice = foodPrice.toDoubleOrNull() ?: 199.0
                                    viewModel.addNewFoodFromAdmin(
                                        name = foodName.trim(),
                                        desc = foodDesc.trim(),
                                        price = parsedPrice,
                                        imageUrl = foodImageUrl.trim(),
                                        category = foodCategory.trim(),
                                        tag = foodTag.trim(),
                                        isCombo = foodIsCombo,
                                        isHappyHour = foodIsHappyHour
                                    )
                                    foodName = ""
                                    foodPrice = ""
                                    foodDesc = ""
                                },
                                modifier = Modifier.fillMaxWidth().testTag("admin_food_submit_btn"),
                                enabled = foodName.isNotBlank() && foodPrice.isNotBlank()
                            ) {
                                Icon(Icons.Default.Upload, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Deploy Gourmet Item Live", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // ACTIVE DISHES LIST LIST
                        Text(
                            text = "CURRENT MENU INVENTORY",
                            color = VelouraGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.5.sp,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )

                        foods.forEach { food ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(VelouraCharcoal)
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(food.name, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("\u20B9${food.price.toInt()}", color = VelouraGold, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    IconButton(
                                        onClick = { viewModel.deleteFoodItem(food.id) },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }

                    "shop" -> {
                        Text(
                            text = "DEPLOY NEW LUXURY VAULT PIECE",
                            color = VelouraGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.5.sp,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = shopName,
                                onValueChange = { shopName = it },
                                label = { Text("Product Accessory Title", color = TextMuted) },
                                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = VelouraGold, unfocusedBorderColor = SoftGlassBorder),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).testTag("admin_shop_name_input")
                            )

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = shopPrice,
                                    onValueChange = { shopPrice = it },
                                    label = { Text("Deal Price \u20B9", color = TextMuted) },
                                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = VelouraGold, unfocusedBorderColor = SoftGlassBorder),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.weight(1f).padding(bottom = 8.dp).testTag("admin_shop_price_input")
                                )

                                OutlinedTextField(
                                    value = shopOrigPrice,
                                    onValueChange = { shopOrigPrice = it },
                                    label = { Text("Original Price \u20B9", color = TextMuted) },
                                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = VelouraGold, unfocusedBorderColor = SoftGlassBorder),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.weight(1f).padding(bottom = 8.dp)
                                )
                            }

                            OutlinedTextField(
                                value = shopDesc,
                                onValueChange = { shopDesc = it },
                                label = { Text("Product Story description", color = TextMuted) },
                                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = VelouraGold, unfocusedBorderColor = SoftGlassBorder),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                            )

                            OutlinedTextField(
                                value = shopImageUrl,
                                onValueChange = { shopImageUrl = it },
                                label = { Text("Unsplash HD Image URL", color = TextMuted) },
                                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = VelouraGold, unfocusedBorderColor = SoftGlassBorder),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                            )

                            OutlinedTextField(
                                value = shopCategory,
                                onValueChange = { shopCategory = it },
                                label = { Text("Category: Elysian Rings, Chains & Chokers, Artisanal Keepsakes", color = TextMuted) },
                                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, focusedBorderColor = VelouraGold, unfocusedBorderColor = SoftGlassBorder),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                            )

                            GlowingGoldButton(
                                onClick = {
                                    val parsedPrice = shopPrice.toDoubleOrNull() ?: 999.0
                                    val parsedOrigPrice = shopOrigPrice.toDoubleOrNull() ?: (parsedPrice * 2)
                                    viewModel.addNewShopItemFromAdmin(
                                        name = shopName.trim(),
                                        desc = shopDesc.trim(),
                                        price = parsedPrice,
                                        originalPrice = parsedOrigPrice,
                                        imageUrl = shopImageUrl.trim(),
                                        category = shopCategory.trim(),
                                        isTrending = shopIsTrending,
                                        isBestSeller = shopIsBestSeller
                                    )
                                    shopName = ""
                                    shopPrice = ""
                                    shopOrigPrice = ""
                                    shopDesc = ""
                                },
                                modifier = Modifier.fillMaxWidth().testTag("admin_shop_submit_btn"),
                                enabled = shopName.isNotBlank() && shopPrice.isNotBlank()
                            ) {
                                Icon(Icons.Default.Upload, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Deploy Boutique Couture Piece", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // ACTIVE COUTURE VAULT INVENTORY
                        Text(
                            text = "CURRENT VAULT INVENTORY",
                            color = VelouraGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.5.sp,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )

                        shopItems.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(VelouraCharcoal)
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(item.name, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("\u20B9${item.price.toInt()}", color = VelouraGold, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    IconButton(
                                        onClick = { viewModel.deleteShopItem(item.id) },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete from vault", tint = Color.Red, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }

                    "reservations" -> {
                        Text(
                            text = "ACTIVE GUEST RESERVATION ROSTER",
                            color = VelouraGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.5.sp,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        if (reservations.isEmpty()) {
                            GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), contentAlignment = Alignment.Center) {
                                    Text("Roster is empty. No user scheduling booked yet.", color = TextMuted, fontSize = 12.sp)
                                }
                            }
                        } else {
                            reservations.forEach { r ->
                                GlassmorphicCard(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
                                ) {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                        Column {
                                            Text("GUEST: ${r.name}", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                            Text("Branch: ${r.branchName}", color = TextMuted, fontSize = 11.sp)
                                            Text("Guests: ${r.guests} | Timing: ${r.date} @ ${r.time}", color = TextMuted, fontSize = 11.sp)
                                            Text("Phone: ${r.phone} | Floor: ${r.section}", color = VelouraChampagne, fontSize = 10.sp)
                                        }

                                        Column(horizontalAlignment = Alignment.End) {
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(4))
                                                    .background(if (r.status == "Approved" || r.status == "Confirmed") Color(0xFF22C55E) else Color(0xFFEF4444))
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text(r.status, color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                            }

                                            if (r.status == "Confirmed" || r.status == "Pending") {
                                                Spacer(modifier = Modifier.height(6.dp))
                                                Button(
                                                    onClick = { viewModel.approveReservationInAdmin(r.id) },
                                                    colors = ButtonDefaults.buttonColors(containerColor = VelouraGold, contentColor = Color.Black),
                                                    shape = RoundedCornerShape(6.dp),
                                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                                    modifier = Modifier.height(28.dp).testTag("approve_btn_${r.id}")
                                                ) {
                                                    Text("Approve Spot", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(112.dp))
            }
        }
    }
}
