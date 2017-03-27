package org.abimon.visi.lang

import java.io.File

enum class EnumOS(internal var storageLocation: String) {

    WINDOWS(System.getProperty("user.home") + File.separator),
    MACOSX(System.getProperty("user.home") + "/Library/Application Support/"),
    LINUX(System.getProperty("user.home") + File.separator),
    OTHER(System.getProperty("user.home") + File.separator);

    fun getStorageLocation(folderName: String = "Visi"): File {
        return File(storageLocation, folderName)
    }


    fun hasANSI(): Boolean {
        return this == MACOSX || this == LINUX
    }

    companion object {
        fun determineOS(): EnumOS {
            val os = System.getProperty("os.name").toLowerCase()
            if (os.contains("mac"))
                return MACOSX
            if (os.contains("windows"))
                return WINDOWS
            return OTHER
        }

        val homeLocation: File
            get() = File(System.getProperty("user.home"))
    }
}