package org.abimon.visi.lang

import org.abimon.visi.collections.ArraySet

fun Runtime.usedMemory(): Long = (totalMemory() - freeMemory())

fun setHeadless() = System.setProperty("java.awt.headless", "true")

//val String.variations: Set<String>
//    get() {
//        val variants = hashSetOf(this)
//        val charPool = this.map { c -> charVariations[c] ?: arrayOf("$c") }
//        val word = IntArray(length)
//        val lastIndex = length - 1
//
//        loop@while(word.filterIndexed { index, i -> i + 1 == charPool[index].size }.count() < length) {
//            word[lastIndex] += 1
//            if(word[lastIndex] >= charPool[lastIndex].size) {
//                word[lastIndex] = 0
//
//                for(i in lastIndex - 1 downTo 0) {
//                    word[i] += 1
//                    if (word[i] >= charPool[i].size) {
//                        word[i] = 0
//                        continue
//                    }
//                    variants.add(word.mapIndexed { index, i -> charPool[index][i] }.joinToString(""))
//                    continue@loop
//                }
//            }
//            else
//                variants.add(word.mapIndexed { index, i -> charPool[index][i] }.joinToString(""))
//        }
//
//        return variants
//    }


fun bruteForce(length: Int, charPool: Array<String>): List<String> = bruteForce(length, (0 until length).map { charPool }.toTypedArray())
fun bruteForce(length: Int, charPool: Array<Array<String>>): List<String> {
    val strs = ArraySet<String>()

    bruteForce(length, charPool) { word -> strs.add(word) }

    return strs
}

fun bruteForce(length: Int, charPool: Array<String>, operate: (String) -> Unit) = bruteForce(length, (0 until length).map { charPool }.toTypedArray(), operate)
fun bruteForce(length: Int, charPool: Array<Array<String>>, operate: (String) -> Unit) = bruteForce(length, charPool.map { it.size }.toIntArray()) { word -> operate(word.mapIndexed { index, i -> charPool[index][i] }.joinToString("")) }
fun bruteForce(length: Int, charPool: IntArray, operate: (IntArray) -> Unit) {
    val word = IntArray(length)
    val lastIndex = length - 1

    operate(word)
    loop@ while (word.filterIndexed { index, i -> i + 1 == charPool[index] }.count() < length) {
        word[lastIndex] += 1
        if (word[lastIndex] >= charPool[lastIndex]) {
            word[lastIndex] = 0

            for (i in lastIndex - 1 downTo 0) {
                word[i] += 1
                if (word[i] >= charPool[i]) {
                    word[i] = 0
                    continue
                }

                operate(word)
                continue@loop
            }
        } else
            operate(word)
    }
}