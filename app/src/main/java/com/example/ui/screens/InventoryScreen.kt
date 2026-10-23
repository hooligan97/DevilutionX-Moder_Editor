package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DiabloItem
import com.example.data.model.EquipSlot
import com.example.data.model.ItemQuality
import com.example.ui.CharacterViewModel
import com.example.ui.components.GothicCard
import com.example.ui.theme.*

@Composable
fun InventoryScreen(
    viewModel: CharacterViewModel,
    modifier: Modifier = Modifier
) {
    val hero by viewModel.character.collectAsState()
    var inspectedItem by remember { mutableStateOf<Pair<DiabloItem, EquipSlot?>?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // Equipment Paperdoll Section
        item {
            GothicCard(borderColor = DemonicGoldDark) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Equipped Gear", color = DemonicGold, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("${hero.equipment.size} / 7 Slots", color = BoneGray, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Top: Helm & Amulet
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    EquipSlotCard(
                        slot = EquipSlot.HEAD,
                        item = hero.equipment[EquipSlot.HEAD],
                        onClick = { hero.equipment[EquipSlot.HEAD]?.let { inspectedItem = Pair(it, EquipSlot.HEAD) } }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    EquipSlotCard(
                        slot = EquipSlot.NECK,
                        item = hero.equipment[EquipSlot.NECK],
                        onClick = { hero.equipment[EquipSlot.NECK]?.let { inspectedItem = Pair(it, EquipSlot.NECK) } }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Middle: Main Hand, Armor, Off Hand
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    EquipSlotCard(
                        slot = EquipSlot.LEFT_HAND,
                        item = hero.equipment[EquipSlot.LEFT_HAND],
                        onClick = { hero.equipment[EquipSlot.LEFT_HAND]?.let { inspectedItem = Pair(it, EquipSlot.LEFT_HAND) } }
                    )
                    EquipSlotCard(
                        slot = EquipSlot.TORSO,
                        item = hero.equipment[EquipSlot.TORSO],
                        onClick = { hero.equipment[EquipSlot.TORSO]?.let { inspectedItem = Pair(it, EquipSlot.TORSO) } }
                    )
                    EquipSlotCard(
                        slot = EquipSlot.RIGHT_HAND,
                        item = hero.equipment[EquipSlot.RIGHT_HAND],
                        onClick = { hero.equipment[EquipSlot.RIGHT_HAND]?.let { inspectedItem = Pair(it, EquipSlot.RIGHT_HAND) } }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Bottom: Ring 1, Ring 2
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    EquipSlotCard(
                        slot = EquipSlot.LEFT_RING,
                        item = hero.equipment[EquipSlot.LEFT_RING],
                        onClick = { hero.equipment[EquipSlot.LEFT_RING]?.let { inspectedItem = Pair(it, EquipSlot.LEFT_RING) } }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    EquipSlotCard(
                        slot = EquipSlot.RIGHT_RING,
                        item = hero.equipment[EquipSlot.RIGHT_RING],
                        onClick = { hero.equipment[EquipSlot.RIGHT_RING]?.let { inspectedItem = Pair(it, EquipSlot.RIGHT_RING) } }
                    )
                }
            }
        }

        // Belt / Speed Bar
        item {
            GothicCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Belt / Speed Bar", color = DemonicGold, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Text("8 Slots (1-8 Keys)", color = BoneGray, fontSize = 11.sp)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    for (i in 0 until 8) {
                        val item = hero.belt.getOrNull(i)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .background(DiabloSurfaceElevated, RoundedCornerShape(6.dp))
                                .clickable {
                                    if (item != null) inspectedItem = Pair(item, null)
                                }
                                .padding(2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (item != null) {
                                Text(
                                    text = item.name.take(4),
                                    fontSize = 10.sp,
                                    color = getItemQualityColor(item.quality),
                                    maxLines = 1
                                )
                            } else {
                                Text("${i + 1}", fontSize = 10.sp, color = DarkBorder)
                            }
                        }
                    }
                }
            }
        }

        // Backpack Inventory Grid
        item {
            GothicCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Backpack Inventory", color = DemonicGold, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("${hero.inventory.size} / 40 Items", color = BoneGray, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (hero.inventory.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Inventory2, contentDescription = null, tint = DarkBorder, modifier = Modifier.size(36.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Backpack is empty", color = BoneGray, fontSize = 13.sp)
                            Text("Add legendary items from the Armory tab", color = DemonicGoldDark, fontSize = 11.sp)
                        }
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        hero.inventory.forEach { item ->
                            val qColor = getItemQualityColor(item.quality)
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { inspectedItem = Pair(item, null) },
                                colors = CardDefaults.cardColors(containerColor = DiabloSurfaceElevated),
                                border = BorderStroke(1.dp, qColor.copy(alpha = 0.5f))
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = item.fullTitle,
                                            color = qColor,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )
                                        Text(
                                            text = "${item.baseType} • ${item.quality.displayName}${if (item.armorClass > 0) " • AC ${item.armorClass}" else ""}",
                                            color = BoneGray,
                                            fontSize = 11.sp
                                        )
                                    }

                                    IconButton(
                                        onClick = { viewModel.removeItemFromInventory(item.id) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = BloodRedBright)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Item Inspector Modal Dialog
    inspectedItem?.let { (item, slot) ->
        val qColor = getItemQualityColor(item.quality)
        AlertDialog(
            onDismissRequest = { inspectedItem = null },
            containerColor = DiabloSurface,
            title = {
                Text(
                    text = item.fullTitle,
                    color = qColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "${item.quality.displayName} ${item.baseType}",
                        color = BoneGray,
                        fontSize = 13.sp
                    )

                    Divider(color = DarkBorder)

                    if (item.armorClass > 0) {
                        Text("Armor Class: ${item.armorClass}", color = NormalWhite, fontSize = 13.sp)
                    }
                    if (item.minDamage > 0 || item.maxDamage > 0) {
                        Text("Damage: ${item.minDamage} - ${item.maxDamage} (+${item.damageBonus}%)", color = NormalWhite, fontSize = 13.sp)
                    }
                    if (item.toHitBonus > 0) {
                        Text("To Hit: +${item.toHitBonus}%", color = NormalWhite, fontSize = 13.sp)
                    }
                    if (item.isIndestructible) {
                        Text("Indestructible", color = DemonicGoldBright, fontSize = 13.sp)
                    } else if (item.maxDurability > 0) {
                        Text("Durability: ${item.durability} / ${item.maxDurability}", color = NormalWhite, fontSize = 13.sp)
                    }
                    if (item.maxCharges > 0) {
                        Text("Charges: ${item.charges} / ${item.maxCharges}", color = ArcaneBlueBright, fontSize = 13.sp)
                    }
                    if (item.allAttributesBonus > 0) {
                        Text("+${item.allAttributesBonus} to All Attributes", color = DemonicGoldBright, fontSize = 13.sp)
                    }
                    if (item.hitPointsBonus > 0) {
                        Text("+${item.hitPointsBonus} Hit Points", color = BloodRedBright, fontSize = 13.sp)
                    }
                    if (item.manaBonus > 0) {
                        Text("+${item.manaBonus} Mana", color = ArcaneBlueBright, fontSize = 13.sp)
                    }
                    if (item.fireResist > 0 || item.lightningResist > 0 || item.magicResist > 0) {
                        Text("Resistances: Fire +${item.fireResist}%, Light +${item.lightningResist}%, Magic +${item.magicResist}%", color = NormalWhite, fontSize = 13.sp)
                    }
                    if (item.extraDescription.isNotBlank()) {
                        Text(item.extraDescription, color = DemonicGold, fontSize = 12.sp, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                    }
                }
            },
            confirmButton = {
                if (slot != null) {
                    FilledTonalButton(
                        onClick = {
                            viewModel.unequipItem(slot)
                            inspectedItem = null
                        },
                        colors = ButtonDefaults.filledTonalButtonColors(containerColor = BloodRedDark)
                    ) {
                        Text("Unequip", color = Color.White)
                    }
                } else {
                    FilledTonalButton(
                        onClick = {
                            viewModel.removeItemFromInventory(item.id)
                            inspectedItem = null
                        },
                        colors = ButtonDefaults.filledTonalButtonColors(containerColor = BloodRedDark)
                    ) {
                        Text("Drop Item", color = Color.White)
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { inspectedItem = null }) {
                    Text("Close", color = BoneGray)
                }
            }
        )
    }
}

@Composable
private fun EquipSlotCard(
    slot: EquipSlot,
    item: DiabloItem?,
    onClick: () -> Unit
) {
    val qColor = item?.let { getItemQualityColor(it.quality) } ?: DarkBorder

    Card(
        modifier = Modifier
            .width(100.dp)
            .height(78.dp)
            .clickable(enabled = item != null, onClick = onClick)
            .testTag("equip_slot_${slot.name.lowercase()}"),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = DiabloSurfaceElevated),
        border = BorderStroke(1.dp, if (item != null) qColor else DarkBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = slot.shortLabel,
                color = BoneGray,
                fontSize = 10.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (item != null) {
                Text(
                    text = item.fullTitle,
                    color = qColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            } else {
                Text(
                    text = "<Empty>",
                    color = DarkBorder,
                    fontSize = 10.sp
                )
            }
        }
    }
}

private fun getItemQualityColor(quality: ItemQuality): Color {
    return when (quality) {
        ItemQuality.UNIQUE -> UniqueGold
        ItemQuality.MAGIC -> MagicBlue
        ItemQuality.NORMAL -> NormalWhite
    }
}
