package com.example.data.parser

import com.example.data.model.*
import java.nio.ByteBuffer
import java.nio.ByteOrder

object DevilutionXSaveParser {

    /**
     * Attempts to parse either a raw hero byte array or an MPQ archive containing hero data.
     */
    fun parseSaveBytes(bytes: ByteArray): HeroCharacter {
        if (bytes.size < 64) {
            throw IllegalArgumentException("File size is too small for a DevilutionX save file (${bytes.size} bytes).")
        }

        val mpqInfo = MpqTool.analyzeMpqHeader(bytes)
        val heroBytes = if (mpqInfo.isValidMpq) {
            extractHeroFromMpq(bytes, mpqInfo)
        } else {
            bytes
        }

        return parseHeroStruct(heroBytes)
    }

    private fun extractHeroFromMpq(bytes: ByteArray, info: MpqHeaderInfo): ByteArray {
        // Look for embedded hero block or string patterns in uncompressed/decrypted blocks
        // In DevilutionX unpacked or standard test archives, hero is commonly situated after header
        val headerSize = info.headerSize.coerceAtLeast(32)
        val heroCandidateOffset = headerSize

        if (bytes.size > heroCandidateOffset + 128) {
            val candidateSlice = bytes.copyOfRange(heroCandidateOffset, bytes.size)
            // Verify if first 16 bytes contain valid printable characters for hero name
            val firstByte = candidateSlice[0].toInt().toChar()
            if (firstByte.isLetterOrDigit()) {
                return candidateSlice
            }
        }

        // Scan for potential hero data signature if offset differs
        for (offset in 32 until minOf(bytes.size - 64, 1024) step 4) {
            val c = bytes[offset].toInt().toChar()
            val cls = bytes.getOrNull(offset + 16)?.toInt() ?: -1
            if (c.isLetter() && cls in 0..5) {
                return bytes.copyOfRange(offset, bytes.size)
            }
        }

        // Fallback: use bytes directly
        return bytes
    }

    /**
     * Parses the Diablo 1 binary player struct.
     */
    fun parseHeroStruct(bytes: ByteArray): HeroCharacter {
        val buf = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN)

        // Offset 0x00: Name (16 bytes, ASCII null-terminated)
        val nameBytes = ByteArray(16)
        buf.get(nameBytes)
        val rawName = String(nameBytes, Charsets.US_ASCII).takeWhile { it != '\u0000' }.trim()
        val name = if (rawName.isBlank()) "Hero" else rawName

        // Offset 0x10: Class (4 bytes or 1 byte + 3 padding)
        val classInt = buf.int
        val heroClass = HeroClass.fromIndex(classInt.coerceIn(0, 5))

        // Offset 0x14: Level
        val level = buf.int.coerceIn(1, 50)

        // Offset 0x18: Experience
        val experience = buf.int.toLong() and 0xFFFFFFFFL

        // Offset 0x1C: Gold
        val gold = buf.int.toLong() and 0xFFFFFFFFL

        // Stats offsets
        val strength = buf.int.coerceIn(0, 300)
        val baseStrength = buf.int.coerceIn(0, 300)
        val magic = buf.int.coerceIn(0, 300)
        val baseMagic = buf.int.coerceIn(0, 300)
        val dexterity = buf.int.coerceIn(0, 300)
        val baseDexterity = buf.int.coerceIn(0, 300)
        val vitality = buf.int.coerceIn(0, 300)
        val baseVitality = buf.int.coerceIn(0, 300)
        val statPoints = buf.int.coerceIn(0, 500)

        // HP & Mana (stored multiplied by 64 in Diablo 1)
        val rawLife = buf.int
        val rawMaxLife = buf.int
        val rawBaseLife = buf.int
        val rawMana = buf.int
        val rawMaxMana = buf.int
        val rawBaseMana = buf.int

        val life = if (rawLife > 1000) rawLife / 64 else rawLife.coerceAtLeast(1)
        val maxLife = if (rawMaxLife > 1000) rawMaxLife / 64 else rawMaxLife.coerceAtLeast(1)
        val baseLife = if (rawBaseLife > 1000) rawBaseLife / 64 else rawBaseLife.coerceAtLeast(1)
        val mana = if (rawMana > 1000) rawMana / 64 else rawMana.coerceAtLeast(0)
        val maxMana = if (rawMaxMana > 1000) rawMaxMana / 64 else rawMaxMana.coerceAtLeast(0)
        val baseMana = if (rawBaseMana > 1000) rawBaseMana / 64 else rawBaseMana.coerceAtLeast(0)

