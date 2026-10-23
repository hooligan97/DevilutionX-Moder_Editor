package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EquipSlot
import com.example.data.model.ItemArmory
import com.example.data.model.ItemQuality
import com.example.ui.CharacterViewModel
import com.example.ui.components.GothicCard
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArmoryScreen(
    viewModel: CharacterViewModel,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }

    val categories = listOf("All", "Weapons", "Armor", "Helms", "Shields", "Jewelry", "Consumables")

    val filteredItems = remember(selectedCategory, searchQuery) {
        ItemArmory.LEGENDARY_CATALOG.filter { item ->
            val matchCat = when (selectedCategory) {
                "Weapons" -> item.baseType.contains("Sword", true) || item.baseType.contains("Bow", true) || item.baseType.contains("Staff", true)
                "Armor" -> item.baseType.contains("Mail", true) || item.baseType.contains("Plate", true)
                "Helms" -> item.baseType.contains("Helm", true) || item.baseType.contains("Cap", true) || item.baseType.contains("Crown", true)
                "Shields" -> item.baseType.contains("Shield", true)
                "Jewelry" -> item.baseType.contains("Ring", true) || item.baseType.contains("Amulet", true)
                "Consumables" -> item.baseType.contains("Potion", true) || item.baseType.contains("Scroll", true)
                else -> true
            }
            val matchSearch = searchQuery.isBlank() ||
                item.name.contains(searchQuery, true) ||
                item.fullTitle.contains(searchQuery, true) ||
                item.extraDescription.contains(searchQuery, true)

            matchCat && matchSearch
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // Armory Header
        item {
            GothicCard(borderColor = DemonicGold) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Shield, contentDescription = null, tint = DemonicGoldBright)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Tristram Armory", color = DemonicGoldBright, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Equip iconic Diablo 1 uniques & god-tier items directly onto your hero with 1 click.",
                    color = BoneGray,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search catalog (e.g. Grandfather, Whale, Windforce)") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = BoneGray) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DemonicGold,
                        unfocusedBorderColor = DarkBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(categories) { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat) },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = DemonicGoldDark)
                        )
                    }
                }
            }
        }

        // Items List
        items(filteredItems, key = { it.name + it.baseType }) { item ->
            val qColor = when (item.quality) {
                ItemQuality.UNIQUE -> UniqueGold
                ItemQuality.MAGIC -> MagicBlue
                ItemQuality.NORMAL -> NormalWhite
            }

            GothicCard(borderColor = qColor.copy(alpha = 0.5f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.fullTitle,
                            color = qColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${item.quality.displayName} • ${item.baseType}",
                            color = BoneGray,
                            fontSize = 12.sp
                        )
                    }

                    Badge(containerColor = DiabloSurfaceElevated, contentColor = qColor) {
                        Text(item.quality.displayName, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Stats breakdown
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    if (item.armorClass > 0) {
                        Text("Armor Class: ${item.armorClass}", color = NormalWhite, fontSize = 12.sp)
                    }
                    if (item.minDamage > 0 || item.maxDamage > 0) {
                        Text("Damage: ${item.minDamage} - ${item.maxDamage} (+${item.damageBonus}%)", color = NormalWhite, fontSize = 12.sp)
                    }
                    if (item.allAttributesBonus > 0) {
                        Text("+${item.allAttributesBonus} to All Attributes", color = DemonicGoldBright, fontSize = 12.sp)
                    }
                    if (item.fireResist > 0 || item.lightningResist > 0 || item.magicResist > 0) {
                        Text("+${item.fireResist}% Fire / +${item.lightningResist}% Lightning / +${item.magicResist}% Magic Resist", color = ArcaneBlueBright, fontSize = 12.sp)
                    }
                    if (item.extraDescription.isNotBlank()) {
                        Text(item.extraDescription, color = DemonicGold, fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val targetSlot = when {
                        item.baseType.contains("Helm", true) || item.baseType.contains("Cap", true) || item.baseType.contains("Crown", true) -> EquipSlot.HEAD
                        item.baseType.contains("Mail", true) || item.baseType.contains("Plate", true) -> EquipSlot.TORSO
                        item.baseType.contains("Sword", true) || item.baseType.contains("Bow", true) || item.baseType.contains("Staff", true) -> EquipSlot.LEFT_HAND
                        item.baseType.contains("Shield", true) -> EquipSlot.RIGHT_HAND
                        item.baseType.contains("Amulet", true) -> EquipSlot.NECK
                        item.baseType.contains("Ring", true) -> EquipSlot.LEFT_RING
                        else -> null
                    }

                    if (targetSlot != null) {
                        FilledTonalButton(
                            onClick = { viewModel.equipItem(targetSlot, item) },
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = DiabloSurfaceElevated,
                                contentColor = DemonicGoldBright
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("equip_${item.name.replace(" ", "_").lowercase()}")
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Equip (${targetSlot.shortLabel})", fontSize = 11.sp)
                        }
                    }

                    OutlinedButton(
                        onClick = { viewModel.addItemToInventory(item) },
                        border = BorderStroke(1.dp, DarkBorder),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("add_backpack_${item.name.replace(" ", "_").lowercase()}")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("To Backpack", fontSize = 11.sp, color = NormalWhite)
                    }
                }
            }
        }
    }
}
