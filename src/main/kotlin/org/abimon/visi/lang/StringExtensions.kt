package org.abimon.visi.lang

import java.util.*
import java.util.regex.PatternSyntaxException

data class StringGroup(val start: String, val end: String) { constructor(pair: Pair<String, String>): this(pair.first, pair.second) }

object StringGroups {
    val SPEECH = StringGroup("\"" to "\"")
    val SINGLE_SPEECH = StringGroup("'" to "'")
    val TILDE = StringGroup("`" to "`")
    val PARENTHESES = StringGroup("(" to ")")
    val BRACKETS = StringGroup("\"" to "\"")
    val BRACES = StringGroup("{" to "}")
}

val String.parents: String
    get() = if (this.lastIndexOf('/') == -1) "" else this.substring(0, this.lastIndexOf('/'))

val String.child: String
    get() = if (this.lastIndexOf('/') == -1) this else this.substring(this.lastIndexOf('/') + 1, length)

val String.extension: String
    get() = if (this.lastIndexOf('.') == -1) this else this.substring(this.lastIndexOf('.') + 1, length)

fun String.isRegex(): Boolean {
    try {
        toRegex()
        return true
    }
    catch(notValid: PatternSyntaxException) {}

    return false
}

fun String.pluralise(amount: Int, plural: String = "${this}s") = if(amount == 1 || amount == -1) "$amount $this" else "$amount $plural"

fun String.numberOfOccurences(str: String): Int = split(str).size - 1
fun String.replaceLast(replace: String, replacing: String): String {
    if(lastIndexOf(replace) == -1)
        return this
    val before = substring(0, lastIndexOf(replace))
    val after = substring(lastIndexOf(replace) + replace.length, length)
    return before + replacing + after
}

fun String.splitOutsideGroup(delimiter: String = "\\s+", cap: Int = 0, group: StringGroup = StringGroups.SPEECH): Array<String> {
    val strings = LinkedList<String>()

    for (string in split("$delimiter(?=([^\\Q${group.start}\\E]*\\Q${group.start}\\E[^\\Q${group.end}\\E]*\\Q${group.end}\\E)*[^\\Q${group.end}\\E]*$)".toRegex(), cap.coerceAtLeast(0)).toTypedArray()) {
        var str = string
        if (str.startsWith("\""))
            str = str.substring(1)
        if (str.endsWith("\""))
            str = str.substring(0, str.length - 1)
        strings.add(str)
    }

    return strings.toTypedArray()
}

operator fun String.times(num: Int): String {
    var str = ""
    for(i in 0..num-1)
        str += this
    return str
}