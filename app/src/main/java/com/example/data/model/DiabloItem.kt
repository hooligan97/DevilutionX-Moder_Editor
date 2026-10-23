package com.example.data.model

import java.util.UUID

enum class ItemQuality(val displayName: String) {
    NORMAL("Normal"),
    MAGIC("Magic"),
    UNIQUE("Unique")
}

enum class EquipSlot(val displayName: String, val shortLabel: String) {
    HEAD("Head", "Helm"),
    NECK("Neck", "Amulet"),
    TORSO("Torso", "Armor"),
    LEFT_HAND("Left Hand", "Main Hand"),
    RIGHT_HAND("Right Hand", "Off Hand / Shield"),
    LEFT_RING("Left Finger", "Ring 1"),
    RIGHT_RING("Right Finger", "Ring 2")
}

data class DiabloItem(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val baseType: String,
    val quality: ItemQuality = ItemQuality.NORMAL,
    val prefix: String = "",
    val suffix: String = "",
    val durability: Int = 50,
    val maxDurability: Int = 50,
    val isIndestructible: Boolean = false,
    val charges: Int = 0,
    val maxCharges: Int = 0,
    val armorClass: Int = 0,
    val minDamage: Int = 0,
    val maxDamage: Int = 0,
    val toHitBonus: Int = 0,
    val damageBonus: Int = 0,
    val strengthReq: Int = 0,
    val magicReq: Int = 0,
    val dexterityReq: Int = 0,
    val fireResist: Int = 0,
    val lightningResist: Int = 0,
    val magicResist: Int = 0,
    val allAttributesBonus: Int = 0,
    val hitPointsBonus: Int = 0,
    val manaBonus: Int = 0,
    val extraDescription: String = "",
    val gridWidth: Int = 1,
    val gridHeight: Int = 1,
    val gridX: Int = -1,
    val gridY: Int = -1
) {
    val fullTitle: String
        get() = when (quality) {
            ItemQuality.UNIQUE -> name
            ItemQuality.MAGIC -> {
                val pre = if (prefix.isNotBlank()) "$prefix " else ""
                val suf = if (suffix.isNotBlank()) " $suffix" else ""
                "$pre$baseType$suf".trim()
            }
            ItemQuality.NORMAL -> baseType
        }
}
