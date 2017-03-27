package org.abimon.visi.lang

import java.awt.Desktop
import java.awt.GraphicsEnvironment
import java.net.URI

object SteamProtocol {
    private val base = "steam://"

    fun openSteam() {
        open("")
    }

    fun exitSteam() {
        open("ExitSteam")
    }

    fun addNonSteamGame() {
        open("AddNonSteamGame")
    }

    fun openStore(steamID: String) {
        open("advertise/" + steamID)
    }

    fun acceptGift(giftPass: String) {
        open("ackMessage/ackGuestPass/" + giftPass)
    }

    fun openNews(steamID: String) {
        open("appnews/" + steamID)
    }

    fun backup(steamID: String) {
        open("backup/" + steamID)
    }

    fun browseMedia() {
        open("browsemedia")
    }

    fun checkSystemRequirements(steamID: String) {
        open("checksysreq/" + steamID)
    }

    fun defragApp(steamID: String) {
        open("defrag/" + steamID)
    }

    fun openFriends() {
        open("friends")
    }

    fun openGame(steamID: String) {
        open("run/" + steamID)
    }

    private fun open(url: String) {
        try {
            val headless = GraphicsEnvironment.isHeadless()
            if (headless)
                System.setProperty("java.awt.headless", "false")
            Desktop.getDesktop().browse(URI.create(base + url))
            if (headless)
                System.setProperty("java.awt.headless", "true")
        } catch (th: Throwable) {
            th.printStackTrace()
        }

    }
}
