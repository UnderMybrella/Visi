package org.abimon.visi.lang

import java.util.*
import java.util.regex.Pattern
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
    val strings = ArrayList<String>()
    val regex = "${group.start}([^${group.start}${group.end}]*)${group.end}|([^$delimiter]+)"
    val m = Pattern.compile(regex).matcher(this)
    while (m.find()) {
        if (m.group(1) != null)
            strings += m.group(1)
        else
            strings += m.group(2)
    }

    return strings.toTypedArray()
}

operator fun String.times(num: Int): String {
    var str = ""
    for(i in 0..num-1)
        str += this
    return str
}