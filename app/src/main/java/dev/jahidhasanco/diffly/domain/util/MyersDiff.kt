package dev.jahidhasanco.diffly.domain.util

import dev.jahidhasanco.diffly.domain.model.DiffEntry
import dev.jahidhasanco.diffly.domain.model.DiffType


class MyersDiff {

    private val charLevelDiff = CharLevelDiff()

    fun calculateDiff(oldLines: List<String>, newLines: List<String>): List<DiffEntry> {
        val n = oldLines.size
        val m = newLines.size
        val max = n + m
        val v = mutableMapOf<Int, Int>()
        v[1] = 0
        val trace = mutableListOf<Map<Int, Int>>()

        for (d in 0..max) {
            val vCopy = v.toMutableMap()
            trace.add(vCopy)

            for (k in -d..d step 2) {
                val x = when {
                    k == -d -> v[k + 1]!!
                    k != d && v[k - 1]!! < v[k + 1]!! -> v[k + 1]!!
                    else -> v[k - 1]!! + 1
                }

                var y = x - k
                var xCopy = x

                while (xCopy < n && y < m && oldLines[xCopy] == newLines[y]) {
                    xCopy++
                    y++
                }

                v[k] = xCopy

                if (xCopy >= n && y >= m) {
                    val rawDiff = buildDiff(trace, oldLines, newLines)
                    return mergeChangedEntries(rawDiff)
                }
            }
        }

        throw IllegalStateException("Diff computation failed")
    }

    private fun buildDiff(
        trace: List<Map<Int, Int>>,
        oldLines: List<String>,
        newLines: List<String>
    ): List<DiffEntry> {
        val result = mutableListOf<DiffEntry>()
        var x = oldLines.size
        var y = newLines.size

        for (d in trace.size - 1 downTo 0) {
            val v = trace[d]
            val k = x - y
            val prevK = when {
                k == -d || (k != d && v[k - 1]!! < v[k + 1]!!) -> k + 1
                else -> k - 1
            }
            val prevX = v[prevK]!!
            val prevY = prevX - prevK

            // Handle unchanged lines
            while (x > prevX && y > prevY) {
                result.add(
                    DiffEntry(
                        oldLine = oldLines[x - 1],
                        newLine = newLines[y - 1],
                        type = DiffType.UNCHANGED,
                        charDiffs = null
                    )
                )
                x--
                y--
            }

            // Handle inserted or deleted lines (only if d > 0)
            if (d > 0) {
                if (x == prevX) {
                    // Inserted line(s)
                    result.add(
                        DiffEntry(
                            oldLine = null,
                            newLine = newLines[y - 1],
                            type = DiffType.ADDED,
                            charDiffs = null
                        )
                    )
                    y--
                } else if (y == prevY) {
                    // Deleted line(s)
                    result.add(
                        DiffEntry(
                            oldLine = oldLines[x - 1],
                            newLine = null,
                            type = DiffType.DELETED,
                            charDiffs = null
                        )
                    )
                    x--
                } else {
                    // Changed line: Both old and new lines exist but differ
                    val oldLine = oldLines[x - 1]
                    val newLine = newLines[y - 1]

                    val charDiffs = charLevelDiff.diff(oldLine, newLine)

                    result.add(
                        DiffEntry(
                            oldLine = oldLine,
                            newLine = newLine,
                            type = DiffType.CHANGED,
                            charDiffs = charDiffs
                        )
                    )
                    x--
                    y--
                }
            }
        }

        return result.reversed()
    }

    fun mergeChangedEntries(diffEntries: List<DiffEntry>): List<DiffEntry> {
        val merged = mutableListOf<DiffEntry>()
        var i = 0
        while (i < diffEntries.size) {
            val current = diffEntries[i]

            // Check if next entry is an inserted line
            if (current.type == DiffType.DELETED && i + 1 < diffEntries.size) {
                val next = diffEntries[i + 1]
                if (next.type == DiffType.ADDED) {
                    // Merge as changed line with char diff
                    val oldLine = current.oldLine ?: ""
                    val newLine = next.newLine ?: ""
                    val charDiffs = CharLevelDiff().diff(oldLine, newLine)

                    merged.add(
                        DiffEntry(
                            oldLine = oldLine,
                            newLine = newLine,
                            type = DiffType.CHANGED,
                            charDiffs = charDiffs
                        )
                    )
                    i += 2
                    continue
                }
            }
            // Otherwise add as-is
            merged.add(current)
            i++
        }
        return merged
    }

}
