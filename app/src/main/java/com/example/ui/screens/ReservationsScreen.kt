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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.VelouraViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationsScreen(
    viewModel: VelouraViewModel,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsState()

    var name by remember { mutableStateOf(userProfile.name) }
    var email by remember { mutableStateOf(userProfile.email) }
    var phone by remember { mutableStateOf(userProfile.phone) }

    var selectedBranch by remember { mutableStateOf("Mumbai Rooftop Sky Lounge") }
    var guestsCount by remember { mutableStateOf(2) }
    var selectedSection by remember { mutableStateOf("VIP Poolside Deck") }
    var selectedDate by remember { mutableStateOf("May 25, 2026") }
    var selectedTime by remember { mutableStateOf("8:30 PM (Candlelight Show)") }
    var usePointsCheckbox by remember { mutableStateOf(false) }

    val branches = listOf("Mumbai Rooftop Sky Lounge", "Delhi Imperial Botanical Dome", "Bengaluru Glass House Suite")
    val sections = listOf("VIP Poolside Deck", "Royal Brass Lounge", "Premium Candlelit Room")
    val dates = listOf("May 23, 2026", "May 24, 2026", "May 25, 2026", "May 26, 2026")
    val times = listOf("7:00 PM (Sunset View)", "8:30 PM (Candlelight Show)", "10:00 PM (Late High Society)")

    var branchDropdownExpanded by remember { mutableStateOf(false) }
    var sectionDropdownExpanded by remember { mutableStateOf(false) }
    var dateDropdownExpanded by remember { mutableStateOf(false) }
    var timeDropdownExpanded by remember { mutableStateOf(false) }

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
            
            // COVER HERO/BANNER
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                SmoothImage(
                    url = "https://images.unsplash.com/photo-1544025162-d76694265947?w=600&auto=format&fit=crop",
                    contentDescription = "VIP Reservation table",
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Black.copy(0.4f), VelouraBlack)
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        text = "VIP LOUNGE BOOKINGS",
                        color = VelouraGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "Reserve Elite Seatings",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // PRIMARY FORM CARD
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                GlassmorphicCard(
                    modifier = Modifier.fillMaxWidth(),
                    cornerRadius = 24.dp
                ) {
                    Text(
                        text = "1. Customer Information",
                        color = VelouraGold,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Contact Name
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Enter Guest Name", color = TextMuted) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = VelouraGold,
                            unfocusedBorderColor = SoftGlassBorder,
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("reserve_name_input")
                            .padding(bottom = 10.dp),
                        singleLine = true
                    )

                    // Contact Email
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Contact Email Address", color = TextMuted) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = VelouraGold,
                            unfocusedBorderColor = SoftGlassBorder,
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        singleLine = true
                    )

                    // Contact Phone
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("VIP Hotline Phone Number", color = TextMuted) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = VelouraGold,
                            unfocusedBorderColor = SoftGlassBorder,
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("reserve_phone_input")
                            .padding(bottom = 16.dp),
                        singleLine = true
                    )

                    Text(
                        text = "2. Lounge Setup & Timing",
                        color = VelouraGold,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Branch select Dropdown
                    ExposedDropdownMenuBox(
                        expanded = branchDropdownExpanded,
                        onExpandedChange = { branchDropdownExpanded = !branchDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedBranch,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Select Club Branch", color = TextMuted) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = branchDropdownExpanded) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = VelouraGold,
                                unfocusedBorderColor = SoftGlassBorder,
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                                .padding(bottom = 10.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = branchDropdownExpanded,
                            onDismissRequest = { branchDropdownExpanded = false },
                            modifier = Modifier.background(VelouraCharcoal)
                        ) {
                            branches.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(item, color = Color.White) },
                                    onClick = {
                                        selectedBranch = item
                                        branchDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Guests Count selector
                    Text(
                        text = "Number of Guests: $guestsCount Persons",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Slider(
                        value = guestsCount.toFloat(),
                        onValueChange = { guestsCount = it.toInt() },
                        valueRange = 1f..12f,
                        steps = 10,
                        colors = SliderDefaults.colors(
                            thumbColor = VelouraGold,
                            activeTrackColor = VelouraGold,
                            inactiveTrackColor = SoftGlassBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                    )

                    // Lounge Section select Dropdown
                    ExposedDropdownMenuBox(
                        expanded = sectionDropdownExpanded,
                        onExpandedChange = { sectionDropdownExpanded = !sectionDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedSection,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Preferential Floor Section", color = TextMuted) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = sectionDropdownExpanded) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = VelouraGold,
                                unfocusedBorderColor = SoftGlassBorder,
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                                .padding(bottom = 10.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = sectionDropdownExpanded,
                            onDismissRequest = { sectionDropdownExpanded = false },
                            modifier = Modifier.background(VelouraCharcoal)
                        ) {
                            sections.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(item, color = Color.White) },
                                    onClick = {
                                        selectedSection = item
                                        sectionDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Choose Date Dropdown
                    ExposedDropdownMenuBox(
                        expanded = dateDropdownExpanded,
                        onExpandedChange = { dateDropdownExpanded = !dateDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedDate,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Select Seating Date", color = TextMuted) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dateDropdownExpanded) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = VelouraGold,
                                unfocusedBorderColor = SoftGlassBorder,
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                                .padding(bottom = 10.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = dateDropdownExpanded,
                            onDismissRequest = { dateDropdownExpanded = false },
                            modifier = Modifier.background(VelouraCharcoal)
                        ) {
                            dates.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(item, color = Color.White) },
                                    onClick = {
                                        selectedDate = item
                                        dateDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Choose Time Slot Dropdown
                    ExposedDropdownMenuBox(
                        expanded = timeDropdownExpanded,
                        onExpandedChange = { timeDropdownExpanded = !timeDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedTime,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Select Seating Time Slot", color = TextMuted) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = timeDropdownExpanded) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = VelouraGold,
                                unfocusedBorderColor = SoftGlassBorder,
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                                .padding(bottom = 16.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = timeDropdownExpanded,
                            onDismissRequest = { timeDropdownExpanded = false },
                            modifier = Modifier.background(VelouraCharcoal)
                        ) {
                            times.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(item, color = Color.White) },
                                    onClick = {
                                        selectedTime = item
                                        timeDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Rewards Redemption Checkbox
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = usePointsCheckbox,
                            onCheckedChange = { usePointsCheckbox = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = VelouraGold,
                                uncheckedColor = SoftGlassBorder,
                                checkmarkColor = Color.Black
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = "Apply Elite Club Rewards points?",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Use 100 reward points to secure a complimentary gold-dusted welcome dessert (Current: ${userProfile.rewardPoints} pts).",
                                color = TextMuted,
                                fontSize = 11.sp,
                                lineHeight = 15.sp
                            )
                        }
                    }

                    // Submission
                    GlowingGoldButton(
                        onClick = {
                            val ptsToUse = if (usePointsCheckbox && userProfile.rewardPoints >= 100) 100 else 0
                            viewModel.bookTable(
                                name = name,
                                email = email,
                                phone = phone,
                                date = selectedDate,
                                time = selectedTime,
                                guests = guestsCount,
                                section = selectedSection,
                                branch = selectedBranch,
                                pointsToUse = ptsToUse
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("submit_booking_button")
                    ) {
                        Icon(Icons.Default.RestaurantMenu, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Submit Premium Reservation Ticket",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // AMENITY GUIDELINES CALLOUT
            Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
                Text(
                    text = "RESERVATION ETIQUETTES",
                    color = VelouraGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(bottom = 6.dp)) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = VelouraOrange, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Grace Period: We preserve reserved spots for 15 minutes past target slot before offering them to queue members.",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
                Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(bottom = 6.dp)) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = VelouraOrange, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Elite Seating Perks: Any table reserved automatically unlocks priority ordering access to limited handcrafted accessory display cases.",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(112.dp))
        }
    }
}
