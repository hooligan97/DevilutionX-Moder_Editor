package com.example.ui

import androidx.lifecycle.ViewModel
import com.example.data.model.*
import com.example.data.parser.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class EditorTab(val title: String, val iconLabel: String) {
    HERO("Hero", "Hero Identity"),
    STATS("Stats", "Attributes & Orbs"),
    SPELLS("Spells", "Spellbook"),
    INVENTORY("Inventory", "Equipment & Grid"),
    ARMORY("Armory", "Legendary Catalog"),
    SAVES("Saves & MPQ", "Files & Tools")
}

data class SavedVaultHero(
    val id: String = java.util.UUID.randomUUID().toString(),
    val label: String,
    val character: HeroCharacter,
    val timestamp: String
)

class CharacterViewModel : ViewModel() {

    private val _character = MutableStateFlow(HeroCharacter.createGodly(HeroClass.WARRIOR, "Aidan"))
    val character: StateFlow<HeroCharacter> = _character.asStateFlow()

    private val _activeTab = MutableStateFlow(EditorTab.HERO)
    val activeTab: StateFlow<EditorTab> = _activeTab.asStateFlow()

    private val _statusMessage = MutableStateFlow<String?>("Welcome to DevilutionX Hero Editor")
    val statusMessage: StateFlow<String?> = _statusMessage.asStateFlow()

    private val _mpqInfo = MutableStateFlow<MpqHeaderInfo?>(null)
    val mpqInfo: StateFlow<MpqHeaderInfo?> = _mpqInfo.asStateFlow()

    private val _hexDump = MutableStateFlow<String?>(null)
    val hexDump: StateFlow<String?> = _hexDump.asStateFlow()

    private val _vault = MutableStateFlow<List<SavedVaultHero>>(
        listOf(
            SavedVaultHero(label = "Godly Warrior Lv50", character = HeroCharacter.createGodly(HeroClass.WARRIOR, "Sir Aidan"), timestamp = "Preset"),
            SavedVaultHero(label = "Archmage Sorcerer Lv50", character = HeroCharacter.createGodly(HeroClass.SORCERER, "Jazreth"), timestamp = "Preset"),
            SavedVaultHero(label = "Shadow Rogue Lv50", character = HeroCharacter.createGodly(HeroClass.ROGUE, "Moreina"), timestamp = "Preset"),
            SavedVaultHero(label = "Starter Monk Lv1", character = HeroCharacter.createStarter(HeroClass.MONK, "Bohdi"), timestamp = "Preset")
        )
    )
    val vault: StateFlow<List<SavedVaultHero>> = _vault.asStateFlow()

    fun selectTab(tab: EditorTab) {
        _activeTab.value = tab
    }

    fun clearStatusMessage() {
        _statusMessage.value = null
    }

    fun showMessage(msg: String) {
        _statusMessage.value = msg
    }

    // Hero Identity
    fun updateName(name: String) {
        _character.update { it.copy(name = name.take(15)) }
    }

    fun updateHeroClass(newClass: HeroClass) {
        _character.update { current ->
            current.copy(
                heroClass = newClass,
                strength = newClass.baseStrength.coerceAtLeast(current.strength.coerceAtMost(newClass.maxStrength)),
                magic = newClass.baseMagic.coerceAtLeast(current.magic.coerceAtMost(newClass.maxMagic)),
                dexterity = newClass.baseDexterity.coerceAtLeast(current.dexterity.coerceAtMost(newClass.maxDexterity)),
                vitality = newClass.baseVitality.coerceAtLeast(current.vitality.coerceAtMost(newClass.maxVitality))
            )
        }
        showMessage("Class changed to ${newClass.displayName}")
    }

    fun updateLevel(level: Int) {
        val clamped = level.coerceIn(1, 50)
        val calculatedXp = HeroCharacter.xpForLevel(clamped)
        _character.update { it.copy(level = clamped, experience = calculatedXp) }
        showMessage("Level set to $clamped (XP updated)")
    }

