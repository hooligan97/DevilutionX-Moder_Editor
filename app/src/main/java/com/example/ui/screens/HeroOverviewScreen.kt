package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.HeroCharacter
import com.example.data.model.HeroClass
import com.example.ui.CharacterViewModel
import com.example.ui.components.GothicCard
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeroOverviewScreen(
    viewModel: CharacterViewModel,
    modifier: Modifier = Modifier
) {
    val hero by viewModel.character.collectAsState()
    var nameText by remember(hero.name) { mutableStateOf(hero.name) }
    var goldText by remember(hero.gold) { mutableStateOf(hero.gold.toString()) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // Hero Header Card
        item {
            GothicCard(
                borderColor = DemonicGold,
                borderWidth = 1.5.dp,
                backgroundColor = DiabloSurfaceVariant
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Class Crest Icon
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .background(DiabloSurfaceElevated, RoundedCornerShape(10.dp))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (hero.heroClass) {
                                HeroClass.WARRIOR -> Icons.Default.Shield
                                HeroClass.ROGUE -> Icons.Default.Adjust
                                HeroClass.SORCERER -> Icons.Default.AutoFixHigh
                                HeroClass.MONK -> Icons.Default.FitnessCenter
                                HeroClass.BARD -> Icons.Default.MusicNote
                                HeroClass.BARBARIAN -> Icons.Default.Handyman
                            },
                            contentDescription = hero.heroClass.displayName,
                            tint = DemonicGoldBright,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = hero.name,
                            color = DemonicGoldBright,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Level ${hero.level} ${hero.heroClass.displayName} • ${hero.heroClass.title}",
                            color = BoneGray,
                            fontSize = 13.sp
                        )
                    }

                    Badge(
                        containerColor = if (hero.difficulty == "Hell") BloodRedDark else DarkBorder,
                        contentColor = Color.White
                    ) {
                        Text(
                            text = hero.difficulty.uppercase(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }

        // Quick Cheats & Boosts
        item {
            GothicCard(borderColor = BloodRedDark) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Bolt, contentDescription = null, tint = BloodRedBright)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "DevilutionX Quick Boosts",
                        color = BloodRedBright,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilledTonalButton(
                        onClick = { viewModel.maxOutAllStats() },
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = DiabloSurfaceElevated,
                            contentColor = DemonicGoldBright
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("max_stats_button")
                    ) {
                        Text("Max Stats", fontSize = 12.sp)
                    }

                    FilledTonalButton(
                        onClick = { viewModel.maxAllSpells() },
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = DiabloSurfaceElevated,
                            contentColor = ArcaneBlueBright
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("max_spells_button")
                    ) {
                        Text("All Spells 15", fontSize = 12.sp)
                    }

                    FilledTonalButton(
                        onClick = { viewModel.maxGold() },
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = DiabloSurfaceElevated,
                            contentColor = DemonicGold
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("max_gold_button")
                    ) {
                        Text("999k Gold", fontSize = 12.sp)
                    }
                }
            }
        }

        // Identity Editor
        item {
            GothicCard {
                Text(
                    text = "Hero Identity",
                    color = DemonicGold,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = nameText,
                    onValueChange = {
                        if (it.length <= 15) {
                            nameText = it
                            viewModel.updateName(it)
                        }
                    },
                    label = { Text("Character Name (max 15 chars)") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DemonicGold,
                        unfocusedBorderColor = DarkBorder,
                        focusedLabelColor = DemonicGold
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("hero_name_input")
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text("Hero Class", color = BoneGray, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(6.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(HeroClass.entries.toTypedArray()) { cls ->
                        val selected = hero.heroClass == cls
                        FilterChip(
                            selected = selected,
                            onClick = { viewModel.updateHeroClass(cls) },
                            label = { Text(cls.displayName) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BloodRedDark,
                                selectedLabelColor = Color.White
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = selected,
                                selectedBorderColor = BloodRedBright,
                                borderColor = DarkBorder
                            )
                        )
                    }
                }
            }
        }

        // Level & Experience Progression
        item {
            GothicCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Level & Experience", color = DemonicGold, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("Level ${hero.level} / 50", color = DemonicGoldBright, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Slider(
                    value = hero.level.toFloat(),
                    onValueChange = { viewModel.updateLevel(it.toInt()) },
                    valueRange = 1f..50f,
                    steps = 48,
                    colors = SliderDefaults.colors(
                        thumbColor = DemonicGoldBright,
                        activeTrackColor = DemonicGold,
                        inactiveTrackColor = DarkBorder
                    ),
                    modifier = Modifier.testTag("level_slider")
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Current XP: ${hero.experience}", color = BoneGray, fontSize = 12.sp)
                    val nextXp = if (hero.level < 50) HeroCharacter.xpForLevel(hero.level + 1) else hero.experience
                    Text("Next Lvl: $nextXp", color = DemonicGoldDark, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                val progress = if (hero.level < 50) {
                    val currentBase = HeroCharacter.xpForLevel(hero.level)
                    val nextBase = HeroCharacter.xpForLevel(hero.level + 1)
                    val diff = nextBase - currentBase
                    if (diff > 0) ((hero.experience - currentBase).toFloat() / diff).coerceIn(0f, 1f) else 1f
                } else 1f

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp),
                    color = DemonicGold,
                    trackColor = DarkBorder
                )
            }
        }

        // Gold & Economy
        item {
            GothicCard {
                Text("Gold & Treasure", color = DemonicGold, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = goldText,
                    onValueChange = { input ->
                        val numeric = input.filter { it.isDigit() }
                        goldText = numeric
                        val g = numeric.toLongOrNull() ?: 0L
                        viewModel.updateGold(g)
                    },
                    label = { Text("Gold in Purse & Bank (max 999,999)") },
                    leadingIcon = {
                        Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = DemonicGoldBright)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DemonicGold,
                        unfocusedBorderColor = DarkBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("gold_input")
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = {
                            val newG = (hero.gold + 10_000L).coerceAtMost(999_999L)
                            goldText = newG.toString()
                            viewModel.updateGold(newG)
                        },
                        border = BorderStroke(1.dp, DarkBorder)
                    ) {
                        Text("+10k Gold", fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = {
                            val newG = (hero.gold + 50_000L).coerceAtMost(999_999L)
                            goldText = newG.toString()
                            viewModel.updateGold(newG)
                        },
                        border = BorderStroke(1.dp, DarkBorder)
                    ) {
                        Text("+50k Gold", fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = {
                            goldText = "999999"
                            viewModel.updateGold(999_999L)
                        },
                        border = BorderStroke(1.dp, DemonicGoldDark)
                    ) {
                        Text("Max (999k)", color = DemonicGoldBright, fontSize = 12.sp)
                    }
                }
            }
        }

        // World & Quest Progression
        item {
            GothicCard {
                Text("Dungeon Location & Difficulty", color = DemonicGold, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(10.dp))

                Text("Difficulty", color = BoneGray, fontSize = 13.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 4.dp)) {
                    listOf("Normal", "Nightmare", "Hell").forEach { diff ->
                        FilterChip(
                            selected = hero.difficulty == diff,
                            onClick = { viewModel.updateDifficulty(diff) },
                            label = { Text(diff) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BloodRedDark,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                val floorName = when (hero.dungeonLevel) {
                    0 -> "Town of Tristram"
                    in 1..4 -> "Cathedral (Level ${hero.dungeonLevel})"
                    in 5..8 -> "Catacombs (Level ${hero.dungeonLevel})"
                    in 9..12 -> "Caves (Level ${hero.dungeonLevel})"
                    in 13..16 -> "Hell (Level ${hero.dungeonLevel})"
                    in 17..20 -> "Crypt (Hellfire Level ${hero.dungeonLevel - 16})"
                    else -> "Hive (Hellfire Level ${hero.dungeonLevel - 20})"
                }

                Text("Dungeon Floor: $floorName", color = NormalWhite, fontSize = 14.sp)
                Slider(
                    value = hero.dungeonLevel.toFloat(),
                    onValueChange = { viewModel.updateDungeonLevel(it.toInt()) },
                    valueRange = 0f..24f,
                    steps = 23,
                    colors = SliderDefaults.colors(
                        thumbColor = BloodRedBright,
                        activeTrackColor = BloodRed
                    )
                )
            }
        }
    }
}
