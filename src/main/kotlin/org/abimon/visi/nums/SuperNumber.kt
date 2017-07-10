package org.abimon.visi.nums

abstract class SuperNumber: Number(), Comparable<Number> {
    companion object {
        private val digits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z')
        internal val LONG_LOOKUP = (0 until 63).reversed().map { index -> Math.pow(2.0, index.toDouble()).toLong() }.toLongArray()

        internal fun twosCompliment(number: Number): BooleanArray {
            if(number is SuperNumber)
                return number.twosCompliment()
            val num = Math.abs(number.toLong())
            val array = BooleanArray(64)
            (0 until 63).forEach { index -> array[index + 1] = num and LONG_LOOKUP[index] == LONG_LOOKUP[index] }
            if(number.toLong() < 0) {
                (0 until 64).forEach { index -> array[index] = !array[index] }
                for (i in (1 until 64).reversed()) {
                    array[i] = !array[i]

                    if(array[i])
                        break
                }
            }

            return array
        }
    }

    abstract fun twosCompliment(): BooleanArray
    abstract fun copy(): SuperNumber

    override abstract fun toDouble(): Double
    override abstract fun toFloat(): Float
    override abstract fun toLong(): Long
    override abstract fun toInt(): Int
    override abstract fun toChar(): Char
    override abstract fun toShort(): Short
    override abstract fun toByte(): Byte

    override fun toString(): String = toString(10)
    fun toString(r: Int): String {
        var i = this
        var radix = r
        if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX)
            radix = 10
        val bits = twosCompliment().size
        val buf = CharArray(bits + 1)
        var charPos = bits
        val negative = i < 0

        if (!negative) {
            i = -i
        }

        while (i <= -radix) {
            buf[charPos--] = digits[(-(i % radix)).toInt()]
            i /= radix
        }
        buf[charPos] = digits[(-i).toInt()]

        if (negative) {
            buf[--charPos] = '-'
        }

        return String(buf, charPos, (bits + 1) - charPos)
    }

    abstract operator fun plus(add: Number): SuperNumber
    abstract operator fun minus(minus: Number): SuperNumber
    abstract operator fun times(times: Number): SuperNumber
    abstract operator fun div(radix: Number): SuperNumber
    abstract operator fun rem(radix: Number): SuperNumber
    abstract operator fun unaryMinus(): SuperNumber
    abstract override operator fun compareTo(other: Number): Int
    abstract override fun equals(other: Any?): Boolean
    abstract override fun hashCode(): Int

    public operator fun rangeTo(other: SuperNumber): SuperNumberRange = SuperNumberRange(this, other)
}