package com.example.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevilutionXApp(
    viewModel: CharacterViewModel = viewModel()
) {
    val context = LocalContext.current
    val hero by viewModel.character.collectAsState()
    val activeTab by viewModel.activeTab.collectAsState()
    val statusMessage by viewModel.statusMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(statusMessage) {
        statusMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg, duration = SnackbarDuration.Short)
            viewModel.clearStatusMessage()
        }
    }

    // BackHandler to navigate back to HERO tab if on a secondary tab
    BackHandler(enabled = activeTab != EditorTab.HERO) {
        viewModel.selectTab(EditorTab.HERO)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(DiabloBackground),
        containerColor = DiabloBackground,
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "DevilutionX Hero Editor",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = DemonicGoldBright
                        )
                        Text(
                            text = "${hero.name} • Lv ${hero.level} ${hero.heroClass.displayName} • ${hero.gold}g",
                            fontSize = 12.sp,
                            color = BoneGray
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.fullHeal() },
                        modifier = Modifier.testTag("top_bar_heal")
                    ) {
                        Icon(Icons.Default.Healing, contentDescription = "Full Rejuvenate", tint = BloodRedBright)
                    }
                    IconButton(
                        onClick = {
                            val clip = ClipData.newPlainText("Noctonic JSON", viewModel.getNoctonicJson())
                            val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            cm.setPrimaryClip(clip)
                            viewModel.showMessage("Noctonic JSON copied to clipboard!")
                        },
                        modifier = Modifier.testTag("top_bar_copy_json")
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "Copy JSON", tint = DemonicGoldBright)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DiabloSurface,
                    titleContentColor = DemonicGoldBright
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = DiabloSurface,
                contentColor = DemonicGold,
                windowInsets = WindowInsets.navigationBars,
                modifier = Modifier.testTag("bottom_nav_bar")
            ) {
                EditorTab.entries.forEach { tab ->
                    val isSelected = activeTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.selectTab(tab) },
                        icon = {
                            Icon(
                                imageVector = when (tab) {
                                    EditorTab.HERO -> Icons.Default.Person
                                    EditorTab.STATS -> Icons.Default.FitnessCenter
                                    EditorTab.SPELLS -> Icons.Default.MenuBook
                                    EditorTab.INVENTORY -> Icons.Default.Inventory2
                                    EditorTab.ARMORY -> Icons.Default.Shield
                                    EditorTab.SAVES -> Icons.Default.FolderSpecial
                                },
                                contentDescription = tab.title,
                                tint = if (isSelected) DemonicGoldBright else BoneGray
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) DemonicGoldBright else BoneGray
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DemonicGoldBright,
                            selectedTextColor = DemonicGoldBright,
                            unselectedIconColor = BoneGray,
                            unselectedTextColor = BoneGray,
                            indicatorColor = BloodRedDark
                        ),
                        modifier = Modifier.testTag("nav_item_${tab.name.lowercase()}")
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    containerColor = DiabloSurfaceElevated,
                    contentColor = NormalWhite,
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(data.visuals.message, fontSize = 13.sp)
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Crossfade(targetState = activeTab, label = "TabSwitch") { tab ->
                when (tab) {
                    EditorTab.HERO -> HeroOverviewScreen(viewModel = viewModel)
                    EditorTab.STATS -> StatsScreen(viewModel = viewModel)
                    EditorTab.SPELLS -> SpellbookScreen(viewModel = viewModel)
                    EditorTab.INVENTORY -> InventoryScreen(viewModel = viewModel)
                    EditorTab.ARMORY -> ArmoryScreen(viewModel = viewModel)
                    EditorTab.SAVES -> SaveManagerScreen(viewModel = viewModel)
                }
            }
        }
    }
}
