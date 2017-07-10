package org.abimon.visi.lang

public infix fun <A, B, C> Pair<A, B>.and(that: C): Triple<A, B, C> = Triple(this.first, this.second, that)