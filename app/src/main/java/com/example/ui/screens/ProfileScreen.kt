package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Order
import com.example.data.Reservation
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.VelouraViewModel

@Composable
fun ProfileScreen(
    viewModel: VelouraViewModel,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val orders by viewModel.allOrders.collectAsState()
    val reservations by viewModel.allReservations.collectAsState()

    var isEditingProfile by remember { mutableStateOf(false) }
    var editName by remember { mutableStateOf(userProfile.name) }
    var editEmail by remember { mutableStateOf(userProfile.email) }
    var editPhone by remember { mutableStateOf(userProfile.phone) }

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
            
            // PROFILE HERO BLOCK
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF231F14), VelouraBlack)
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Avatar bubble
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(VelouraGold)
                            .border(3.dp, Color(0xFFFFEA9F), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userProfile.name.take(2).uppercase(),
                            color = Color.Black,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = userProfile.name,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.testTag("profile_user_name")
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(VelouraOrange.copy(alpha = 0.2f))
                            .border(1.dp, VelouraOrange, RoundedCornerShape(50))
                            .padding(horizontal = 14.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.CardMembership, contentDescription = null, tint = VelouraOrange, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = userProfile.tier.uppercase(),
                            color = VelouraOrange,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tier status gauge progress
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Current: Gold Elite", color = TextMuted, fontSize = 11.sp)
                            Text("Upgrade: Platinum", color = VelouraGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { 0.75f },
                            color = VelouraGold,
                            trackColor = SoftGlassBorder,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(50))
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Spend ₹550 more under dining coupon to unlock free concierge service.",
                            color = TextMuted,
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // LOYALTY CARD & COUPONS SECTION
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                StatMetricBlock(
                    label = "ACCUMULATED REWARD POINTS",
                    value = "${userProfile.rewardPoints} PTS",
                    subLabel = "\u2248 \u20B9${userProfile.rewardPoints / 2} Loyalty cashback cashable",
                    icon = Icons.Default.Stars,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Available Coupons
                Text(
                    text = "AVAILABLE VIP OFFERS (CLICK TO COPY)",
                    color = VelouraGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.5.sp,
                    modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    couponItem(
                        code = "VELOURA100",
                        benefit = "\u20B9100 OFF",
                        minimum = "On any dining bag",
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.applyCoupon("VELOURA100") }
                    )
                    couponItem(
                        code = "VIPMEMBER",
                        benefit = "15% DISCOUNT",
                        minimum = "All items, no limit",
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.applyCoupon("VIPMEMBER") }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ACTIVE ORDERS SECTOR
                Text(
                    text = "MY ACTIVE ORDER TRACKING",
                    color = VelouraGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.5.sp,
                    modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
                )

                if (orders.isEmpty()) {
                    GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.ShoppingBag, contentDescription = null, tint = TextMuted, modifier = Modifier.size(36.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("No order transactions logged.", color = TextMuted, fontSize = 13.sp)
                        }
                    }
                } else {
                    orders.forEach { order ->
                        ActiveOrderTrackingCard(order = order)
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // HISTORIC RESERVATIONS
                Text(
                    text = "VIP RESERVATIONS SCHEDULE",
                    color = VelouraGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.5.sp,
                    modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
                )

                if (reservations.isEmpty()) {
                    GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.RestaurantMenu, contentDescription = null, tint = TextMuted, modifier = Modifier.size(36.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("No future reservations in scheduling book.", color = TextMuted, fontSize = 13.sp)
                        }
                    }
                } else {
                    reservations.forEach { r ->
                        ReservationItemCard(
                            reservation = r,
                            onCancel = { viewModel.deleteReservation(r.id) }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // CUSTOMER SETTINGS ACCORDEON
                Text(
                    text = "MANAGE PROFILE CREDENTIALS",
                    color = VelouraGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.5.sp,
                    modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
                )

                GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                    if (!isEditingProfile) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = VelouraGold, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Full Name: ${userProfile.name}", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }
                                IconButton(onClick = { isEditingProfile = true }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit profiles settings", tint = VelouraOrange, modifier = Modifier.size(16.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("VIP Contact Email: ${userProfile.email}", color = TextMuted, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Secure Registry Mobile: ${userProfile.phone}", color = TextMuted, fontSize = 12.sp)
                        }
                    } else {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = editName,
                                onValueChange = { editName = it },
                                label = { Text("Display Name", color = TextMuted) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = VelouraGold,
                                    unfocusedBorderColor = SoftGlassBorder,
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                            )

                            OutlinedTextField(
                                value = editEmail,
                                onValueChange = { editEmail = it },
                                label = { Text("Contact Email", color = TextMuted) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = VelouraGold,
                                    unfocusedBorderColor = SoftGlassBorder,
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                            )

                            OutlinedTextField(
                                value = editPhone,
                                onValueChange = { editPhone = it },
                                label = { Text("Contact Hotline", color = TextMuted) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = VelouraGold,
                                    unfocusedBorderColor = SoftGlassBorder,
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = { isEditingProfile = false },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.White),
                                    border = BorderStroke(1.dp, SoftGlassBorder),
                                    shape = RoundedCornerShape(50),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Cancel", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }

                                GlowingGoldButton(
                                    onClick = {
                                        viewModel.updateProfile(editName, editEmail, editPhone)
                                        isEditingProfile = false
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Save Changes", fontSize = 11.sp, fontWeight = FontWeight.Bold)
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

@Composable
fun couponItem(
    code: String,
    benefit: String,
    minimum: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.linearGradient(listOf(Color(0xFF1E1F26), Color(0xFF14151B))))
            .border(1.dp, VelouraGold.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(12.dp)
            .testTag("coupon_item_$code")
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(VelouraGold.copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Icon(Icons.Default.CardGiftcard, contentDescription = null, tint = VelouraGold, modifier = Modifier.size(10.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = code, color = VelouraGold, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(text = benefit, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
            Text(text = minimum, color = TextMuted, fontSize = 10.sp, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun ActiveOrderTrackingCard(order: Order) {
    GlassmorphicCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 20.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "ORDER ID: #VL${order.id}109", color = VelouraGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text(text = order.date, color = TextMuted, fontSize = 11.sp)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = order.itemNames,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Progress tracking graphics
            val currentStatus = order.status
            val currentStep = when (currentStatus) {
                "Preparing" -> 0
                "On the Way" -> 1
                "Completed" -> 2
                else -> 0
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                trackingStepIndicator(label = "Cooking", isActive = currentStep >= 0, isDone = currentStep > 0)
                trackingConnectingBar(isActive = currentStep >= 1)
                trackingStepIndicator(label = "Dispatch", isActive = currentStep >= 1, isDone = currentStep > 1)
                trackingConnectingBar(isActive = currentStep >= 2)
                trackingStepIndicator(label = "Arrived", isActive = currentStep >= 2, isDone = currentStep > 2)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Delivery Model: ${order.deliveryType}",
                    color = TextMuted,
                    fontSize = 11.sp
                )

                Text(
                    text = "TOTAL PAID: \u20B9${order.totalAmount.toInt()}",
                    color = VelouraOrange,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

@Composable
fun trackingStepIndicator(label: String, isActive: Boolean, isDone: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(
                    if (isDone) VelouraGold
                    else if (isActive) VelouraOrange
                    else Color(0x33FFFFFF)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isDone) Icons.Default.Check
                else if (isActive) Icons.Default.DirectionsRun
                else Icons.Default.HourglassBottom,
                contentDescription = null,
                tint = if (isDone || isActive) Color.Black else Color.White,
                modifier = Modifier.size(12.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = if (isActive) Color.White else TextMuted,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun RowScope.trackingConnectingBar(isActive: Boolean) {
    Box(
        modifier = Modifier
            .weight(1f)
            .height(2.dp)
            .background(if (isActive) VelouraGold else Color(0x22FFFFFF))
            .padding(horizontal = 4.dp)
    )
}

@Composable
fun ReservationItemCard(
    reservation: Reservation,
    onCancel: () -> Unit
) {
    GlassmorphicCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("reservation_item_${reservation.id}"),
        cornerRadius = 20.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Restaurant, contentDescription = null, tint = VelouraGold, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = reservation.branchName, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4))
                        .background(
                            when (reservation.status) {
                                "Confirmed" -> Color(0xFF22C55E)
                                "Approved" -> Color(0xFF3B82F6)
                                "Cancelled" -> Color(0xFFEF4444)
                                else -> Color.Gray
                            }
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = reservation.status,
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Date: ${reservation.date} | Space: ${reservation.section}", color = TextMuted, fontSize = 11.sp)
                Text(text = "Time: ${reservation.time}", color = TextMuted, fontSize = 11.sp)
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Guests Count: ${reservation.guests} VIPs", color = VelouraChampagne, fontSize = 11.sp)

            if (reservation.status != "Cancelled") {
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = onCancel,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0x1AEF4444), contentColor = Color(0xFFEF4444)),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier
                        .align(Alignment.End)
                        .height(32.dp)
                        .testTag("cancel_reservation_${reservation.id}")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Cancel, contentDescription = null, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Cancel Spot", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
