package com.example

import com.example.data.model.HeroCharacter
import com.example.data.model.HeroClass
import com.example.data.parser.DevilutionXSaveParser
import com.example.data.parser.MpqTool
import com.example.data.parser.NoctonicJsonParser
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class DevilutionXEditorTest {

    @Test
    fun testLevelAndXpProgression() {
        val level1Xp = HeroCharacter.xpForLevel(1)
        assertEquals(0L, level1Xp)

        val level50Xp = HeroCharacter.xpForLevel(50)
        assertEquals(1413867624L, level50Xp)

        assertEquals(1, HeroCharacter.levelForXp(0L))
        assertEquals(50, HeroCharacter.levelForXp(1413867624L))
        assertEquals(2, HeroCharacter.levelForXp(2500L))
    }

    @Test
    fun testNoctonicJsonRoundTrip() {
        val hero = HeroCharacter.createGodly(HeroClass.SORCERER, "MageGod")
        val json = NoctonicJsonParser.toJson(hero)

        assertTrue(json.contains("\"name\": \"MageGod\""))
        assertTrue(json.contains("\"class\": \"Sorcerer\""))
        assertTrue(json.contains("\"level\": 50"))

        val parsed = NoctonicJsonParser.fromJson(json)
        assertEquals("MageGod", parsed.name)
        assertEquals(HeroClass.SORCERER, parsed.heroClass)
        assertEquals(50, parsed.level)
        assertEquals(hero.strength, parsed.strength)
        assertEquals(hero.magic, parsed.magic)
    }

    @Test
    fun testDevilutionXSaveStructSerialization() {
        val warrior = HeroCharacter.createGodly(HeroClass.WARRIOR, "Aidan")
        val bytes = DevilutionXSaveParser.serializeToHeroStruct(warrior)

        assertEquals(1280, bytes.size)

        val parsed = DevilutionXSaveParser.parseHeroStruct(bytes)
        assertEquals("Aidan", parsed.name)
        assertEquals(HeroClass.WARRIOR, parsed.heroClass)
        assertEquals(50, parsed.level)
        assertEquals(warrior.strength, parsed.strength)
    }

    @Test
    fun testMpqHeaderGenerationAndAnalysis() {
        val hero = HeroCharacter.createGodly(HeroClass.ROGUE, "Moreina")
        val mpqArchive = DevilutionXSaveParser.createSaveFileMpq(hero)

        assertTrue(mpqArchive.size > 32)
        val info = MpqTool.analyzeMpqHeader(mpqArchive)

        assertTrue(info.isValidMpq)
        assertEquals("MPQ\\x1a", info.signature)
        assertEquals(32, info.headerSize)
        assertTrue(info.archiveSize > 0)
    }
}
