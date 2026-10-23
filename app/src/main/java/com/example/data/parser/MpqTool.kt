package com.example.data.parser

import java.nio.ByteBuffer
import java.nio.ByteOrder

data class MpqHeaderInfo(
    val signature: String,
    val headerSize: Int,
    val archiveSize: Int,
    val formatVersion: Int,
    val sectorSize: Int,
    val hashTableOffset: Long,
    val blockTableOffset: Long,
    val hashTableEntries: Int,
    val blockTableEntries: Int,
    val isValidMpq: Boolean,
    val detectedInternalFiles: List<String> = emptyList()
)

object MpqTool {
    const val MPQ_MAGIC = 0x1A51504D // "MPQ\x1a" in Little Endian

    fun analyzeMpqHeader(bytes: ByteArray): MpqHeaderInfo {
        if (bytes.size < 32) {
            return MpqHeaderInfo(
                signature = "Too small",
                headerSize = 0,
                archiveSize = bytes.size,
                formatVersion = 0,
                sectorSize = 0,
                hashTableOffset = 0,
                blockTableOffset = 0,
                hashTableEntries = 0,
                blockTableEntries = 0,
                isValidMpq = false
            )
        }

        val buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN)
        val magic = buffer.int

        if (magic != MPQ_MAGIC) {
            // Check if it's a raw hero file instead of full MPQ archive
            val isHero = isRawHeroFile(bytes)
            return MpqHeaderInfo(
                signature = if (isHero) "RAW_HERO" else "UNKNOWN",
                headerSize = 0,
                archiveSize = bytes.size,
                formatVersion = 0,
                sectorSize = 0,
                hashTableOffset = 0,
                blockTableOffset = 0,
                hashTableEntries = 0,
                blockTableEntries = 0,
                isValidMpq = false,
                detectedInternalFiles = if (isHero) listOf("hero") else emptyList()
            )
        }

        val headerSize = buffer.int
        val archiveSize = buffer.int
        val formatVersion = buffer.short.toInt()
        val sectorSizeShift = buffer.short.toInt()
        val sectorSize = 512 shl sectorSizeShift
        val hashTableOffset = buffer.int.toLong() and 0xFFFFFFFFL
        val blockTableOffset = buffer.int.toLong() and 0xFFFFFFFFL
        val hashTableEntries = buffer.int
        val blockTableEntries = buffer.int

        // Standard DevilutionX single-player archive files
        val detectedFiles = listOf("hero", "game", "quests")

        return MpqHeaderInfo(
            signature = "MPQ\\x1a",
            headerSize = headerSize,
            archiveSize = archiveSize,
            formatVersion = formatVersion,
            sectorSize = sectorSize,
            hashTableOffset = hashTableOffset,
            blockTableOffset = blockTableOffset,
            hashTableEntries = hashTableEntries,
            blockTableEntries = blockTableEntries,
            isValidMpq = true,
            detectedInternalFiles = detectedFiles
        )
    }

    private fun isRawHeroFile(bytes: ByteArray): Boolean {
        if (bytes.size < 64) return false
        // Diablo 1 hero name is first 16 bytes, ASCII null-terminated
        val nameBytes = bytes.copyOfRange(0, 16)
        val hasPrintableAscii = nameBytes[0].toInt().toChar().isLetterOrDigit()
        val classByte = bytes.getOrNull(16)?.toInt() ?: -1
        return hasPrintableAscii && classByte in 0..5
    }

    /**
     * Generates a hex dump string with 16 bytes per line and ASCII view.
     */
    fun formatHexDump(bytes: ByteArray, maxLines: Int = 16): String {
        val sb = StringBuilder()
        val totalBytes = minOf(bytes.size, maxLines * 16)

        for (i in 0 until totalBytes step 16) {
            val lineOffset = String.format("%04X  ", i)
            sb.append(lineOffset)

            val hexPart = StringBuilder()
            val asciiPart = StringBuilder()

            for (j in 0 until 16) {
                val idx = i + j
                if (idx < bytes.size) {
                    val b = bytes[idx].toInt() and 0xFF
                    hexPart.append(String.format("%02X ", b))
                    val c = b.toChar()
                    if (c in ' '..'~') {
                        asciiPart.append(c)
                    } else {
                        asciiPart.append('.')
                    }
                } else {
                    hexPart.append("   ")
                }
                if (j == 7) hexPart.append(" ")
            }

            sb.append(hexPart)
            sb.append(" |")
            sb.append(asciiPart)
            sb.append("|\n")
        }

        if (bytes.size > totalBytes) {
            sb.append("... (${bytes.size - totalBytes} more bytes)")
        }

        return sb.toString()
    }
}
