package com.example.data.model

object ItemArmory {
    val LEGENDARY_CATALOG = listOf(
        // Weapons
        DiabloItem(
            name = "The Grandfather",
            baseType = "Great Sword",
            quality = ItemQuality.UNIQUE,
            minDamage = 10,
            maxDamage = 20,
            damageBonus = 70,
            toHitBonus = 20,
            allAttributesBonus = 5,
            hitPointsBonus = 20,
            armorClass = 0,
            isIndestructible = true,
            extraDescription = "One-handed great sword of ancient barbarian kings",
            gridWidth = 1,
            gridHeight = 3
        ),
        DiabloItem(
            name = "King's Sword of Haste",
            baseType = "Bastard Sword",
            quality = ItemQuality.MAGIC,
            prefix = "King's",
            suffix = "of Haste",
            minDamage = 8,
            maxDamage = 16,
            damageBonus = 175,
            toHitBonus = 100,
            durability = 60,
            maxDurability = 60,
            extraDescription = "Fastest attack speed, unmatched melee DPS",
            gridWidth = 1,
            gridHeight = 3
        ),
        DiabloItem(
            name = "Windforce",
            baseType = "Long War Bow",
            quality = ItemQuality.UNIQUE,
            minDamage = 1,
            maxDamage = 14,
            damageBonus = 200,
            durability = 45,
            maxDurability = 45,
            strengthReq = 45,
            dexterityReq = 80,
            extraDescription = "Knocks target back with legendary gale force",
            gridWidth = 2,
            gridHeight = 3
        ),
        DiabloItem(
            name = "Eaglehorn",
            baseType = "Composite Bow",
            quality = ItemQuality.UNIQUE,
            minDamage = 4,
            maxDamage = 10,
            damageBonus = 100,
            toHitBonus = 50,
            dexterityReq = 55,
            isIndestructible = true,
            extraDescription = "Always hits target with piercing sight",
            gridWidth = 2,
            gridHeight = 3
        ),
        DiabloItem(
            name = "Arch-Angel's Staff of Apocalypse",
            baseType = "War Staff",
            quality = ItemQuality.MAGIC,
            prefix = "Arch-Angel's",
            suffix = "of Apocalypse",
            minDamage = 8,
            maxDamage = 16,
            charges = 12,
            maxCharges = 12,
            magicReq = 105,
            extraDescription = "+2 to all spell levels, casts Apocalypse charges",
            gridWidth = 1,
            gridHeight = 3
        ),
        DiabloItem(
            name = "Naj's Puzzler",
            baseType = "Long Staff",
            quality = ItemQuality.UNIQUE,
            charges = 57,
            maxCharges = 57,
            allAttributesBonus = 10,
            manaBonus = 20,
            extraDescription = "Instant teleportation charges with infinite versatility",
            gridWidth = 1,
            gridHeight = 3
        ),
        DiabloItem(
            name = "Griswold's Edge",
            baseType = "Broad Sword",
            quality = ItemQuality.UNIQUE,
            minDamage = 4,
            maxDamage = 12,
            damageBonus = 25,
            toHitBonus = 25,
            manaBonus = 20,
            extraDescription = "Fast attack rate and adds 1-10 fire damage",
            gridWidth = 1,
            gridHeight = 3
        ),

        // Body Armor
        DiabloItem(
            name = "Godly Plate of the Whale",
            baseType = "Full Plate Mail",
            quality = ItemQuality.MAGIC,
            prefix = "Godly",
            suffix = "of the Whale",
            armorClass = 175,
            hitPointsBonus = 100,
            durability = 90,
            maxDurability = 90,
            strengthReq = 90,
            extraDescription = "God-tier +200% Armor Class & +100 Max Hit Points",
            gridWidth = 2,
            gridHeight = 3
        ),
        DiabloItem(
            name = "Arkaine's Valor",
            baseType = "Splint Mail",
            quality = ItemQuality.UNIQUE,
            armorClass = 25,
            hitPointsBonus = 10,
            strengthReq = 30,
            extraDescription = "Fastest hit recovery, reduces damage by 3",
            gridWidth = 2,
            gridHeight = 3
        ),
        DiabloItem(
            name = "Demonspike Coat",
            baseType = "Full Plate Mail",
            quality = ItemQuality.UNIQUE,
            armorClass = 100,
            fireResist = 50,
            isIndestructible = true,
            extraDescription = "Reduces damage by 6, indestructible iron shell",
            gridWidth = 2,
            gridHeight = 3
        ),
        DiabloItem(
            name = "Naj's Light Plate",
            baseType = "Light Plate",
            quality = ItemQuality.UNIQUE,
            armorClass = 45,
            allAttributesBonus = 5,
            manaBonus = 20,
            magicResist = 20,
            extraDescription = "Zero movement speed penalty for high casters",
            gridWidth = 2,
            gridHeight = 3
        ),

        // Helms
        DiabloItem(
            name = "The Thinking Cap",
            baseType = "Skull Cap",
            quality = ItemQuality.UNIQUE,
            armorClass = 2,
            manaBonus = 30,
            allAttributesBonus = 0,
            fireResist = 20,
            lightningResist = 20,
            magicResist = 20,
            extraDescription = "+2 to all spell levels, alters mana absorption",
            gridWidth = 2,
            gridHeight = 2
        ),
        DiabloItem(
            name = "Royal Crest of Alton",
            baseType = "Crown",
            quality = ItemQuality.UNIQUE,
            armorClass = 12,
            allAttributesBonus = 10,
            manaBonus = 40,
            durability = 40,
            maxDurability = 40,
            extraDescription = "+10 to All Attributes, +40 Mana, fast recovery",
            gridWidth = 2,
            gridHeight = 2
        ),
        DiabloItem(
            name = "Veil of Steel",
            baseType = "Great Helm",
            quality = ItemQuality.UNIQUE,
            armorClass = 18,
            fireResist = 50,
            lightningResist = 50,
            magicResist = 50,
            allAttributesBonus = 15,
            extraDescription = "+50% to all elemental and magic resistances",
            gridWidth = 2,
            gridHeight = 2
        ),
        DiabloItem(
            name = "Gotterdamerung",
            baseType = "Great Helm",
            quality = ItemQuality.UNIQUE,
            armorClass = 60,
            allAttributesBonus = 20,
            extraDescription = "+20 to All Attributes, massive AC, zero light radius",
            gridWidth = 2,
            gridHeight = 2
        ),

        // Shields
        DiabloItem(
            name = "Stormshield",
            baseType = "Gothic Shield",
            quality = ItemQuality.UNIQUE,
            armorClass = 40,
            isIndestructible = true,
            extraDescription = "Fast block, absorbs melee impact, indestructible",
            gridWidth = 2,
            gridHeight = 3
        ),
        DiabloItem(
            name = "Holy Defender",
            baseType = "Large Shield",
            quality = ItemQuality.UNIQUE,
            armorClass = 15,
            fireResist = 20,
            extraDescription = "Fast block recovery, +20% fire protection",
            gridWidth = 2,
            gridHeight = 3
        ),

        // Jewelry
        DiabloItem(
            name = "Obsidian Ring of the Zodiac",
            baseType = "Ring",
            quality = ItemQuality.MAGIC,
            prefix = "Obsidian",
            suffix = "of the Zodiac",
            allAttributesBonus = 20,
            fireResist = 40,
            lightningResist = 40,
            magicResist = 40,
            extraDescription = "The Holy Grail of rings: +20 to All Stats, +40% All Resists",
            gridWidth = 1,
            gridHeight = 1
        ),
        DiabloItem(
            name = "Dragon's Ring of the Zodiac",
            baseType = "Ring",
            quality = ItemQuality.MAGIC,
            prefix = "Dragon's",
            suffix = "of the Zodiac",
            allAttributesBonus = 20,
            manaBonus = 60,
            extraDescription = "+20 to All Stats, +60 Mana Pool for casters",
            gridWidth = 1,
            gridHeight = 1
        ),
        DiabloItem(
            name = "Obsidian Amulet of the Zodiac",
            baseType = "Amulet",
            quality = ItemQuality.MAGIC,
            prefix = "Obsidian",
            suffix = "of the Zodiac",
            allAttributesBonus = 20,
            fireResist = 40,
            lightningResist = 40,
            magicResist = 40,
            extraDescription = "Legendary amulet: +20 to All Attributes, +40% All Resists",
            gridWidth = 1,
            gridHeight = 1
        ),
        DiabloItem(
            name = "Optic Amulet",
            baseType = "Amulet",
            quality = ItemQuality.UNIQUE,
            allAttributesBonus = 5,
            lightningResist = 20,
            extraDescription = "+20% Lightning resist, enhanced light radius",
            gridWidth = 1,
            gridHeight = 1
        ),

        // Consumables & Scrolls
        DiabloItem(
            name = "Elixir of Vitality",
            baseType = "Potion",
            quality = ItemQuality.NORMAL,
            extraDescription = "Permanently increases Vitality by 1 point",
            gridWidth = 1,
            gridHeight = 1
        ),
        DiabloItem(
            name = "Full Rejuvenation Potion",
            baseType = "Potion",
            quality = ItemQuality.NORMAL,
            extraDescription = "Instantly restores 100% of both Life and Mana pools",
            gridWidth = 1,
            gridHeight = 1
        ),
        DiabloItem(
            name = "Scroll of Apocalypse",
            baseType = "Scroll",
            quality = ItemQuality.NORMAL,
            extraDescription = "Unleashes ancient apocalyptic hellfire upon screen",
            gridWidth = 1,
            gridHeight = 1
        )
    )
}
