package org.abimon.visi.nums

import java.util.*

/** Represents numbers between -2^127 to 2^127-1 */
class SuperLong() : SuperNumber() {
    companion object {
        val NUM_BITS = 8192
        val RANGE = (0 until NUM_BITS)
        val TWOS_RANGE = (1 until NUM_BITS)

        val LOOKUP = RANGE.reversed().map { index -> Math.pow(2.0, index.toDouble()).toLong() }.toLongArray()

        val MIN_VALUE = SuperLong(RANGE.map { index -> index == 0 }.toBooleanArray())
        val MAX_VALUE = SuperLong(RANGE.map { index -> index != 0 }.toBooleanArray())

        private fun pad(arr: BooleanArray): BooleanArray {
            if (arr.size == NUM_BITS)
                return arr
            val num = BooleanArray(NUM_BITS)
            if (arr[0])
                num.indices.forEach { index -> num[index] = true }
            System.arraycopy(arr, 1, num, 1 + (num.size - arr.size).coerceIn(0 until NUM_BITS), arr.size - 1)
            return num
        }

        private fun abs(num: BooleanArray): BooleanArray {
            val abs = Arrays.copyOf(num, num.size)
            if (abs[0]) {
                abs.forEachIndexed { index, bit -> abs[index] = !bit }

                for (index in TWOS_RANGE.reversed()) {
                    abs[index] = !abs[index]
                    if (abs[index])
                        break
                }
            }

            return abs
        }

        private fun add(num: BooleanArray, adding: BooleanArray) {
            adding.indices.reversed()
                    .filter { adding[it] }
                    .forEach { index ->
                        if (num[index]) {
                            for (j in (0..index).reversed()) {
                                num[j] = !num[j]

                                if (num[j])
                                    break
                            }
                        } else
                            num[index] = true
                    }
        }

        private fun sub(num: BooleanArray, subbing: BooleanArray) {
            subbing.indices.reversed()
                    .filter { subbing[it] }
                    .forEach { index ->
                        if (num[index])
                            num[index] = false
                        else {
                            for (j in (0..index).reversed()) {
                                num[j] = !num[j]

                                if (!num[j])
                                    break
                            }
                        }
                    }
        }

        private fun multiply(num: BooleanArray, multiplyBy: BooleanArray) {
            val newNum = BooleanArray(NUM_BITS)
            val tmp = BooleanArray(NUM_BITS)

            multiplyBy.indices.reversed()
                    .filter { multiplyBy[it] }
                    .forEach { index ->
                        for (j in (0 until NUM_BITS).reversed()) {
                            if (num[j]) {
                                val numIndex = j - (NUM_BITS - 1 - index)
                                if (numIndex >= 0) {
                                    tmp[numIndex] = true
                                    add(newNum, tmp)
                                    tmp[numIndex] = false
                                }
                            }
                        }
                    }

            System.arraycopy(newNum, 0, num, 0, NUM_BITS)
        }

        private fun shift(arr: BooleanArray) {
            System.arraycopy(arr, 1, arr, 0, arr.size - 1)
        }

        private fun div(num: BooleanArray, divideBy: BooleanArray): Pair<BooleanArray, BooleanArray> {
            if (divideBy.all { !it })
                throw ArithmeticException("/ by zero")
            if (num.all { !it })
                return Pair(BooleanArray(NUM_BITS), BooleanArray(NUM_BITS))

            val absNum = abs(num)
            val absRadix = abs(divideBy)

            val quotient = BooleanArray(NUM_BITS)
            val remainder = BooleanArray(NUM_BITS)
            val lsb = remainder.size - 1

            for (i in (1 until NUM_BITS)) {
                shift(remainder)
                remainder[lsb] = absNum[i]

                if (compare(remainder, absRadix) >= 0) {
                    sub(remainder, absRadix)
                    quotient[i] = true
                }
            }

            if (num[0] != divideBy[0]) {
                RANGE.forEach { index -> quotient[index] = !quotient[index] }
                for (i in TWOS_RANGE.reversed()) {
                    quotient[i] = !quotient[i]

                    if (quotient[i])
                        break
                }

                RANGE.forEach { index -> remainder[index] = !remainder[index] }
                for (i in TWOS_RANGE.reversed()) {
                    remainder[i] = !remainder[i]

                    if (remainder[i])
                        break
                }

            }

            return Pair(quotient, remainder)
        }

        private fun compare(num: BooleanArray, otherNum: BooleanArray): Int {
            if (num[0] > otherNum[0]) //Negative numbers
                return -1
            else if (num[0] < otherNum[0])
                return 1

            num.indices.forEach { index ->
                if (num[index] < otherNum[index])
                    return -1
                else if (num[index] > otherNum[index])
                    return 1
            }

            return 0
        }
    }

