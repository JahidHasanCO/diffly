package dev.jahidhasanco.diffly.domain.util

import dev.jahidhasanco.diffly.domain.model.CharDiff
import dev.jahidhasanco.diffly.domain.model.CharDiffType

class CharLevelDiff {

    fun diff(oldStr: String, newStr: String): List<CharDiff> {
        val a = oldStr.toList()
        val b = newStr.toList()

        val n = a.size
        val m = b.size
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
                while (xCopy < n && y < m && a[xCopy] == b[y]) {
                    xCopy++
                    y++
                }
                v[k] = xCopy
                if (xCopy >= n && y >= m) {
                    return buildCharDiff(trace, a, b)
                }
            }
        }
        throw IllegalStateException("Character diff computation failed")
    }

    private fun buildCharDiff(
        trace: List<Map<Int, Int>>,
        a: List<Char>,
        b: List<Char>
    ): List<CharDiff> {
        val result = mutableListOf<CharDiff>()
        var x = a.size
        var y = b.size

        for (d in trace.size - 1 downTo 0) {
            val v = trace[d]
            val k = x - y
            val prevK = when {
                k == -d || (k != d && v[k - 1]!! < v[k + 1]!!) -> k + 1
                else -> k - 1
            }
            val prevX = v[prevK]!!
            val prevY = prevX - prevK

            while (x > prevX && y > prevY) {
                result.add(CharDiff(a[x - 1], CharDiffType.UNCHANGED))
                x--
                y--
            }

            if (d > 0) {
                if (x == prevX) {
                    result.add(CharDiff(b[y - 1], CharDiffType.INSERTED))
                    y--
                } else {
                    result.add(CharDiff(a[x - 1], CharDiffType.DELETED))
                    x--
                }
            }
        }
        return result.reversed()
    }
}
