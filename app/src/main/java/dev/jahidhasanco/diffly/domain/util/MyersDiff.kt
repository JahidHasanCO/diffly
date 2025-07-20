package dev.jahidhasanco.diffly.domain.util

import dev.jahidhasanco.diffly.domain.model.DiffEntry
import dev.jahidhasanco.diffly.domain.model.DiffType

class MyersDiff {

    fun calculateDiff(a: List<String>, b: List<String>): List<DiffEntry> {
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
                    k == -d || (k != d && v[k - 1]!! < v[k + 1]!!) -> v[k + 1]!!
                    else -> v[k - 1]!! + 1
                }
                var y = x - k
                var xCopy = x
                while (xCopy < n && y < m && a[xCopy] == b[y]) {
                    xCopy++
                    y++
                }
                v[k] = xCopy
                if (xCopy >= n && y >= m) return buildDiff(trace, a, b)
            }
        }
        throw IllegalStateException("Diff computation failed")
    }

    private fun buildDiff(
        trace: List<Map<Int, Int>>,
        a: List<String>,
        b: List<String>
    ): List<DiffEntry> {
        val result = mutableListOf<DiffEntry>()
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
                result.add(
                    DiffEntry(
                        oldLine = a[x - 1],
                        newLine = b[y - 1],
                        type = DiffType.UNCHANGED
                    )
                )
                x--
                y--
            }

            if (d > 0) {
                if (x == prevX) {
                    // Added in new text
                    result.add(
                        DiffEntry(
                            oldLine = null,
                            newLine = b[y - 1],
                            type = DiffType.ADDED
                        )
                    )
                    y--
                } else {
                    // Deleted from old text
                    result.add(
                        DiffEntry(
                            oldLine = a[x - 1],
                            newLine = null,
                            type = DiffType.DELETED
                        )
                    )
                    x--
                }
            }
        }

        return result.reversed()
    }
}