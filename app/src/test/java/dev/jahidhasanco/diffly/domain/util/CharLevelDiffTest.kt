package dev.jahidhasanco.diffly.domain.util

import dev.jahidhasanco.diffly.domain.model.CharDiffType
import kotlin.test.Test
import kotlin.test.assertEquals


class CharLevelDiffTest {

    private val diffUtil = CharLevelDiff()

    @Test
    fun test_emptyStrings() {
        val result = diffUtil.diff("", "")
        assertEquals(emptyList(), result)
    }

    @Test
    fun test_identicalStrings() {
        val input = "hello world"
        val result = diffUtil.diff(input, input)
        assertEquals(input.length, result.size)
        assert(result.all { it.type == CharDiffType.UNCHANGED })
    }

    @Test
    fun test_insertionAtEnd() {
        val oldStr = "hello"
        val newStr = "hello!"
        val result = diffUtil.diff(oldStr, newStr)

        val expectedTypes = listOf(
            CharDiffType.UNCHANGED,
            CharDiffType.UNCHANGED,
            CharDiffType.UNCHANGED,
            CharDiffType.UNCHANGED,
            CharDiffType.UNCHANGED,
            CharDiffType.INSERTED
        )
        assertEquals(expectedTypes, result.map { it.type })
        assertEquals(newStr, result.map { it.char }.joinToString(""))
    }

    @Test
    fun test_deletionAtEnd() {
        val oldStr = "hello!"
        val newStr = "hello"
        val result = diffUtil.diff(oldStr, newStr)

        val expectedTypes = listOf(
            CharDiffType.UNCHANGED,
            CharDiffType.UNCHANGED,
            CharDiffType.UNCHANGED,
            CharDiffType.UNCHANGED,
            CharDiffType.UNCHANGED,
            CharDiffType.DELETED
        )
        assertEquals(expectedTypes, result.map { it.type })
        assertEquals(oldStr, result.map { it.char }.joinToString(""))
    }

    @Test
    fun test_insertionInMiddle() {
        val oldStr = "helo"
        val newStr = "hello"
        val result = diffUtil.diff(oldStr, newStr)

        val expectedTypes = listOf(
            CharDiffType.UNCHANGED,
            CharDiffType.UNCHANGED,
            CharDiffType.UNCHANGED,
            CharDiffType.INSERTED,
            CharDiffType.UNCHANGED
        )
        assertEquals(expectedTypes, result.map { it.type })
        assertEquals("hello", result.map { it.char }.joinToString(""))
    }

    @Test
    fun test_deletionInMiddle() {
        val oldStr = "hello"
        val newStr = "helo"
        val result = diffUtil.diff(oldStr, newStr)

        val expectedTypes = listOf(
            CharDiffType.UNCHANGED,
            CharDiffType.UNCHANGED,
            CharDiffType.UNCHANGED,
            CharDiffType.DELETED,
            CharDiffType.UNCHANGED
        )
        assertEquals(expectedTypes, result.map { it.type })
        assertEquals("helo",
            result.filter { it.type != CharDiffType.DELETED }.map { it.char }
                .joinToString("")
        )
    }

    @Test
    fun test_substitution() {
        val oldStr = "hello"
        val newStr = "hallo"
        val result = diffUtil.diff(oldStr, newStr)

        // Deletion followed by insertion for 'e' -> 'a'
        val types = result.map { it.type }
        assert(types.contains(CharDiffType.DELETED))
        assert(types.contains(CharDiffType.INSERTED))

        val filteredNew =
            result.filter { it.type != CharDiffType.DELETED }.map { it.char }
                .joinToString("")
        assertEquals(newStr, filteredNew)
    }

    @Test
    fun test_complexChange() {
        val oldStr = "kitten"
        val newStr = "sitting"
        val result = diffUtil.diff(oldStr, newStr)

        val filteredNew =
            result.filter { it.type != CharDiffType.DELETED }.map { it.char }
                .joinToString("")
        assertEquals(newStr, filteredNew)
    }

