package dev.jahidhasanco.diffly.domain.util

import dev.jahidhasanco.diffly.domain.model.DiffEntry
import dev.jahidhasanco.diffly.domain.model.DiffType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MyersDiffTest {

    private val myersDiff = MyersDiff()

    @Test
    fun `calculateDiff with empty oldLines and empty newLines`() {
        val result = myersDiff.calculateDiff(emptyList(), emptyList())
        assertTrue(result.isEmpty())
    }

    @Test
    fun `calculateDiff with empty oldLines and non empty newLines`() {
        val newLines = listOf("A", "B")
        val result = myersDiff.calculateDiff(emptyList(), newLines)
        assertEquals(2, result.size)
        assertTrue(result.all { it.type == DiffType.ADDED })
    }

    @Test
    fun `calculateDiff with non empty oldLines and empty newLines`() {
        val oldLines = listOf("A", "B")
        val result = myersDiff.calculateDiff(oldLines, emptyList())
        assertEquals(2, result.size)
        assertTrue(result.all { it.type == DiffType.DELETED })
    }

    @Test
    fun `calculateDiff with identical oldLines and newLines`() {
        val lines = listOf("X", "Y", "Z")
        val result = myersDiff.calculateDiff(lines, lines)
        assertEquals(3, result.size)
        assertTrue(result.all { it.type == DiffType.UNCHANGED })
    }

    @Test
    fun `calculateDiff with all lines added at the beginning`() {
        val oldLines = listOf("C", "D")
        val newLines = listOf("A", "B") + oldLines
        val result = myersDiff.calculateDiff(oldLines, newLines)
        assertEquals(4, result.size)
        assertEquals(DiffType.ADDED, result[0].type)
        assertEquals(DiffType.ADDED, result[1].type)
        assertEquals(DiffType.UNCHANGED, result[2].type)
        assertEquals(DiffType.UNCHANGED, result[3].type)
    }

    @Test
    fun `calculateDiff with all lines added at the end`() {
        val oldLines = listOf("A", "B")
        val newLines = oldLines + listOf("C", "D")
        val result = myersDiff.calculateDiff(oldLines, newLines)
        assertEquals(4, result.size)
        assertEquals(DiffType.UNCHANGED, result[0].type)
        assertEquals(DiffType.UNCHANGED, result[1].type)
        assertEquals(DiffType.ADDED, result[2].type)
        assertEquals(DiffType.ADDED, result[3].type)
    }

    @Test
    fun `calculateDiff with all lines added in the middle`() {
        val oldLines = listOf("A", "D")
        val newLines = listOf("A", "B", "C", "D")
        val result = myersDiff.calculateDiff(oldLines, newLines)
        assertEquals(4, result.size)
        assertEquals(DiffType.UNCHANGED, result[0].type)
        assertEquals(DiffType.ADDED, result[1].type)
        assertEquals(DiffType.ADDED, result[2].type)
        assertEquals(DiffType.UNCHANGED, result[3].type)
    }

    @Test
    fun `calculateDiff with all lines deleted from the beginning`() {
        val oldLines = listOf("A", "B", "C", "D")
        val newLines = listOf("C", "D")
        val result = myersDiff.calculateDiff(oldLines, newLines)
        assertEquals(4, result.size)
        assertEquals(DiffType.DELETED, result[0].type)
        assertEquals(DiffType.DELETED, result[1].type)
        assertEquals(DiffType.UNCHANGED, result[2].type)
        assertEquals(DiffType.UNCHANGED, result[3].type)
    }

    @Test
    fun `calculateDiff with all lines deleted from the end`() {
        val oldLines = listOf("A", "B", "C", "D")
        val newLines = listOf("A", "B")
        val result = myersDiff.calculateDiff(oldLines, newLines)
        assertEquals(4, result.size)
        assertEquals(DiffType.UNCHANGED, result[0].type)
        assertEquals(DiffType.UNCHANGED, result[1].type)
        assertEquals(DiffType.DELETED, result[2].type)
        assertEquals(DiffType.DELETED, result[3].type)
    }

    @Test
    fun `calculateDiff with all lines deleted from the middle`() {
        val oldLines = listOf("A", "B", "C", "D")
        val newLines = listOf("A", "D")
        val result = myersDiff.calculateDiff(oldLines, newLines)
        assertEquals(4, result.size)
        assertEquals(DiffType.UNCHANGED, result[0].type)
        assertEquals(DiffType.DELETED, result[1].type)
        assertEquals(DiffType.DELETED, result[2].type)
        assertEquals(DiffType.UNCHANGED, result[3].type)
    }

    @Test
    fun `calculateDiff with all lines changed`() {
        val entries = listOf(
            DiffEntry("A", null, DiffType.DELETED),
            DiffEntry(null, "X", DiffType.ADDED),
            DiffEntry("B", null, DiffType.DELETED),
            DiffEntry(null, "Y", DiffType.ADDED),
        )
        val merged = myersDiff.mergeChangedEntries(entries)
        assertEquals(2, merged.size)
    }


    @Test
    fun `calculateDiff with a mix of added deleted and unchanged lines`() {
        val oldLines = listOf("A", "B", "D")
        val newLines = listOf("A", "C", "D")
        val result = myersDiff.calculateDiff(oldLines, newLines)
        assertEquals(3, result.size)
        assertEquals(DiffType.UNCHANGED, result[0].type)
        assertEquals(DiffType.CHANGED, result[1].type)
        assertEquals(DiffType.UNCHANGED, result[2].type)
    }

    @Test
    fun `calculateDiff with lines containing special characters`() {
        val oldLines = listOf("A@", "B€")
        val newLines = listOf("A@", "B£")
        val result = myersDiff.calculateDiff(oldLines, newLines)
        assertEquals(2, result.size) // 1 unchanged, 1 changed

        assertEquals(DiffType.UNCHANGED, result[0].type)
        assertEquals(DiffType.CHANGED, result[1].type)
    }


    @Test
    fun `calculateDiff with lines containing only whitespace`() {
        val oldLines = listOf(" ", "\t")
        val newLines = listOf("\t", " ")
        val rawResult = myersDiff.calculateDiff(oldLines, newLines)
        val mergedResult = myersDiff.mergeChangedEntries(rawResult)

        println("Raw Diff:")
        rawResult.forEachIndexed { i, entry ->
            println("$i: ${entry.type} oldLine='${entry.oldLine}' newLine='${entry.newLine}'")
        }


        println("Merged Diff:")
        mergedResult.forEachIndexed { i, entry ->
            println("$i: ${entry.type} oldLine='${entry.oldLine}' newLine='${entry.newLine}'")
        }

    }

    @Test
    fun `calculateDiff with very long lines`() {
        val longLine = "A".repeat(10_000)
        val result = myersDiff.calculateDiff(listOf(longLine), listOf(longLine))
        assertEquals(1, result.size)
        assertEquals(DiffType.UNCHANGED, result[0].type)
    }

    @Test
    fun `calculateDiff with large number of lines`() {
        val oldLines = List(1000) { "A$it" }
        val newLines = List(1000) { "A$it" }
        val result = myersDiff.calculateDiff(oldLines, newLines)
        assertEquals(1000, result.size)
        assertTrue(result.all { it.type == DiffType.UNCHANGED })
    }

    @Test
    fun `calculateDiff where newLines is a subset of oldLines deletion only`() {
        val oldLines = listOf("A", "B", "C")
        val newLines = listOf("A", "C")
        val result = myersDiff.calculateDiff(oldLines, newLines)
        assertEquals(3, result.size)
        assertEquals(DiffType.UNCHANGED, result[0].type)
        assertEquals(DiffType.DELETED, result[1].type)
        assertEquals(DiffType.UNCHANGED, result[2].type)
    }

    @Test
    fun `calculateDiff where oldLines is a subset of newLines addition only`() {
        val oldLines = listOf("A", "C")
        val newLines = listOf("A", "B", "C")
        val result = myersDiff.calculateDiff(oldLines, newLines)
        assertEquals(3, result.size)
        assertEquals(DiffType.UNCHANGED, result[0].type)
        assertEquals(DiffType.ADDED, result[1].type)
        assertEquals(DiffType.UNCHANGED, result[2].type)
    }

    @Test
    fun `calculateDiff with swapped lines`() {
        val oldLines = listOf("A", "B")
        val newLines = listOf("B", "A")
        val result = myersDiff.calculateDiff(oldLines, newLines)
        assertEquals(3, result.size)
        assertEquals(DiffType.DELETED, result[0].type)
        assertEquals(DiffType.UNCHANGED, result[1].type)
        assertEquals(DiffType.ADDED, result[2].type)

    }

    @Test
    fun `calculateDiff with duplicate lines in input`() {
        val oldLines = listOf("A", "A", "B")
        val newLines = listOf("A", "B", "B")
        val result = myersDiff.calculateDiff(oldLines, newLines)
        assertTrue(result.isNotEmpty())
    }

    @Test
    fun `calculateDiff where buildDiff trace is complex`() {
        val oldLines = listOf("A", "B", "C", "D", "E")
        val newLines = listOf("A", "X", "C", "Y", "E")
        val result = myersDiff.calculateDiff(oldLines, newLines)
        assertTrue(result.isNotEmpty())

        // After merging, replaced lines are marked as CHANGED
        assertEquals(DiffType.UNCHANGED, result[0].type)
        assertEquals(DiffType.CHANGED, result[1].type)  // changed from ADDED to CHANGED
        assertEquals(DiffType.UNCHANGED, result[2].type)
        assertEquals(DiffType.CHANGED, result[3].type)  // changed from ADDED to CHANGED
        assertEquals(DiffType.UNCHANGED, result[4].type)
    }


    @Test
    fun `calculateDiff ensuring charLevelDiff is called for changed lines`() {
        val diff = MyersDiff()
        val oldLines = listOf("hello")
        val newLines = listOf("hallo")
        val result = diff.calculateDiff(oldLines, newLines)
        val changedEntry = result.find { it.type == DiffType.CHANGED }
        assertTrue(changedEntry != null)
        assertTrue(!changedEntry.charDiffs.isNullOrEmpty())
    }

    @Test
    fun `mergeChangedEntries with empty list`() {
        val result = myersDiff.mergeChangedEntries(emptyList())
        assertTrue(result.isEmpty())
    }

    @Test
    fun `mergeChangedEntries with no DELETED ADDED pairs`() {
        val entries = listOf(
            DiffEntry("A", "A", DiffType.UNCHANGED),
            DiffEntry("B", null, DiffType.DELETED),
            DiffEntry(null, "C", DiffType.ADDED),
            DiffEntry("D", "D", DiffType.UNCHANGED)
        )
        val result = myersDiff.mergeChangedEntries(entries)
        // Since DELETED and ADDED are adjacent, they will be merged,
        // So expected size is 3, not 4.
        assertEquals(3, result.size)

        // Optional: Check merged entry is CHANGED
        assertEquals(DiffType.CHANGED, result[1].type)
        assertEquals("B", result[1].oldLine)
        assertEquals("C", result[1].newLine)
    }


    @Test
    fun `mergeChangedEntries with a single DELETED ADDED pair at the beginning`() {
        val entries = listOf(
            DiffEntry("old", null, DiffType.DELETED),
            DiffEntry(null, "new", DiffType.ADDED),
            DiffEntry("X", "X", DiffType.UNCHANGED)
        )
        val result = myersDiff.mergeChangedEntries(entries)
        assertEquals(2, result.size)
        assertEquals(DiffType.CHANGED, result[0].type)
    }

    @Test
    fun `mergeChangedEntries with a single DELETED ADDED pair at the end`() {
        val entries = listOf(
            DiffEntry("X", "X", DiffType.UNCHANGED),
            DiffEntry("old", null, DiffType.DELETED),
            DiffEntry(null, "new", DiffType.ADDED)
        )
        val result = myersDiff.mergeChangedEntries(entries)
        assertEquals(2, result.size)
        assertEquals(DiffType.CHANGED, result[1].type)
    }

    @Test
    fun `mergeChangedEntries with a single DELETED ADDED pair in the middle`() {
        val entries = listOf(
            DiffEntry("A", "A", DiffType.UNCHANGED),
            DiffEntry("old", null, DiffType.DELETED),
            DiffEntry(null, "new", DiffType.ADDED),
            DiffEntry("B", "B", DiffType.UNCHANGED)
        )
        val result = myersDiff.mergeChangedEntries(entries)
        assertEquals(3, result.size)
        assertEquals(DiffType.CHANGED, result[1].type)
    }

    @Test
    fun `mergeChangedEntries with multiple DELETED ADDED pairs`() {
        val entries = listOf(
            DiffEntry("a", null, DiffType.DELETED),
            DiffEntry(null, "x", DiffType.ADDED),
            DiffEntry("b", null, DiffType.DELETED),
            DiffEntry(null, "y", DiffType.ADDED)
        )
        val result = myersDiff.mergeChangedEntries(entries)
        assertEquals(2, result.size)
        assertTrue(result.all { it.type == DiffType.CHANGED })
    }

    @Test
    fun `mergeChangedEntries with DELETED followed by UNCHANGED then ADDED`() {
        val entries = listOf(
            DiffEntry("old", null, DiffType.DELETED),
            DiffEntry("mid", "mid", DiffType.UNCHANGED),
            DiffEntry(null, "new", DiffType.ADDED)
        )
        val result = myersDiff.mergeChangedEntries(entries)
        assertEquals(entries.size, result.size)
    }

    @Test
    fun `mergeChangedEntries with ADDED followed by DELETED`() {
        val entries = listOf(
            DiffEntry(null, "new", DiffType.ADDED),
            DiffEntry("old", null, DiffType.DELETED)
        )
        val result = myersDiff.mergeChangedEntries(entries)
        assertEquals(entries.size, result.size)
    }

    @Test
    fun `mergeChangedEntries with only DELETED entries`() {
        val entries = listOf(
            DiffEntry("a", null, DiffType.DELETED),
            DiffEntry("b", null, DiffType.DELETED)
        )
        val result = myersDiff.mergeChangedEntries(entries)
        assertEquals(entries.size, result.size)
    }

    @Test
    fun `mergeChangedEntries with only ADDED entries`() {
        val entries = listOf(
            DiffEntry(null, "x", DiffType.ADDED),
            DiffEntry(null, "y", DiffType.ADDED)
        )
        val result = myersDiff.mergeChangedEntries(entries)
        assertEquals(entries.size, result.size)
    }

    @Test
    fun `mergeChangedEntries with DELETED entry having null oldLine should not happen from calculateDiff`() {
        val entries = listOf(
            DiffEntry(null, null, DiffType.DELETED)
        )
        val result = myersDiff.mergeChangedEntries(entries)
        assertEquals(1, result.size)
        assertEquals("", result[0].oldLine ?: "")
    }

    @Test
    fun `mergeChangedEntries with ADDED entry having null newLine should not happen from calculateDiff`() {
        val entries = listOf(
            DiffEntry(null, null, DiffType.ADDED)
        )
        val result = myersDiff.mergeChangedEntries(entries)
        assertEquals(1, result.size)
        assertEquals("", result[0].newLine ?: "")
    }

    @Test
    fun `mergeChangedEntries correctly populates charDiffs for merged CHANGED entries`() {
        val entries = listOf(
            DiffEntry("abc", null, DiffType.DELETED),
            DiffEntry(null, "axc", DiffType.ADDED)
        )
        val result = myersDiff.mergeChangedEntries(entries)
        val changedEntry = result.first()
        assertEquals(DiffType.CHANGED, changedEntry.type)
        assertTrue(!changedEntry.charDiffs.isNullOrEmpty())
    }

    @Test
    fun `mergeChangedEntries handles DELETED at the very end of the list`() {
        val entries = listOf(
            DiffEntry("A", "A", DiffType.UNCHANGED),
            DiffEntry("B", null, DiffType.DELETED)
        )
        val result = myersDiff.mergeChangedEntries(entries)
        assertEquals(2, result.size)
        assertEquals(DiffType.DELETED, result.last().type)
    }


}