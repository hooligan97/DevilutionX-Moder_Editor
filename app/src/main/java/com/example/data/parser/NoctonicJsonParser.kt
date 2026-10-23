package com.example.data.parser

import com.example.data.model.*
import org.json.JSONArray
import org.json.JSONObject

object NoctonicJsonParser {

    /**
     * Serializes a HeroCharacter into the Noctonic DEADitor JSON format.
     */
    fun toJson(hero: HeroCharacter): String {
        val root = JSONObject()

        // Identity
        root.put("name", hero.name)
        root.put("class", hero.heroClass.displayName)
        root.put("level", hero.level)
        root.put("experience", hero.experience)
        root.put("gold", hero.gold)
        root.put("difficulty", hero.difficulty)
        root.put("dungeonLevel", hero.dungeonLevel)
        root.put("questsCompleted", hero.questsCompleted)

        // Core Stats
        val statsObj = JSONObject()
        statsObj.put("strength", hero.strength)
        statsObj.put("baseStrength", hero.baseStrength)
        statsObj.put("magic", hero.magic)
        statsObj.put("baseMagic", hero.baseMagic)
        statsObj.put("dexterity", hero.dexterity)
        statsObj.put("baseDexterity", hero.baseDexterity)
        statsObj.put("vitality", hero.vitality)
        statsObj.put("baseVitality", hero.baseVitality)
        statsObj.put("statPoints", hero.statPoints)
        statsObj.put("life", hero.life)
        statsObj.put("maxLife", hero.maxLife)
        statsObj.put("baseLife", hero.baseLife)
        statsObj.put("mana", hero.mana)
        statsObj.put("maxMana", hero.maxMana)
        statsObj.put("baseMana", hero.baseMana)
        statsObj.put("manaShieldActive", hero.manaShieldActive)
        root.put("stats", statsObj)

        // Spells
        val spellsObj = JSONObject()
        hero.spellLevels.forEach { (spellId, level) ->
            spellsObj.put(spellId, level)
        }
        root.put("spells", spellsObj)

        // Equipment
        val equipObj = JSONObject()
        hero.equipment.forEach { (slot, item) ->
            equipObj.put(slot.name, itemToJson(item))
        }
        root.put("equipment", equipObj)

        // Inventory
        val invArray = JSONArray()
        hero.inventory.forEach { item ->
            invArray.put(itemToJson(item))
        }
        root.put("inventory", invArray)

        // Belt
        val beltArray = JSONArray()
        hero.belt.forEach { item ->
            if (item != null) beltArray.put(itemToJson(item)) else beltArray.put(JSONObject.NULL)
        }
        root.put("belt", beltArray)

        return root.toString(2)
    }

    private fun itemToJson(item: DiabloItem): JSONObject {
        val obj = JSONObject()
        obj.put("id", item.id)
        obj.put("name", item.name)
        obj.put("baseType", item.baseType)
        obj.put("quality", item.quality.name)
        obj.put("prefix", item.prefix)
        obj.put("suffix", item.suffix)
        obj.put("durability", item.durability)
        obj.put("maxDurability", item.maxDurability)
        obj.put("isIndestructible", item.isIndestructible)
        obj.put("charges", item.charges)
        obj.put("maxCharges", item.maxCharges)
        obj.put("armorClass", item.armorClass)
        obj.put("minDamage", item.minDamage)
        obj.put("maxDamage", item.maxDamage)
        obj.put("toHitBonus", item.toHitBonus)
        obj.put("damageBonus", item.damageBonus)
        obj.put("strengthReq", item.strengthReq)
        obj.put("magicReq", item.magicReq)
        obj.put("dexterityReq", item.dexterityReq)
        obj.put("fireResist", item.fireResist)
        obj.put("lightningResist", item.lightningResist)
        obj.put("magicResist", item.magicResist)
        obj.put("allAttributesBonus", item.allAttributesBonus)
        obj.put("hitPointsBonus", item.hitPointsBonus)
        obj.put("manaBonus", item.manaBonus)
        obj.put("extraDescription", item.extraDescription)
        obj.put("gridWidth", item.gridWidth)
        obj.put("gridHeight", item.gridHeight)
        obj.put("gridX", item.gridX)
        obj.put("gridY", item.gridY)
        return obj
    }