    @Test
    fun test_completelyDifferentStrings() {
        val oldStr = "abc"
        val newStr = "xyz"
        val result = diffUtil.diff(oldStr, newStr)

        assertEquals(oldStr.length + newStr.length, result.size)
        assertEquals(
            listOf(
                CharDiffType.DELETED, CharDiffType.DELETED, CharDiffType.DELETED,
                CharDiffType.INSERTED, CharDiffType.INSERTED, CharDiffType.INSERTED
            ),
            result.map { it.type }
        )
        val filteredNew = result.filter { it.type != CharDiffType.DELETED }.map { it.char }.joinToString("")
        assertEquals(newStr, filteredNew)
    }

    @Test
    fun test_newStringEmpty() {
        val oldStr = "delete me"
        val newStr = ""
        val result = diffUtil.diff(oldStr, newStr)

        assertEquals(oldStr.length, result.size)
        assert(result.all { it.type == CharDiffType.DELETED })
    }

    @Test
    fun test_oldStringEmpty() {
        val oldStr = ""
        val newStr = "insert me"
        val result = diffUtil.diff(oldStr, newStr)

        assertEquals(newStr.length, result.size)
        assert(result.all { it.type == CharDiffType.INSERTED })
    }

    @Test
    fun test_singleCharSame() {
        val oldStr = "a"
        val newStr = "a"
        val result = diffUtil.diff(oldStr, newStr)

        assertEquals(1, result.size)
        assertEquals(CharDiffType.UNCHANGED, result[0].type)
    }

    @Test
    fun test_singleCharDifferent() {
        val oldStr = "a"
        val newStr = "b"
        val result = diffUtil.diff(oldStr, newStr)

        assertEquals(2, result.size)
        assertEquals(listOf(CharDiffType.DELETED, CharDiffType.INSERTED), result.map { it.type })
    }

    @Test
    fun test_repeatingCharactersChange() {
        val oldStr = "aaabbb"
        val newStr = "aaaabbb"
        val result = diffUtil.diff(oldStr, newStr)

        val types = result.map { it.type }
        assert(types.contains(CharDiffType.INSERTED))
        assertEquals(newStr, result.filter { it.type != CharDiffType.DELETED }.map { it.char }.joinToString(""))
    }

    @Test
    fun test_insertionAtStart() {
        val oldStr = "world"
        val newStr = "hello world"
        val result = diffUtil.diff(oldStr, newStr)

        val types = result.map { it.type }
        // Should begin with inserted 'hello '
        assert(types.take(6).all { it == CharDiffType.INSERTED })
        assertEquals(newStr, result.filter { it.type != CharDiffType.DELETED }.map { it.char }.joinToString(""))
    }

    @Test
    fun test_deletionAtStart() {
        val oldStr = "hello world"
        val newStr = "world"
        val result = diffUtil.diff(oldStr, newStr)

        val types = result.map { it.type }
        // Should begin with deleted 'hello '
        assert(types.take(6).all { it == CharDiffType.DELETED })
        assertEquals(newStr, result.filter { it.type != CharDiffType.DELETED }.map { it.char }.joinToString(""))
    }

    @Test
    fun test_largeInputPerformance() {
        val oldStr = "a".repeat(1000)
        val newStr = "a".repeat(999) + "b"
        val result = diffUtil.diff(oldStr, newStr)

        // Expect 1001 results: 999 unchanged + delete + insert
        assertEquals(1001, result.size)

        // Verify first 999 are unchanged
        assert(result.take(999).all { it.type == CharDiffType.UNCHANGED })

        // Verify deletion and insertion
        assert(result[999].type == CharDiffType.DELETED)
        assert(result[1000].type == CharDiffType.INSERTED)

        // Verify reconstructed new string
        val filteredNew = result.filter { it.type != CharDiffType.DELETED }.map { it.char }.joinToString("")
        assertEquals(newStr, filteredNew)
    }


    @Test
    fun test_mixedInsertionsAndDeletions() {
        val oldStr = "abcdef"
        val newStr = "abXYef"
        val result = diffUtil.diff(oldStr, newStr)

        val filteredNew = result.filter { it.type != CharDiffType.DELETED }.map { it.char }.joinToString("")
        assertEquals(newStr, filteredNew)
    }

}