    constructor(number: Number) : this(twosCompliment(number))
    internal constructor(arr: BooleanArray) : this() {
        System.arraycopy(pad(arr), 0, num, 0, NUM_BITS)
    }

    val num = BooleanArray(NUM_BITS)

    override fun toByte(): Byte = toLong().toByte()
    override fun toChar(): Char = toLong().toChar()
    override fun toShort(): Short = toLong().toShort()
    override fun toInt(): Int = toLong().toInt()
    override fun toLong(): Long {
        var numVal = 0L
        val negative = num[0]
        TWOS_RANGE.forEach { index -> if (num[index] != negative) numVal = numVal or LOOKUP[index] } //numVal = numVal or LOOKUP[index]
        if (negative)
            return -(numVal + 1)
        return numVal
    }

    override fun toDouble(): Double = toLong().toDouble()
    override fun toFloat(): Float = toLong().toFloat()

    override fun twosCompliment(): BooleanArray = Arrays.copyOf(num, NUM_BITS)
    override fun copy(): SuperLong = SuperLong(Arrays.copyOf(num, NUM_BITS))

    override operator fun plus(add: Number): SuperLong {
        val adding = pad(twosCompliment(add))
        val newNum = Arrays.copyOf(num, NUM_BITS)
        add(newNum, adding)
        return SuperLong(newNum)
    }

    override operator fun minus(minus: Number): SuperLong {
        val subbing = pad(twosCompliment(minus))
        val newNum = Arrays.copyOf(num, NUM_BITS)
        sub(newNum, subbing)
        return SuperLong(newNum)
    }

    override operator fun times(times: Number): SuperLong {
        val timesing = pad(twosCompliment(times))
        val newNum = Arrays.copyOf(num, NUM_BITS)
        multiply(newNum, timesing)
        return SuperLong(newNum)
    }

    override fun div(radix: Number): SuperLong {
        val radixArray = pad(twosCompliment(radix))
        val newNum = Arrays.copyOf(num, NUM_BITS)
        return SuperLong(div(newNum, radixArray).first)
    }

    override fun rem(radix: Number): SuperLong {
        val radixArray = pad(twosCompliment(radix))
        val newNum = Arrays.copyOf(num, NUM_BITS)
        return SuperLong(div(newNum, radixArray).second)
    }

    infix fun pow(power: Number): SuperLong {
        val num = twosCompliment()
        val original = twosCompliment()
        (SuperLong(0) until SuperLong(power) - 1).forEach { multiply(num, original) }
        return SuperLong(num)
    }

    override fun unaryMinus(): SuperLong {
        val arr = Arrays.copyOf(num, NUM_BITS)

        if (num.all { !it })
            return SuperLong(arr)

        arr.forEachIndexed { index, bit -> arr[index] = !bit }

        for (index in TWOS_RANGE.reversed()) {
            arr[index] = !arr[index]
            if (arr[index])
                break
        }

        return SuperLong(arr)
    }

    //-1 if this is smaller, 0 if equal, 1 if this is bigger
    override fun compareTo(other: Number): Int {
        val otherNum = pad(SuperNumber.twosCompliment(other))
        return compare(num, otherNum)
    }

    override fun equals(other: Any?): Boolean {
        if((other ?: return false) is SuperNumber)
            return Arrays.equals(twosCompliment(), (other as SuperNumber).twosCompliment())
        return false
    }
}