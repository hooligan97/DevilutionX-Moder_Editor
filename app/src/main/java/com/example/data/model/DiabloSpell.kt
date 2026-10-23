package com.example.data.model

enum class SpellSchool(val displayName: String) {
    FIRE("Fire Magic"),
    LIGHTNING("Lightning Magic"),
    ARCANE("Arcane & Utility"),
    FORBIDDEN("Forbidden Arts"),
    HELLFIRE("Hellfire Spells")
}

data class DiabloSpell(
    val id: String,
    val name: String,
    val school: SpellSchool,
    val minLevel: Int = 1,
    val maxLevel: Int = 15,
    val manaCost: String,
    val description: String
)

object DiabloSpellsCatalog {
    val ALL_SPELLS = listOf(
        // Fire
        DiabloSpell("firebolt", "Firebolt", SpellSchool.FIRE, 1, 15, "6", "Fires a concentrated bolt of flame at target."),
        DiabloSpell("fireball", "Fireball", SpellSchool.FIRE, 1, 15, "16", "Explodes into a fiery blast dealing heavy splash damage."),
        DiabloSpell("firewall", "Fire Wall", SpellSchool.FIRE, 1, 15, "28", "Creates an incinerating barrier that consumes all who pass."),
        DiabloSpell("inferno", "Inferno", SpellSchool.FIRE, 1, 15, "19", "Streams continuous gouts of devastating flame."),
        DiabloSpell("flame_wave", "Flame Wave", SpellSchool.FIRE, 1, 15, "35", "Sends a sweeping wall of fire across the battlefield."),
        DiabloSpell("guardian", "Guardian", SpellSchool.FIRE, 1, 15, "50", "Summons a three-headed dragon from the nether realms."),
        DiabloSpell("apocalypse", "Apocalypse", SpellSchool.FIRE, 1, 15, "60", "Calls celestial fire raining destruction upon all enemies."),

        // Lightning
        DiabloSpell("charged_bolt", "Charged Bolt", SpellSchool.LIGHTNING, 1, 15, "6", "Releases erratically dancing electric sparks."),
        DiabloSpell("lightning", "Lightning", SpellSchool.LIGHTNING, 1, 15, "19", "Strikes enemies with a straight discharge of high-voltage lightning."),
        DiabloSpell("chain_lightning", "Chain Lightning", SpellSchool.LIGHTNING, 1, 15, "30", "Arcs multiple lethal electrical branches jumping between foes."),
        DiabloSpell("nova", "Nova", SpellSchool.LIGHTNING, 1, 15, "60", "Expands a deadly 360-degree shockwave ring of electrical destruction."),
        DiabloSpell("flash", "Flash", SpellSchool.LIGHTNING, 1, 15, "30", "Emits an instantaneous close-quarters burst of blinding shock."),

        // Arcane & Utility
        DiabloSpell("healing", "Healing", SpellSchool.ARCANE, 1, 15, "8", "Channels divine energy to restore the caster's lost hit points."),
        DiabloSpell("heal_other", "Heal Other", SpellSchool.ARCANE, 1, 15, "8", "Directs restorative holy energy to mend wounded companions."),
        DiabloSpell("town_portal", "Town Portal", SpellSchool.ARCANE, 1, 15, "35", "Opens a mystical rift directly back to the town of Tristram."),
        DiabloSpell("teleport", "Teleport", SpellSchool.ARCANE, 1, 15, "35", "Bends space to instantaneously relocate to target destination."),
        DiabloSpell("mana_shield", "Mana Shield", SpellSchool.ARCANE, 1, 15, "33", "Envelops caster in an arcane aegis, redirecting physical damage to mana."),
        DiabloSpell("phasing", "Phasing", SpellSchool.ARCANE, 1, 15, "25", "Temporarily blinks through the veil to an unpredictable nearby location."),
        DiabloSpell("stone_curse", "Stone Curse", SpellSchool.ARCANE, 1, 15, "40", "Transmutes demonic flesh into motionless, brittle solid stone."),
        DiabloSpell("identify", "Identify", SpellSchool.ARCANE, 1, 15, "1", "Deciphers ancient runic enchantments on mysterious magical items."),
        DiabloSpell("infravision", "Infravision", SpellSchool.ARCANE, 1, 15, "41", "Allows supernatural perception through dark dungeon corridors."),

        // Forbidden
        DiabloSpell("holy_bolt", "Holy Bolt", SpellSchool.FORBIDDEN, 1, 15, "7", "Dispatches searing sanctified bolts that obliterate undead fiends."),
        DiabloSpell("blood_star", "Blood Star", SpellSchool.FORBIDDEN, 1, 15, "14", "Launches orbs of distilled vitality that penetrate demonic armor."),
        DiabloSpell("bone_spirit", "Bone Spirit", SpellSchool.FORBIDDEN, 1, 15, "24", "Unleashes a hungry vengeful spectre that drains 1/3 of enemy life."),
        DiabloSpell("golem", "Golem", SpellSchool.FORBIDDEN, 1, 15, "100", "Forges an enduring animated clay monstrosity to crush adversaries."),

        // Hellfire
        DiabloSpell("ring_of_fire", "Ring of Fire", SpellSchool.HELLFIRE, 1, 15, "40", "Erects a protective concentric ring of intense demonic flame."),
        DiabloSpell("lightning_wall", "Lightning Wall", SpellSchool.HELLFIRE, 1, 15, "45", "Forms a shimmering barrier of continuous high-voltage crackle."),
        DiabloSpell("warp", "Warp", SpellSchool.HELLFIRE, 1, 15, "25", "Warps through the ethereal labyrinth to a random nearby room."),
        DiabloSpell("reflect", "Reflect", SpellSchool.HELLFIRE, 1, 15, "45", "Rebounds incoming ranged enemy missiles back toward attackers."),
        DiabloSpell("berserk", "Berserk", SpellSchool.HELLFIRE, 1, 15, "50", "Drives lesser monsters into blind homicidal fury against each other."),
        DiabloSpell("search", "Search", SpellSchool.HELLFIRE, 1, 15, "5", "Illuminates hidden treasures and items dropped on dungeon floors."),
        DiabloSpell("jester", "Jester", SpellSchool.HELLFIRE, 1, 15, "30", "Casts a chaotic random spell effect with unpredictable consequences.")
    )
}