    /**
     * Parses a Noctonic DEADitor JSON string into a HeroCharacter.
     */
    fun fromJson(jsonStr: String): HeroCharacter {
        val root = JSONObject(jsonStr)

        val name = root.optString("name", "Hero")
        val className = root.optString("class", "Warrior")
        val heroClass = HeroClass.fromString(className)
        val level = root.optInt("level", 1)
        val experience = root.optLong("experience", 0L)
        val gold = root.optLong("gold", 100L)
        val difficulty = root.optString("difficulty", "Normal")
        val dungeonLevel = root.optInt("dungeonLevel", 0)
        val questsCompleted = root.optInt("questsCompleted", 0)

        // Stats
        val statsObj = root.optJSONObject("stats")
        val strength = statsObj?.optInt("strength", heroClass.baseStrength) ?: heroClass.baseStrength
        val baseStrength = statsObj?.optInt("baseStrength", strength) ?: strength
        val magic = statsObj?.optInt("magic", heroClass.baseMagic) ?: heroClass.baseMagic
        val baseMagic = statsObj?.optInt("baseMagic", magic) ?: magic
        val dexterity = statsObj?.optInt("dexterity", heroClass.baseDexterity) ?: heroClass.baseDexterity
        val baseDexterity = statsObj?.optInt("baseDexterity", dexterity) ?: dexterity
        val vitality = statsObj?.optInt("vitality", heroClass.baseVitality) ?: heroClass.baseVitality
        val baseVitality = statsObj?.optInt("baseVitality", vitality) ?: vitality
        val statPoints = statsObj?.optInt("statPoints", 0) ?: 0

        val life = statsObj?.optInt("life", heroClass.baseHp) ?: heroClass.baseHp
        val maxLife = statsObj?.optInt("maxLife", heroClass.baseHp) ?: heroClass.baseHp
        val baseLife = statsObj?.optInt("baseLife", maxLife) ?: maxLife
        val mana = statsObj?.optInt("mana", heroClass.baseMana) ?: heroClass.baseMana
        val maxMana = statsObj?.optInt("maxMana", heroClass.baseMana) ?: heroClass.baseMana
        val baseMana = statsObj?.optInt("baseMana", maxMana) ?: maxMana
        val manaShieldActive = statsObj?.optBoolean("manaShieldActive", false) ?: false

        // Spells
        val spellLevels = mutableMapOf<String, Int>()
        val spellsObj = root.optJSONObject("spells")
        if (spellsObj != null) {
            val keys = spellsObj.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                spellLevels[key] = spellsObj.optInt(key, 0)
            }
        }

        // Equipment
        val equipment = mutableMapOf<EquipSlot, DiabloItem>()
        val equipObj = root.optJSONObject("equipment")
        if (equipObj != null) {
            val keys = equipObj.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                val slot = runCatching { EquipSlot.valueOf(key) }.getOrNull()
                val itemJson = equipObj.optJSONObject(key)
                if (slot != null && itemJson != null) {
                    equipment[slot] = parseItem(itemJson)
                }
            }
        }

        // Inventory
        val inventory = mutableListOf<DiabloItem>()
        val invArray = root.optJSONArray("inventory")
        if (invArray != null) {
            for (i in 0 until invArray.length()) {
                val itemObj = invArray.optJSONObject(i)
                if (itemObj != null) {
                    inventory.add(parseItem(itemObj))
                }
            }
        }

        // Belt
        val belt = MutableList<DiabloItem?>(8) { null }
        val beltArray = root.optJSONArray("belt")
        if (beltArray != null) {
            for (i in 0 until minOf(8, beltArray.length())) {
                val itemObj = beltArray.optJSONObject(i)
                if (itemObj != null) {
                    belt[i] = parseItem(itemObj)
                }
            }
        }

        return HeroCharacter(
            name = name,
            heroClass = heroClass,
            level = level,
            experience = experience,
            gold = gold,
            difficulty = difficulty,
            dungeonLevel = dungeonLevel,
            questsCompleted = questsCompleted,
            strength = strength,
            baseStrength = baseStrength,
            magic = magic,
            baseMagic = baseMagic,
            dexterity = dexterity,
            baseDexterity = baseDexterity,
            vitality = vitality,
            baseVitality = baseVitality,
            statPoints = statPoints,
            life = life,
            maxLife = maxLife,
            baseLife = baseLife,
            mana = mana,
            maxMana = maxMana,
            baseMana = baseMana,
            manaShieldActive = manaShieldActive,
            spellLevels = spellLevels,
            equipment = equipment,
            inventory = inventory,
            belt = belt
        )
    }

    private fun parseItem(obj: JSONObject): DiabloItem {
        val qualityName = obj.optString("quality", "NORMAL")
        val quality = runCatching { ItemQuality.valueOf(qualityName) }.getOrDefault(ItemQuality.NORMAL)

        return DiabloItem(
            id = obj.optString("id", java.util.UUID.randomUUID().toString()),
            name = obj.optString("name", "Unknown Item"),
            baseType = obj.optString("baseType", "Item"),
            quality = quality,
            prefix = obj.optString("prefix", ""),
            suffix = obj.optString("suffix", ""),
            durability = obj.optInt("durability", 50),
            maxDurability = obj.optInt("maxDurability", 50),
            isIndestructible = obj.optBoolean("isIndestructible", false),
            charges = obj.optInt("charges", 0),
            maxCharges = obj.optInt("maxCharges", 0),
            armorClass = obj.optInt("armorClass", 0),
            minDamage = obj.optInt("minDamage", 0),
            maxDamage = obj.optInt("maxDamage", 0),
            toHitBonus = obj.optInt("toHitBonus", 0),
            damageBonus = obj.optInt("damageBonus", 0),
            strengthReq = obj.optInt("strengthReq", 0),
            magicReq = obj.optInt("magicReq", 0),
            dexterityReq = obj.optInt("dexterityReq", 0),
            fireResist = obj.optInt("fireResist", 0),
            lightningResist = obj.optInt("lightningResist", 0),
            magicResist = obj.optInt("magicResist", 0),
            allAttributesBonus = obj.optInt("allAttributesBonus", 0),
            hitPointsBonus = obj.optInt("hitPointsBonus", 0),
            manaBonus = obj.optInt("manaBonus", 0),
            extraDescription = obj.optString("extraDescription", ""),
            gridWidth = obj.optInt("gridWidth", 1),
            gridHeight = obj.optInt("gridHeight", 1),
            gridX = obj.optInt("gridX", -1),
            gridY = obj.optInt("gridY", -1)
        )
    }
}