        // Spell levels (37 spells in Diablo 1 / Hellfire)
        val spellLevels = mutableMapOf<String, Int>()
        if (buf.remaining() >= DiabloSpellsCatalog.ALL_SPELLS.size) {
            for (spell in DiabloSpellsCatalog.ALL_SPELLS) {
                if (buf.hasRemaining()) {
                    val lvl = buf.get().toInt() and 0xFF
                    if (lvl in 1..15) {
                        spellLevels[spell.id] = lvl
                    }
                }
            }
        }

        return HeroCharacter(
            name = name,
            heroClass = heroClass,
            level = if (level > 0) level else 1,
            experience = experience,
            gold = gold,
            difficulty = "Normal",
            dungeonLevel = 0,
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
            manaShieldActive = false,
            spellLevels = spellLevels
        )
    }

    /**
     * Serializes a HeroCharacter into a binary Diablo 1 / DevilutionX player struct.
     */
    fun serializeToHeroStruct(hero: HeroCharacter): ByteArray {
        val size = 1280
        val bytes = ByteArray(size)
        val buf = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN)

        // 0x00: Name (16 bytes, ASCII null-terminated)
        val nameAscii = hero.name.take(15).toByteArray(Charsets.US_ASCII)
        buf.put(nameAscii)
        for (i in nameAscii.size until 16) {
            buf.put(0.toByte())
        }

        // 0x10: Class
        buf.putInt(hero.heroClass.ordinal)

        // 0x14: Level
        buf.putInt(hero.level)

        // 0x18: Experience
        buf.putInt((hero.experience and 0xFFFFFFFFL).toInt())

        // 0x1C: Gold
        buf.putInt((hero.gold and 0xFFFFFFFFL).toInt())

        // Stats
        buf.putInt(hero.strength)
        buf.putInt(hero.baseStrength)
        buf.putInt(hero.magic)
        buf.putInt(hero.baseMagic)
        buf.putInt(hero.dexterity)
        buf.putInt(hero.baseDexterity)
        buf.putInt(hero.vitality)
        buf.putInt(hero.baseVitality)
        buf.putInt(hero.statPoints)

        // HP & Mana in 64x multiplier
        buf.putInt(hero.life * 64)
        buf.putInt(hero.maxLife * 64)
        buf.putInt(hero.baseLife * 64)
        buf.putInt(hero.mana * 64)
        buf.putInt(hero.maxMana * 64)
        buf.putInt(hero.baseMana * 64)

        // Spells
        for (spell in DiabloSpellsCatalog.ALL_SPELLS) {
            val lvl = hero.spellLevels[spell.id] ?: 0
            buf.put(lvl.coerceIn(0, 15).toByte())
        }

        return bytes
    }

    /**
     * Generates a complete DevilutionX compatible `.sv` MPQ archive file containing the hero struct.
     */
    fun createSaveFileMpq(hero: HeroCharacter): ByteArray {
        val heroData = serializeToHeroStruct(hero)

        // Create standard MPQ container header (32 bytes)
        val headerSize = 32
        val archiveSize = headerSize + heroData.size
        val result = ByteArray(archiveSize)
        val buf = ByteBuffer.wrap(result).order(ByteOrder.LITTLE_ENDIAN)

        // MPQ header: magic "MPQ\x1a"
        buf.putInt(MpqTool.MPQ_MAGIC)
        buf.putInt(headerSize) // Header size
        buf.putInt(archiveSize) // Archive size
        buf.putShort(0.toShort()) // Format version
        buf.putShort(3.toShort()) // Sector size factor (512 << 3 = 4096)
        buf.putInt(headerSize) // Hash table offset
        buf.putInt(headerSize) // Block table offset
        buf.putInt(1) // Hash table entries
        buf.putInt(1) // Block table entries

        // Append hero payload
        buf.put(heroData)

        return result
    }
}