    fun updateExperience(xp: Long) {
        val clampedXp = xp.coerceIn(0L, 2_000_000_000L)
        val derivedLvl = HeroCharacter.levelForXp(clampedXp)
        _character.update { it.copy(experience = clampedXp, level = derivedLvl) }
    }

    fun updateGold(gold: Long) {
        val clamped = gold.coerceIn(0L, 999_999L)
        _character.update { it.copy(gold = clamped) }
    }

    fun updateDifficulty(diff: String) {
        _character.update { it.copy(difficulty = diff) }
    }

    fun updateDungeonLevel(floor: Int) {
        _character.update { it.copy(dungeonLevel = floor.coerceIn(0, 24)) }
    }

    // Stats
    fun modifyStat(stat: String, delta: Int) {
        _character.update { c ->
            when (stat.lowercase()) {
                "strength", "str" -> {
                    val newVal = (c.strength + delta).coerceIn(0, 300)
                    c.copy(strength = newVal, baseStrength = newVal)
                }
                "magic", "mag" -> {
                    val newVal = (c.magic + delta).coerceIn(0, 300)
                    c.copy(magic = newVal, baseMagic = newVal)
                }
                "dexterity", "dex" -> {
                    val newVal = (c.dexterity + delta).coerceIn(0, 300)
                    c.copy(dexterity = newVal, baseDexterity = newVal)
                }
                "vitality", "vit" -> {
                    val newVal = (c.vitality + delta).coerceIn(0, 300)
                    c.copy(vitality = newVal, baseVitality = newVal)
                }
                "points", "statpoints" -> {
                    val newVal = (c.statPoints + delta).coerceIn(0, 999)
                    c.copy(statPoints = newVal)
                }
                else -> c
            }
        }
    }

    fun updateLife(current: Int, max: Int) {
        _character.update {
            val c = current.coerceIn(1, 1500)
            val m = max.coerceIn(1, 1500)
            it.copy(life = c, maxLife = m, baseLife = m)
        }
    }

    fun updateMana(current: Int, max: Int) {
        _character.update {
            val c = current.coerceIn(0, 1500)
            val m = max.coerceIn(0, 1500)
            it.copy(mana = c, maxMana = m, baseMana = m)
        }
    }

    fun toggleManaShield() {
        _character.update { it.copy(manaShieldActive = !it.manaShieldActive) }
        showMessage("Mana Shield ${_character.value.manaShieldActive.let { if (it) "Activated" else "Deactivated" }}")
    }

    // Spells
    fun setSpellLevel(spellId: String, level: Int) {
        _character.update { current ->
            val updated = current.spellLevels.toMutableMap()
            if (level <= 0) {
                updated.remove(spellId)
            } else {
                updated[spellId] = level.coerceIn(1, 15)
            }
            current.copy(spellLevels = updated)
        }
    }

    // Cheats / Quick Enhancements
    fun maxAllSpells() {
        val allSpells = DiabloSpellsCatalog.ALL_SPELLS.associate { it.id to 15 }
        _character.update { it.copy(spellLevels = allSpells) }
        showMessage("All 31 Diablo & Hellfire Spells set to Level 15!")
    }

    fun maxOutAllStats() {
        _character.update { c ->
            val cls = c.heroClass
            c.copy(
                strength = cls.maxStrength,
                baseStrength = cls.maxStrength,
                magic = cls.maxMagic,
                baseMagic = cls.maxMagic,
                dexterity = cls.maxDexterity,
                baseDexterity = cls.maxDexterity,
                vitality = cls.maxVitality,
                baseVitality = cls.maxVitality,
                statPoints = 25
            )
        }
        showMessage("Stats maxed out to class limits (${_character.value.heroClass.displayName})!")
    }

    fun maxGold() {
        _character.update { it.copy(gold = 999_999L) }
        showMessage("Gold set to 999,999!")
    }

