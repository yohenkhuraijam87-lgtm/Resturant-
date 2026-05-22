package com.example.ui.screens

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CartItem
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.VelouraViewModel

@Composable
fun CartScreen(
    viewModel: VelouraViewModel,
    modifier: Modifier = Modifier
) {
    val cartList by viewModel.cartItems.collectAsState()
    val subtotal by viewModel.cartSubtotal.collectAsState()
    val appliedCoupon by viewModel.appliedCoupon.collectAsState()

    var deliveryModel by remember { mutableStateOf("Delivery") } // "Delivery" or "Pickup"
    var couponInput by remember { mutableStateOf("") }

    val couponDiscount = remember(subtotal, appliedCoupon) {
        if (appliedCoupon == "VELOURA100") 100.0
        else if (appliedCoupon == "VIPMEMBER") subtotal * 0.15
        else 0.0
    }

    val shippingFee = remember(deliveryModel) {
        if (deliveryModel == "Delivery") 49.0 else 0.0
    }

    val finalTotal = remember(subtotal, couponDiscount, shippingFee) {
        (subtotal - couponDiscount + shippingFee).coerceAtLeast(0.0)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(VelouraBlack)
    ) {
        if (cartList.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingBag,
                    contentDescription = "Empty bag icon",
                    tint = TextMuted,
                    modifier = Modifier.size(76.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Your elite bag is empty.",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Explore culinary categories or stylish rings inside the boutique layout to log treasures.",
                    color = TextMuted,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                GlowingGoldButton(
                    onClick = { viewModel.navigateTo("home") }
                ) {
                    Text("Return to Lounge Home", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                
                // CART LIST HEADER
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
                            text = "MY SELECTED BAG",
                            color = VelouraGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.5.sp
                        )
                        Text(
                            text = "Checkout Summary",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(
                        onClick = { viewModel.navigateBack() }
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Go back", tint = Color.White)
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    
                    // CART ITEMS LIST
                    cartList.forEach { item ->
                        CartItemRow(
                            item = item,
                            onQuantityIncrease = { viewModel.updateCartItemQuantity(item, 1) },
                            onQuantityDecrease = { viewModel.updateCartItemQuantity(item, -1) },
                            onRemove = { viewModel.removeCartItem(item) }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // DELIVERY MODE SWITCH
                    Text(
                        text = "FULFILLMENT MODE SELECTION",
                        color = VelouraGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.5.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(VelouraCharcoal)
                            .padding(6.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Button(
                            onClick = { deliveryModel = "Delivery" },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (deliveryModel == "Delivery") VelouraGold else Color.Transparent,
                                contentColor = if (deliveryModel == "Delivery") Color.Black else Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                        ) {
                            Icon(Icons.Default.LocalShipping, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Delivery (\u20B949)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { deliveryModel = "Pickup" },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (deliveryModel == "Pickup") VelouraGold else Color.Transparent,
                                contentColor = if (deliveryModel == "Pickup") Color.Black else Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                        ) {
                            Icon(Icons.Default.Storefront, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Lounge Pickup", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // PROMO CODE FIELD
                    Text(
                        text = "APPLY VIP DISCOUNT COUPON",
                        color = VelouraGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.5.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = couponInput,
                            onValueChange = { couponInput = it },
                            placeholder = { Text("VELOURA100, VIPMEMBER", color = TextMuted) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = VelouraGold,
                                unfocusedBorderColor = SoftGlassBorder,
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("cart_coupon_input"),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Button(
                            onClick = {
                                if (couponInput.isNotBlank()) {
                                    viewModel.applyCoupon(couponInput.trim().uppercase())
                                    couponInput = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = VelouraGold, contentColor = Color.Black),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .height(56.dp)
                                .testTag("apply_coupon_button")
                        ) {
                            Text("Apply", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    if (appliedCoupon != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text(
                                text = "Coupon `$appliedCoupon` currently active!",
                                color = VelouraOrange,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "(Remove)",
                                color = Color.Red,
                                fontSize = 10.sp,
                                textDecoration = TextDecoration.Underline,
                                modifier = Modifier.clickable { viewModel.removeCoupon() }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // CALCULATED BREAKDOWN
                    Text(
                        text = "PRICING CALCULATIONS",
                        color = VelouraGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.5.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Standard Subtotal", color = TextMuted, fontSize = 12.sp)
                            Text("\u20B9${subtotal.toInt()}", color = Color.White, fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Coupon Discount", color = TextMuted, fontSize = 12.sp)
                            Text("- \u20B9${couponDiscount.toInt()}", color = VelouraOrange, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Shipping / Concierge", color = TextMuted, fontSize = 12.sp)
                            Text(if (shippingFee > 0) "\u20B9${shippingFee.toInt()}" else "FREE", color = Color.White, fontSize = 12.sp)
                        }
                        Divider(modifier = Modifier.padding(vertical = 10.dp), color = SoftGlassBorder)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("FINAL CHECKOUT TOTAL", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text("\u20B9${finalTotal.toInt()}", color = VelouraGold, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    // ORDER TRIGGER BUTTON
                    GlowingGoldButton(
                        onClick = { viewModel.checkout(deliveryModel) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("checkout_order_submit_button")
                    ) {
                        Icon(Icons.Default.Payment, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Checkout Order (\u20B9${finalTotal.toInt()})",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(96.dp))
                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    item: CartItem,
    onQuantityIncrease: () -> Unit,
    onQuantityDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(VelouraCharcoal)
            .border(1.dp, SoftGlassBorder, RoundedCornerShape(16.dp))
            .padding(10.dp)
            .testTag("cart_item_row_${item.id}")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tiny Thumbnail
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(10.dp))
            ) {
                SmoothImage(url = item.imageUrl, contentDescription = item.name, modifier = Modifier.fillMaxSize())
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Body
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${item.type} • \u20B9${item.price.toInt()} each",
                    color = TextMuted,
                    fontSize = 10.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Total: \u20B9${(item.price * item.quantity).toInt()}",
                    color = VelouraGold,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Controls
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Minus button
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(Color(0x1AFFFFFF))
                        .clickable { onQuantityDecrease() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("-", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }

                Text(
                    text = item.quantity.toString(),
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.widthIn(min = 16.dp),
                    textAlign = TextAlign.Center
                )

                // Plus button
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(Color(0x1AFFFFFF))
                        .clickable { onQuantityIncrease() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("+", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                // Delete Trash
                IconButton(onClick = onRemove, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove cart item", tint = Color.Red.copy(alpha = 0.8f), modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}
