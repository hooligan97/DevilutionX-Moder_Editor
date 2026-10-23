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
import com.example.data.model.DiabloSpell
import com.example.data.model.DiabloSpellsCatalog
import com.example.data.model.SpellSchool
import com.example.ui.CharacterViewModel
import com.example.ui.components.GothicCard
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpellbookScreen(
    viewModel: CharacterViewModel,
    modifier: Modifier = Modifier
) {
    val hero by viewModel.character.collectAsState()
    var selectedSchool by remember { mutableStateOf<SpellSchool?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredSpells = remember(selectedSchool, searchQuery) {
        DiabloSpellsCatalog.ALL_SPELLS.filter { spell ->
            (selectedSchool == null || spell.school == selectedSchool) &&
            (searchQuery.isBlank() || spell.name.contains(searchQuery, ignoreCase = true) || spell.description.contains(searchQuery, ignoreCase = true))
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // Spellbook Header & Actions
        item {
            GothicCard(borderColor = ArcaneBlueDark) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.MenuBook, contentDescription = null, tint = ArcaneBlueBright)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Diablo Spellbook", color = ArcaneBlueBright, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }

                    FilledTonalButton(
                        onClick = { viewModel.maxAllSpells() },
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = DiabloSurfaceElevated,
                            contentColor = ArcaneBlueBright
                        ),
                        modifier = Modifier.testTag("max_all_spells_btn")
                    ) {
                        Icon(Icons.Default.AutoFixHigh, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Max All (15)", fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search spells (e.g. Apocalypse, Firewall, Teleport)") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = BoneGray) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear")
                            }
                        }
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ArcaneBlue,
                        unfocusedBorderColor = DarkBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // School Filter Chips
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        FilterChip(
                            selected = selectedSchool == null,
                            onClick = { selectedSchool = null },
                            label = { Text("All (${DiabloSpellsCatalog.ALL_SPELLS.size})") },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = ArcaneBlueDark)
                        )
                    }
                    items(SpellSchool.entries.toTypedArray()) { school ->
                        val count = DiabloSpellsCatalog.ALL_SPELLS.count { it.school == school }
                        FilterChip(
                            selected = selectedSchool == school,
                            onClick = { selectedSchool = if (selectedSchool == school) null else school },
                            label = { Text("${school.displayName} ($count)") },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = ArcaneBlueDark)
                        )
                    }
                }
            }
        }

        // Spells List
        items(filteredSpells, key = { it.id }) { spell ->
            val currentLevel = hero.spellLevels[spell.id] ?: 0
            val schoolColor = when (spell.school) {
                SpellSchool.FIRE -> BloodRedBright
                SpellSchool.LIGHTNING -> DemonicGoldBright
                SpellSchool.ARCANE -> ArcaneBlueBright
                SpellSchool.FORBIDDEN -> Color(0xFFC062FF)
                SpellSchool.HELLFIRE -> Color(0xFFFF8E3D)
            }

            GothicCard(
                borderColor = if (currentLevel > 0) schoolColor.copy(alpha = 0.5f) else DarkBorder
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = spell.name,
                                color = if (currentLevel > 0) schoolColor else NormalWhite,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Mana: ${spell.manaCost}",
                                color = BoneGray,
                                fontSize = 11.sp
                            )
                        }
                        Text(
                            text = spell.description,
                            color = BoneGray,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    Badge(
                        containerColor = if (currentLevel > 0) DiabloSurfaceElevated else DarkBorder,
                        contentColor = if (currentLevel == 15) DemonicGoldBright else NormalWhite
                    ) {
                        Text(
                            text = if (currentLevel > 0) "Lv $currentLevel" else "Not Learned",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Level Slider and Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Slider(
                        value = currentLevel.toFloat(),
                        onValueChange = { viewModel.setSpellLevel(spell.id, it.toInt()) },
                        valueRange = 0f..15f,
                        steps = 14,
                        colors = SliderDefaults.colors(
                            thumbColor = schoolColor,
                            activeTrackColor = schoolColor
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("spell_slider_${spell.id}")
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        FilledTonalButton(
                            onClick = { viewModel.setSpellLevel(spell.id, (currentLevel - 1).coerceAtLeast(0)) },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            modifier = Modifier.defaultMinSize(minWidth = 32.dp, minHeight = 32.dp)
                        ) {
                            Text("-", fontSize = 12.sp)
                        }

                        FilledTonalButton(
                            onClick = { viewModel.setSpellLevel(spell.id, (currentLevel + 1).coerceAtMost(15)) },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            modifier = Modifier.defaultMinSize(minWidth = 32.dp, minHeight = 32.dp)
                        ) {
                            Text("+", fontSize = 12.sp)
                        }

                        FilledTonalButton(
                            onClick = { viewModel.setSpellLevel(spell.id, 15) },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = DiabloSurfaceElevated,
                                contentColor = DemonicGoldBright
                            ),
                            modifier = Modifier.defaultMinSize(minWidth = 40.dp, minHeight = 32.dp)
                        ) {
                            Text("15", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
