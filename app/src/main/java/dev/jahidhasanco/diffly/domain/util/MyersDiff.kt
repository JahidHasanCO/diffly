package dev.jahidhasanco.diffly.domain.util

import dev.jahidhasanco.diffly.domain.model.DiffEntry
import dev.jahidhasanco.diffly.domain.model.DiffType


class MyersDiff {

    private val charLevelDiff = CharLevelDiff()

    fun calculateDiff(
        oldLines: List<String>,
        newLines: List<String>
    ): List<DiffEntry> {
        val result = mutableListOf<DiffEntry>()
        val maxLines = maxOf(oldLines.size, newLines.size)

        for (i in 0 until maxLines) {
            val oldLine = oldLines.getOrNull(i)
            val newLine = newLines.getOrNull(i)

            val entry = when {
                oldLine == null && newLine != null -> {
                    DiffEntry(
                        oldLine = null,
                        newLine = newLine,
                        type = DiffType.ADDED
                    )
                }

                oldLine != null && newLine == null -> {
                    DiffEntry(
                        oldLine = oldLine,
                        newLine = null,
                        type = DiffType.DELETED
                    )
                }

                oldLine == newLine -> {
                    DiffEntry(
                        oldLine = oldLine,
                        newLine = newLine,
                        type = DiffType.UNCHANGED
                    )
                }

                else -> {
                    // Lines differ: compute char-level diff
                    val charDiffs = charLevelDiff.diff(oldLine!!, newLine!!)
                    DiffEntry(
                        oldLine = oldLine,
                        newLine = newLine,
                        type = DiffType.CHANGED,
                        charDiffs = charDiffs
                    )
                }
            }

            result.add(entry)
        }

        return result
    }

}
