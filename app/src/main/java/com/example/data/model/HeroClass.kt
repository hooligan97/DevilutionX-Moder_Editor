package com.example.data.model

enum class HeroClass(
    val displayName: String,
    val title: String,
    val maxStrength: Int,
    val maxMagic: Int,
    val maxDexterity: Int,
    val maxVitality: Int,
    val baseStrength: Int,
    val baseMagic: Int,
    val baseDexterity: Int,
    val baseVitality: Int,
    val baseHp: Int,
    val baseMana: Int,
    val isHellfire: Boolean = false
) {
    WARRIOR(
        displayName = "Warrior",
        title = "Knight of Khanduras",
        maxStrength = 250,
        maxMagic = 50,
        maxDexterity = 60,
        maxVitality = 100,
        baseStrength = 30,
        baseMagic = 10,
        baseDexterity = 20,
        baseVitality = 25,
        baseHp = 70,
        baseMana = 10
    ),
    ROGUE(
        displayName = "Rogue",
        title = "Sightless Eye Archer",
        maxStrength = 55,
        maxMagic = 70,
        maxDexterity = 250,
        maxVitality = 80,
        baseStrength = 20,
        baseMagic = 15,
        baseDexterity = 30,
        baseVitality = 20,
        baseHp = 45,
        baseMana = 22
    ),
    SORCERER(
        displayName = "Sorcerer",
        title = "Vizjerei Mage",
        maxStrength = 45,
        maxMagic = 250,
        maxDexterity = 85,
        maxVitality = 80,
        baseStrength = 15,
        baseMagic = 35,
        baseDexterity = 15,
        baseVitality = 20,
        baseHp = 30,
        baseMana = 70
    ),
    MONK(
        displayName = "Monk",
        title = "Staff Master",
        maxStrength = 150,
        maxMagic = 80,
        maxDexterity = 150,
        maxVitality = 80,
        baseStrength = 25,
        baseMagic = 15,
        baseDexterity = 25,
        baseVitality = 20,
        baseHp = 50,
        baseMana = 30,
        isHellfire = true
    ),
    BARD(
        displayName = "Bard",
        title = "Dual Wield Jack-of-all-trades",
        maxStrength = 120,
        maxMagic = 120,
        maxDexterity = 120,
        maxVitality = 100,
        baseStrength = 20,
        baseMagic = 20,
        baseDexterity = 20,
        baseVitality = 20,
        baseHp = 45,
        baseMana = 35,
        isHellfire = true
    ),
    BARBARIAN(
        displayName = "Barbarian",
        title = "Northern Berserker",
        maxStrength = 270,
        maxMagic = 0,
        maxDexterity = 40,
        maxVitality = 150,
        baseStrength = 40,
        baseMagic = 0,
        baseDexterity = 15,
        baseVitality = 30,
        baseHp = 80,
        baseMana = 0,
        isHellfire = true
    );

    companion object {
        fun fromString(name: String): HeroClass {
            return entries.firstOrNull { it.name.equals(name, ignoreCase = true) || it.displayName.equals(name, ignoreCase = true) } ?: WARRIOR
        }

        fun fromIndex(index: Int): HeroClass {
            return when (index) {
                0 -> WARRIOR
                1 -> ROGUE
                2 -> SORCERER
                3 -> MONK
                4 -> BARD
                5 -> BARBARIAN
                else -> WARRIOR
            }
        }
    }
}
