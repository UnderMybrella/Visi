package org.abimon.visi.nums

public class SuperNumberRange(start: SuperNumber, endInclusive: SuperNumber) : SuperNumberProgression(start, endInclusive, SuperLong(1)), ClosedRange<SuperNumber> {
    override val start: SuperNumber get() = first
    override val endInclusive: SuperNumber get() = last

    override fun contains(value: SuperNumber): Boolean = first <= value && value <= last

    override fun isEmpty(): Boolean = first > last

    override fun equals(other: Any?): Boolean =
            other is SuperNumberRange && (isEmpty() && other.isEmpty() ||
                    first == other.first && last == other.last)

    override fun hashCode(): Int =
            if (isEmpty()) -1 else ((first * 31) + last).toInt()

    override fun toString(): String = "$first..$last"

    companion object {
        /** An empty range of values of type Int. */
        public val EMPTY: SuperNumberRange = SuperNumberRange(SuperLong(1), SuperLong(0))
    }
}
public open class SuperNumberProgression
internal constructor
(
        start: SuperNumber,
        endInclusive: SuperNumber,
        /**
         * The step of the progression.
         */
        public val step: SuperNumber
) : Iterable<SuperNumber> {
    init {
        if ((step as Number) == 0) throw kotlin.IllegalArgumentException("Step must be non-zero")
    }

    /**
     * The first element in the progression.
     */
    public val first: SuperNumber = start

    /**
     * The last element in the progression.
     */
    public val last: SuperNumber = getProgressionLastElement(start, endInclusive, step)

    override fun iterator(): SuperNumberIterator = SuperNumberProgressionIterator(first, last, step)

    /** Checks if the progression is empty. */
    public open fun isEmpty(): Boolean = if (step > 0) first > last else first < last

    override fun equals(other: Any?): Boolean =
            other is SuperNumberProgression && (isEmpty() && other.isEmpty() ||
                    first == other.first && last == other.last && step == other.step)

    override fun hashCode(): Int =
            if (isEmpty()) -1 else (31 * (31 * (first.toLong() xor (first.toLong() ushr 32)) + (last.toLong() xor (last.toLong() ushr 32))) + (step.toLong() xor (step.toLong() ushr 32))).toInt()

    override fun toString(): String = if (step > 0) "$first..$last step $step" else "$first downTo $last step ${-step}"

    companion object {
        /**
         * Creates LongProgression within the specified bounds of a closed range.

         * The progression starts with the [rangeStart] value and goes toward the [rangeEnd] value not excluding it, with the specified [step].
         * In order to go backwards the [step] must be negative.
         */
        public fun fromClosedRange(rangeStart: SuperNumber, rangeEnd: SuperNumber, step: SuperNumber): SuperNumberProgression = SuperNumberProgression(rangeStart, rangeEnd, step)
    }
}

/** An iterator over a sequence of values of type `Long`. */
public abstract class SuperNumberIterator : Iterator<SuperNumber> {
    override final fun next() = nextNumber()

    /** Returns the next value in the sequence without boxing. */
    public abstract fun nextNumber(): SuperNumber
}

class SuperNumberProgressionIterator(first: SuperNumber, last: SuperNumber, val step: SuperNumber) : SuperNumberIterator() {
    private var next = first
    private val finalElement = last
    private var hasNext: Boolean = if (step > 0) first <= last else first >= last

    override fun hasNext(): Boolean = hasNext

    override fun nextNumber(): SuperNumber {
        val value = next
        if (value == finalElement) {
            hasNext = false
        }
        else {
            next += step
        }
        return value
    }
}

fun mod(a: SuperNumber, b: SuperNumber): SuperNumber {
    val mod = a % b
    return if (mod >= 0) mod else mod + b
}

fun differenceModulo(a: SuperNumber, b: SuperNumber, c: SuperNumber): SuperNumber {
    return mod(mod(a, c) - mod(b, c), c)
}

/**
 * Calculates the final element of a bounded arithmetic progression, i.e. the last element of the progression which is in the range
 * from [start] to [end] in case of a positive [step], or from [end] to [start] in case of a negative
 * [step].
 *
 * No validation on passed parameters is performed. The given parameters should satisfy the condition: either
 * `step > 0` and `start >= end`, or `step < 0` and`start >= end`.
 * @param start first element of the progression
 * @param end ending bound for the progression
 * @param step increment, or difference of successive elements in the progression
 * @return the final element of the progression
 * @suppress
 */
fun getProgressionLastElement(start: SuperNumber, end: SuperNumber, step: SuperNumber): SuperNumber {
    if (step > 0) {
        return end - differenceModulo(end, start, step)
    }
    else if (step < 0) {
        return end + differenceModulo(start, end, -step)
    }
    else {
        throw kotlin.IllegalArgumentException("Step is zero.")
    }
}

public infix fun SuperLong.until(to: SuperLong): SuperNumberRange {
    if (to <= SuperLong.MIN_VALUE) return SuperNumberRange.EMPTY
    return this .. (to - 1)
}