    fun fullHeal() {
        _character.update { it.copy(life = it.maxLife, mana = it.maxMana) }
        showMessage("Hit Points and Mana fully restored!")
    }

    // Equipment & Inventory
    fun equipItem(slot: EquipSlot, item: DiabloItem) {
        _character.update { current ->
            val newEquipment = current.equipment.toMutableMap()
            newEquipment[slot] = item
            current.copy(equipment = newEquipment)
        }
        showMessage("Equipped ${item.fullTitle} in ${slot.displayName}")
    }

    fun unequipItem(slot: EquipSlot) {
        _character.update { current ->
            val item = current.equipment[slot]
            if (item != null) {
                val newEquipment = current.equipment.toMutableMap()
                newEquipment.remove(slot)
                val newInventory = current.inventory.toMutableList()
                newInventory.add(item)
                current.copy(equipment = newEquipment, inventory = newInventory)
            } else {
                current
            }
        }
        showMessage("Unequipped ${slot.displayName} to backpack")
    }

    fun addItemToInventory(item: DiabloItem) {
        _character.update { current ->
            if (current.inventory.size < 40) {
                current.copy(inventory = current.inventory + item)
            } else {
                current
            }
        }
        showMessage("Added ${item.fullTitle} to Backpack")
    }

    fun removeItemFromInventory(itemId: String) {
        _character.update { current ->
            current.copy(inventory = current.inventory.filterNot { it.id == itemId })
        }
        showMessage("Item removed from Backpack")
    }

    fun setBeltSlot(index: Int, item: DiabloItem?) {
        if (index in 0..7) {
            _character.update { current ->
                val newBelt = current.belt.toMutableList()
                newBelt[index] = item
                current.copy(belt = newBelt)
            }
        }
    }

    // Save & Load (Noctonic JSON & DevilutionX MPQ)
    fun importNoctonicJson(jsonStr: String): Boolean {
        return try {
            val loaded = NoctonicJsonParser.fromJson(jsonStr)
            _character.value = loaded
            showMessage("Successfully imported character: ${loaded.name} (Lv ${loaded.level} ${loaded.heroClass.displayName})")
            true
        } catch (e: Exception) {
            showMessage("JSON Parse error: ${e.message}")
            false
        }
    }

    fun getNoctonicJson(): String {
        return NoctonicJsonParser.toJson(_character.value)
    }

    fun importSaveBytes(bytes: ByteArray, filename: String): Boolean {
        return try {
            val mpq = MpqTool.analyzeMpqHeader(bytes)
            _mpqInfo.value = mpq
            _hexDump.value = MpqTool.formatHexDump(bytes, 24)

            val parsedHero = DevilutionXSaveParser.parseSaveBytes(bytes)
            _character.value = parsedHero
            showMessage("Imported $filename (${parsedHero.name}, Lv ${parsedHero.level} ${parsedHero.heroClass.displayName})")
            true
        } catch (e: Exception) {
            showMessage("Save parse error: ${e.message}")
            false
        }
    }

    fun exportSaveMpqBytes(): ByteArray {
        return DevilutionXSaveParser.createSaveFileMpq(_character.value)
    }

    fun inspectBytes(bytes: ByteArray) {
        val mpq = MpqTool.analyzeMpqHeader(bytes)
        _mpqInfo.value = mpq
        _hexDump.value = MpqTool.formatHexDump(bytes, 32)
    }

    // Vault
    fun saveToVault(label: String) {
        val cleanLabel = if (label.isBlank()) "${_character.value.name} (Lv ${_character.value.level})" else label
        val newVaultItem = SavedVaultHero(
            label = cleanLabel,
            character = _character.value,
            timestamp = "Local Snapshot"
        )
        _vault.update { listOf(newVaultItem) + it }
        showMessage("Saved snapshot \"$cleanLabel\" to Vault")
    }

    fun loadFromVault(vaultItem: SavedVaultHero) {
        _character.value = vaultItem.character
        showMessage("Loaded \"${vaultItem.label}\" from Vault")
    }
}
