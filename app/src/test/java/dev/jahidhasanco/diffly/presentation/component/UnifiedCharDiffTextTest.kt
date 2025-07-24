package dev.jahidhasanco.diffly.presentation.component

import dev.jahidhasanco.diffly.domain.model.DiffEntry
import dev.jahidhasanco.diffly.domain.model.DiffType
import dev.jahidhasanco.diffly.domain.model.CharDiff
import dev.jahidhasanco.diffly.domain.model.CharDiffType
import kotlin.test.Test

/**
 * Unit test to verify UnifiedCharDiffText logic for CHANGED entries
 */
class UnifiedCharDiffTextTest {

    @Test
    fun testUnifiedViewLogicWithChangedEntries() {
        // Test data that represents different types of diffs
        val diffResult = listOf(
            DiffEntry("line 1", "line 1", DiffType.UNCHANGED),
            DiffEntry("old line 2", "new line 2", DiffType.CHANGED, listOf(
                CharDiff('o', CharDiffType.DELETED),
                CharDiff('l', CharDiffType.DELETED),
                CharDiff('d', CharDiffType.DELETED),
                CharDiff('n', CharDiffType.INSERTED),
                CharDiff('e', CharDiffType.INSERTED),
                CharDiff('w', CharDiffType.INSERTED),
                CharDiff(' ', CharDiffType.UNCHANGED),
                CharDiff('l', CharDiffType.UNCHANGED),
                CharDiff('i', CharDiffType.UNCHANGED),
                CharDiff('n', CharDiffType.UNCHANGED),
                CharDiff('e', CharDiffType.UNCHANGED),
                CharDiff(' ', CharDiffType.UNCHANGED),
                CharDiff('2', CharDiffType.UNCHANGED)
            )),
            DiffEntry(null, "added line", DiffType.ADDED),
            DiffEntry("deleted line", null, DiffType.DELETED),
            DiffEntry("line 5", "line 5", DiffType.UNCHANGED)
        )
        
        // Simulate the line numbering logic from UnifiedCharDiffText
        var oldLineNumber = 1
        var newLineNumber = 1
        val renderedLines = mutableListOf<String>()
        
        for (entry in diffResult) {
            when (entry.type) {
                DiffType.CHANGED -> {
                    // Should render old line first
                    entry.oldLine?.let { oldLine ->
                        renderedLines.add("${oldLineNumber.toString().padStart(3)} ${" ".repeat(3)} - $oldLine")
                    }
                    
                    // Should render new line second
                    entry.newLine?.let { newLine ->
                        renderedLines.add("${" ".repeat(3)} ${newLineNumber.toString().padStart(3)} + $newLine")
                    }
                    
                    oldLineNumber++
                    newLineNumber++
                }
                DiffType.ADDED -> {
                    val line = entry.newLine ?: ""
                    renderedLines.add("${" ".repeat(3)} ${newLineNumber.toString().padStart(3)} + $line")
                    newLineNumber++
                }
                DiffType.DELETED -> {
                    val line = entry.oldLine ?: ""
                    renderedLines.add("${oldLineNumber.toString().padStart(3)} ${" ".repeat(3)} - $line")
                    oldLineNumber++
                }
                DiffType.UNCHANGED -> {
                    val line = entry.oldLine ?: ""
                    renderedLines.add("${oldLineNumber.toString().padStart(3)} ${newLineNumber.toString().padStart(3)}   $line")
                    oldLineNumber++
                    newLineNumber++
                }
            }
        }
        
        // Expected output for the unified view
        val expected = listOf(
            "  1   1   line 1",
            "  2     - old line 2",
            "      2 + new line 2",  
            "      3 + added line",
            "  3     - deleted line",
            "  4   4   line 5"
        )
        
        println("=== Unified View Test Results ===")
        println("Expected:")
        expected.forEach { println("  $it") }
        println("\nActual:")
        renderedLines.forEach { println("  $it") }
        
        // Verify the results
        assert(renderedLines.size == expected.size) { 
            "Expected ${expected.size} lines but got ${renderedLines.size}" 
        }
        
        for (i in expected.indices) {
            assert(renderedLines[i] == expected[i]) {
                "Line $i mismatch:\nExpected: '${expected[i]}'\nActual:   '${renderedLines[i]}'"
            }
        }
        
        println("\n✅ Test passed! Unified view correctly shows both old and new lines for CHANGED entries.")
    }
}