package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.CharacterViewModel
import com.example.ui.components.GothicCard
import com.example.ui.theme.*

@Composable
fun SaveManagerScreen(
    viewModel: CharacterViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val hero by viewModel.character.collectAsState()
    val mpqInfo by viewModel.mpqInfo.collectAsState()
    val hexDump by viewModel.hexDump.collectAsState()
    val vault by viewModel.vault.collectAsState()

    var showJsonDialog by remember { mutableStateOf(false) }
    var jsonInputText by remember { mutableStateOf("") }
    var showSnapshotDialog by remember { mutableStateOf(false) }
    var snapshotLabel by remember { mutableStateOf("") }

    // File Pickers using Storage Access Framework (SAF)
    val openSaveLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            runCatching {
                context.contentResolver.openInputStream(uri)?.use { stream ->
                    val bytes = stream.readBytes()
                    val filename = uri.lastPathSegment ?: "save.sv"
                    if (filename.endsWith(".json", ignoreCase = true) || bytes.take(10).toByteArray().decodeToString().contains("{")) {
                        viewModel.importNoctonicJson(String(bytes))
                    } else {
                        viewModel.importSaveBytes(bytes, filename)
                    }
                }
            }.onFailure {
                viewModel.showMessage("Failed to open file: ${it.message}")
            }
        }
    }

    val createSaveLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/octet-stream")
    ) { uri ->
        if (uri != null) {
            runCatching {
                context.contentResolver.openOutputStream(uri)?.use { stream ->
                    val mpqBytes = viewModel.exportSaveMpqBytes()
                    stream.write(mpqBytes)
                }
                viewModel.showMessage("DevilutionX Save exported successfully!")
            }.onFailure {
                viewModel.showMessage("Failed to export: ${it.message}")
            }
        }
    }

    val createJsonLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        if (uri != null) {
            runCatching {
                context.contentResolver.openOutputStream(uri)?.use { stream ->
                    val jsonBytes = viewModel.getNoctonicJson().toByteArray(Charsets.UTF_8)
                    stream.write(jsonBytes)
                }
                viewModel.showMessage("Noctonic JSON exported successfully!")
            }.onFailure {
                viewModel.showMessage("Failed to export JSON: ${it.message}")
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // DevilutionX Android Save File Guide
        item {
            GothicCard(borderColor = BloodRedDark) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.FolderSpecial, contentDescription = null, tint = BloodRedBright)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("DevilutionX Android Paths", color = BloodRedBright, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "On Android devices, DevilutionX save files are stored in:\n" +
                           "/Android/data/org.diasurgical.devilutionx/files/\n\n" +
                           "• Single Player Diablo 1: single_0.sv .. single_3.sv\n" +
                           "• Multiplayer Diablo 1: multi_0.sv .. multi_3.sv\n" +
                           "• Hellfire expansion: single_0.hsv, multi_0.hsv",
                    color = BoneGray,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
            }
        }

        // File Actions (Import / Export)
        item {
            GothicCard(borderColor = DemonicGoldDark) {
                Text("File Operations", color = DemonicGold, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilledTonalButton(
                        onClick = { openSaveLauncher.launch(arrayOf("*/*")) },
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = DiabloSurfaceElevated,
                            contentColor = DemonicGoldBright
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("open_save_button")
                    ) {
                        Icon(Icons.Default.FileOpen, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Open .sv / .json", fontSize = 12.sp)
                    }

                    FilledTonalButton(
                        onClick = { createSaveLauncher.launch("single_0.sv") },
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = DiabloSurfaceElevated,
                            contentColor = BloodRedBright
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("export_sv_button")
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Save single_0.sv", fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { createJsonLauncher.launch("${hero.name.lowercase()}_hero.json") },
                        border = BorderStroke(1.dp, DarkBorder),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Export JSON", fontSize = 12.sp, color = NormalWhite)
                    }

                    OutlinedButton(
                        onClick = {
                            val clip = ClipData.newPlainText("Noctonic JSON", viewModel.getNoctonicJson())
                            val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            cm.setPrimaryClip(clip)
                            viewModel.showMessage("Noctonic JSON copied to clipboard! (Ready to paste on noctonic.github.io/diablo)")
                        },
                        border = BorderStroke(1.dp, DemonicGoldDark),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, tint = DemonicGoldBright, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Copy JSON", fontSize = 12.sp, color = DemonicGoldBright)
                    }

                    OutlinedButton(
                        onClick = {
                            jsonInputText = ""
                            showJsonDialog = true
                        },
                        border = BorderStroke(1.dp, ArcaneBlueDark),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Paste JSON", fontSize = 12.sp, color = ArcaneBlueBright)
                    }
                }
            }
        }

        // MPQ Tools & Hex Inspector
        item {
            GothicCard(borderColor = ArcaneBlueDark) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Memory, contentDescription = null, tint = ArcaneBlueBright)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("MPQ Archive & Hex Tool", color = ArcaneBlueBright, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }

                    FilledTonalButton(
                        onClick = {
                            val testBytes = viewModel.exportSaveMpqBytes()
                            viewModel.inspectBytes(testBytes)
                            viewModel.showMessage("Analyzed generated DevilutionX MPQ container")
                        },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        modifier = Modifier.defaultMinSize(minHeight = 28.dp)
                    ) {
                        Text("Inspect Current", fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (mpqInfo != null) {
                    val info = mpqInfo!!
                    Card(
                        colors = CardDefaults.cardColors(containerColor = DiabloSurfaceElevated),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Signature: ${info.signature} • ${if (info.isValidMpq) "Valid MPQ Container" else "Binary Payload"}", color = DemonicGoldBright, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text("Archive Size: ${info.archiveSize} bytes • Header: ${info.headerSize} bytes • Sector: ${info.sectorSize} bytes", color = BoneGray, fontSize = 11.sp)
                            Text("Internal Files: ${info.detectedInternalFiles.joinToString(", ")}", color = ArcaneBlueBright, fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (hexDump != null) {
                    SelectionContainer {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF090605), RoundedCornerShape(6.dp))
                                .padding(8.dp)
                        ) {
                            Text(
                                text = hexDump!!,
                                color = NormalWhite,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                lineHeight = 13.sp
                            )
                        }
                    }
                } else {
                    Text(
                        text = "Open a .sv file or tap 'Inspect Current' to view MPQ headers and binary hex offsets.",
                        color = BoneGray,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Snapshot Vault
        item {
            GothicCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Hero Backup Vault", color = DemonicGold, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    IconButton(onClick = {
                        snapshotLabel = "${hero.name} (Lv ${hero.level})"
                        showSnapshotDialog = true
                    }) {
                        Icon(Icons.Default.AddBox, contentDescription = "Add Snapshot", tint = DemonicGoldBright)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    vault.forEach { item ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = DiabloSurfaceElevated),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(item.label, color = NormalWhite, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                    Text("${item.character.name} • Lv ${item.character.level} ${item.character.heroClass.displayName} • ${item.timestamp}", color = BoneGray, fontSize = 11.sp)
                                }

                                FilledTonalButton(
                                    onClick = { viewModel.loadFromVault(item) },
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                    modifier = Modifier.defaultMinSize(minHeight = 28.dp)
                                ) {
                                    Text("Restore", fontSize = 11.sp, color = DemonicGoldBright)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Paste Noctonic JSON Dialog
    if (showJsonDialog) {
        AlertDialog(
            onDismissRequest = { showJsonDialog = false },
            containerColor = DiabloSurface,
            title = { Text("Paste Noctonic DEADitor JSON", color = DemonicGold, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Paste JSON exported from noctonic.github.io/diablo:", color = BoneGray, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = jsonInputText,
                        onValueChange = { jsonInputText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = DemonicGold,
                            unfocusedBorderColor = DarkBorder
                        ),
                        placeholder = { Text("{\"name\": \"...\", \"class\": \"...\"}") }
                    )
                }
            },
            confirmButton = {
                FilledTonalButton(
                    onClick = {
                        if (viewModel.importNoctonicJson(jsonInputText)) {
                            showJsonDialog = false
                        }
                    },
                    colors = ButtonDefaults.filledTonalButtonColors(containerColor = DemonicGoldDark)
                ) {
                    Text("Import Hero", color = DemonicGoldBright)
                }
            },
            dismissButton = {
                TextButton(onClick = { showJsonDialog = false }) {
                    Text("Cancel", color = BoneGray)
                }
            }
        )
    }

    // Create Snapshot Dialog
    if (showSnapshotDialog) {
        AlertDialog(
            onDismissRequest = { showSnapshotDialog = false },
            containerColor = DiabloSurface,
            title = { Text("Save Character Snapshot", color = DemonicGold, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Enter a label for this backup:", color = BoneGray, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = snapshotLabel,
                        onValueChange = { snapshotLabel = it },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                FilledTonalButton(
                    onClick = {
                        viewModel.saveToVault(snapshotLabel)
                        showSnapshotDialog = false
                    },
                    colors = ButtonDefaults.filledTonalButtonColors(containerColor = DemonicGoldDark)
                ) {
                    Text("Save Snapshot", color = DemonicGoldBright)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSnapshotDialog = false }) {
                    Text("Cancel", color = BoneGray)
                }
            }
        )
    }
}
