package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.viewmodel.VelouraViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                VelouraSocialMainLayout()
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun VelouraSocialMainLayout() {
    val context = LocalContext.current
    val vm: VelouraViewModel = viewModel()

    val currentScreen by vm.currentScreen.collectAsState()
    val cartCount by vm.cartItemCount.collectAsState()
    val userMessage by vm.userMessage.collectAsState()

    // Handle incoming messages gracefully with Native Toasts
    LaunchedEffect(userMessage) {
        userMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            vm.clearUserMessage()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("app_root_scaffold"),
        contentWindowInsets = WindowInsets.safeDrawing, // Edge-to-Edge window inset safety
        topBar = {
            // Show custom premium navigation header except in Details Screen
            if (currentScreen != "details") {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "VELOURA SOCIAL",
                            color = VelouraGold,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 2.5.sp,
                            modifier = Modifier.testTag("app_header_logo")
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { vm.navigateTo("home") },
                            modifier = Modifier.testTag("nav_icon_home_logo")
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "Veloura Brand logo symbol",
                                tint = VelouraGold
                            )
                        }
                    },
                    actions = {
                        // Shopping Bag with numerical badging
                        Box(
                            modifier = Modifier
                                .padding(end = 6.dp)
                                .size(48.dp)
                                .clip(CircleShape)
                                .clickable { vm.navigateTo("cart") }
                                .testTag("top_cart_gateway"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingBag,
                                contentDescription = "Shopping Bag Cart",
                                tint = VelouraGold,
                                modifier = Modifier.size(24.dp)
                            )
                            if (cartCount > 0) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(top = 4.dp, end = 4.dp)
                                        .size(18.dp)
                                        .clip(CircleShape)
                                        .background(VelouraOrange)
                                        .border(1.dp, Color.Black, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = cartCount.toString(),
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = VelouraCharcoal,
                        titleContentColor = Color.White
                    )
                )
            }
        },
        bottomBar = {
            // Hide bottom navigation in overlay checkout and details pages for clean immersive focus
            if (currentScreen != "cart" && currentScreen != "admin" && currentScreen != "details") {
                NavigationBar(
                    containerColor = VelouraCharcoal,
                    contentColor = TextWhite,
                    tonalElevation = 8.dp,
                    modifier = Modifier.testTag("main_bottom_nav_bar")
                ) {
                    val tabs = listOf(
                        "home" to Triple("Lounge", Icons.Default.Home, Icons.Outlined.Home),
                        "dining" to Triple("Dining", Icons.Default.RestaurantMenu, Icons.Outlined.RestaurantMenu),
                        "shop" to Triple("Boutique", Icons.Default.Diamond, Icons.Outlined.Diamond),
                        "reservations" to Triple("Reserve", Icons.Default.EventNote, Icons.Outlined.EventNote),
                        "profile" to Triple("My Club", Icons.Default.AccountCircle, Icons.Outlined.AccountCircle)
                    )

                    tabs.forEach { (route, triple) ->
                        val (label, filledIcon, outlinedIcon) = triple
                        val isSelected = currentScreen == route

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { vm.navigateTo(route) },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) filledIcon else outlinedIcon,
                                    contentDescription = label,
                                    tint = if (isSelected) Color.Black else VelouraChampagne
                                )
                            },
                            label = {
                                Text(
                                    text = label,
                                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Normal,
                                    fontSize = 11.sp,
                                    letterSpacing = 0.5.sp
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.Black,
                                unselectedIconColor = VelouraChampagne,
                                selectedTextColor = VelouraGold,
                                unselectedTextColor = TextMuted,
                                indicatorColor = VelouraGold
                            ),
                            modifier = Modifier.testTag("nav_item_$route")
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        // Fluid transition container content
        AnimatedContent(
            targetState = currentScreen,
            transitionSpec = {
                (fadeIn(animationSpec = tween(220)) + slideInVertically(
                    animationSpec = tween(220),
                    initialOffsetY = { 30 }
                )).togetherWith(
                    fadeOut(animationSpec = tween(150))
                )
            },
            label = "screen_navigation_animation"
        ) { screenKey ->
            when (screenKey) {
                "home" -> HomeScreen(viewModel = vm, modifier = Modifier.padding(innerPadding))
                "dining" -> DiningScreen(viewModel = vm, modifier = Modifier.padding(innerPadding))
                "shop" -> ShopScreen(viewModel = vm, modifier = Modifier.padding(innerPadding))
                "reservations" -> ReservationsScreen(viewModel = vm, modifier = Modifier.padding(innerPadding))
                "profile" -> ProfileScreen(viewModel = vm, modifier = Modifier.padding(innerPadding))
                "cart" -> CartScreen(viewModel = vm, modifier = Modifier.padding(innerPadding))
                "admin" -> AdminScreen(viewModel = vm, modifier = Modifier.padding(innerPadding))
                "details" -> DetailsScreen(viewModel = vm) // no scaffold top bar or padding required for full bleed visual weight
                else -> HomeScreen(viewModel = vm, modifier = Modifier.padding(innerPadding))
            }
        }
    }
}
