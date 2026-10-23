package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.ui.CharacterViewModel
import com.example.ui.components.DiabloOrb
import com.example.ui.components.GothicCard
import com.example.ui.theme.*

@Composable
fun StatsScreen(
    viewModel: CharacterViewModel,
    modifier: Modifier = Modifier
) {
    val hero by viewModel.character.collectAsState()

    // Calculate effective bonuses from equipment
    val equipItems = hero.equipment.values
    val totalAc = equipItems.sumOf { it.armorClass } + (hero.dexterity / 5)
    val totalFireRes = minOf(75, equipItems.sumOf { it.fireResist })
    val totalLightRes = minOf(75, equipItems.sumOf { it.lightningResist })
    val totalMagicRes = minOf(75, equipItems.sumOf { it.magicResist })
    val toHit = 50 + (hero.dexterity / 2) + equipItems.sumOf { it.toHitBonus }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // Health & Mana Globes
        item {
            GothicCard(borderColor = DarkBorder) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DiabloOrb(
                        title = "LIFE (HP)",
                        currentValue = hero.life,
                        maxValue = hero.maxLife,
                        isLife = true,
                        onClick = { viewModel.fullHeal() }
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        OutlinedButton(
                            onClick = { viewModel.fullHeal() },
                            border = BorderStroke(1.dp, DemonicGoldDark)
                        ) {
                            Icon(Icons.Default.Healing, contentDescription = null, tint = BloodRedBright, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Full Heal", fontSize = 12.sp, color = DemonicGoldBright)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        FilterChip(
                            selected = hero.manaShieldActive,
                            onClick = { viewModel.toggleManaShield() },
                            label = { Text("Mana Shield", fontSize = 11.sp) },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = if (hero.manaShieldActive) ArcaneBlueBright else BoneGray,
                                    modifier = Modifier.size(14.dp)
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ArcaneBlueDark,
                                selectedLabelColor = Color.White
                            )
                        )
                    }

                    DiabloOrb(
                        title = "MANA",
                        currentValue = hero.mana,
                        maxValue = hero.maxMana,
                        isLife = false,
                        onClick = { viewModel.fullHeal() }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // HP/Mana Max sliders
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Max Life: ${hero.maxLife}", color = BloodRedBright, fontSize = 12.sp)
                        Slider(
                            value = hero.maxLife.toFloat(),
                            onValueChange = {
                                val m = it.toInt()
                                viewModel.updateLife(hero.life.coerceAtMost(m), m)
                            },
                            valueRange = 20f..1200f,
                            colors = SliderDefaults.colors(thumbColor = BloodRedBright, activeTrackColor = BloodRed)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text("Max Mana: ${hero.maxMana}", color = ArcaneBlueBright, fontSize = 12.sp)
                        Slider(
                            value = hero.maxMana.toFloat(),
                            onValueChange = {
                                val m = it.toInt()
                                viewModel.updateMana(hero.mana.coerceAtMost(m), m)
                            },
                            valueRange = 0f..1200f,
                            colors = SliderDefaults.colors(thumbColor = ArcaneBlueBright, activeTrackColor = ArcaneBlue)
                        )
                    }
                }
            }
        }

        // Core Attributes
        item {
            GothicCard(borderColor = DemonicGoldDark) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Core Attributes", color = DemonicGold, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("Points: ${hero.statPoints}", color = DemonicGoldBright, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                AttributeRow(
                    name = "Strength",
                    value = hero.strength,
                    maxCap = hero.heroClass.maxStrength,
                    icon = Icons.Default.FitnessCenter,
                    color = DemonicGold,
                    onAdjust = { delta -> viewModel.modifyStat("strength", delta) }
                )

                Divider(color = DarkBorder, modifier = Modifier.padding(vertical = 10.dp))

                AttributeRow(
                    name = "Magic",
                    value = hero.magic,
                    maxCap = hero.heroClass.maxMagic,
                    icon = Icons.Default.AutoFixHigh,
                    color = ArcaneBlueBright,
                    onAdjust = { delta -> viewModel.modifyStat("magic", delta) }
                )

                Divider(color = DarkBorder, modifier = Modifier.padding(vertical = 10.dp))

                AttributeRow(
                    name = "Dexterity",
                    value = hero.dexterity,
                    maxCap = hero.heroClass.maxDexterity,
                    icon = Icons.Default.Speed,
                    color = Color(0xFF67DE6E),
                    onAdjust = { delta -> viewModel.modifyStat("dexterity", delta) }
                )

                Divider(color = DarkBorder, modifier = Modifier.padding(vertical = 10.dp))

                AttributeRow(
                    name = "Vitality",
                    value = hero.vitality,
                    maxCap = hero.heroClass.maxVitality,
                    icon = Icons.Default.Favorite,
                    color = BloodRedBright,
                    onAdjust = { delta -> viewModel.modifyStat("vitality", delta) }
                )
            }
        }

        // Combat Calculations & Resistances
        item {
            GothicCard {
                Text("Combat Ratings & Resistances", color = DemonicGold, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CombatStatItem("Armor Class", "$totalAc AC", Icons.Default.Shield)
                    CombatStatItem("To-Hit Chance", "$toHit%", Icons.Default.GpsFixed)
                    val blockChance = if (hero.equipment.containsKey(EquipSlot.RIGHT_HAND)) minOf(75, 10 + hero.dexterity / 4) else 0
                    CombatStatItem("Block Chance", "$blockChance%", Icons.Default.Security)
                }

                Spacer(modifier = Modifier.height(14.dp))
                Divider(color = DarkBorder)
                Spacer(modifier = Modifier.height(14.dp))

                Text("Elemental Resistances (Max 75%)", color = BoneGray, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ResistBadge("Fire", totalFireRes, BloodRedBright, Modifier.weight(1f))
                    ResistBadge("Lightning", totalLightRes, DemonicGoldBright, Modifier.weight(1f))
                    ResistBadge("Magic", totalMagicRes, ArcaneBlueBright, Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun AttributeRow(
    name: String,
    value: Int,
    maxCap: Int,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onAdjust: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = name, color = NormalWhite, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            val capColor = if (value >= maxCap) DemonicGoldBright else BoneGray
            Text(text = "Cap: $maxCap (${if (value >= maxCap) "MAX" else "${maxCap - value} to max"})", color = capColor, fontSize = 11.sp)
        }

        Text(
            text = "$value",
            color = if (value >= maxCap) DemonicGoldBright else NormalWhite,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            SmallAdjustButton("-5") { onAdjust(-5) }
            SmallAdjustButton("-1") { onAdjust(-1) }
            SmallAdjustButton("+1") { onAdjust(+1) }
            SmallAdjustButton("+5") { onAdjust(+5) }
        }
    }
}

@Composable
private fun SmallAdjustButton(label: String, onClick: () -> Unit) {
    FilledTonalButton(
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
        colors = ButtonDefaults.filledTonalButtonColors(containerColor = DiabloSurfaceElevated),
        modifier = Modifier
            .defaultMinSize(minWidth = 36.dp, minHeight = 32.dp)
            .testTag("adjust_$label")
    ) {
        Text(label, fontSize = 11.sp, color = NormalWhite)
    }
}

@Composable
private fun CombatStatItem(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = DemonicGold, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, color = NormalWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text(label, color = BoneGray, fontSize = 11.sp)
    }
}

@Composable
private fun ResistBadge(label: String, value: Int, tint: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = DiabloSurfaceElevated),
        border = BorderStroke(1.dp, if (value >= 75) tint else DarkBorder)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "$value%", color = tint, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(text = label, color = BoneGray, fontSize = 10.sp)
        }
    }
}
