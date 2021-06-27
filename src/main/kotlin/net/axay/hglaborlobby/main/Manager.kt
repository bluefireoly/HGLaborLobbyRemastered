package net.axay.hglaborlobby.main

import net.axay.hglaborlobby.chat.ChatFormatter
import net.axay.hglaborlobby.damager.DamageCommand
import net.axay.hglaborlobby.damager.Damager
import net.axay.hglaborlobby.database.DatabaseManager
import net.axay.hglaborlobby.eventmanager.joinserver.OnJoinManager
import net.axay.hglaborlobby.eventmanager.leaveserver.KickMessageListener
import net.axay.hglaborlobby.eventmanager.leaveserver.OnLeaveManager
import net.axay.hglaborlobby.functionality.*
import net.axay.hglaborlobby.gui.guis.*
import net.axay.hglaborlobby.hg.HostCommand
import net.axay.hglaborlobby.minigames.Waterfight
import net.axay.hglaborlobby.protection.ServerProtection
import net.axay.hglaborlobby.security.VPNCommand
import net.axay.kspigot.chat.KColors
import net.axay.kspigot.extensions.broadcast
import net.axay.kspigot.extensions.bukkit.info
import net.axay.kspigot.extensions.bukkit.register
import net.axay.kspigot.extensions.bukkit.success
import net.axay.kspigot.extensions.console
import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.main.KSpigot
import net.axay.kspigot.sound.sound
import org.bukkit.Sound

class InternalMainClass : KSpigot() {
    companion object {
        lateinit var INSTANCE: InternalMainClass; private set
    }

    override fun load() {
        INSTANCE = this
        System.setProperty(
            "org.litote.mongo.test.mapping.service",
            "org.litote.kmongo.serialization.SerializationClassMappingTypeService"
        )
        console.info("Loading Lobby plugin...")
    }

    override fun startup() {
        ServerProtection.enable()

        //PlayerSettingsHolder.enable()

        StatsDisplay.enable()

        SoupHealing.enable()
        OnJoinManager.enable()
        LobbyItems.enable()
        OnLeaveManager.enable()
        KickMessageListener.enable()
        ChatFormatter.enable()
        Damager.enable()
        Waterfight.enable()
        ElytraLauncher.enable()

        AdminGUI.register("admingui")
        DamageCommand.register("damage")
        RandomFirework.register("randomfirework")
        ChatFormatter.register("formatting")
        VPNCommand.register("vpn")
        HostCommand.register("host")

        // Main GUI
        MainGUI.enable()
        WarpGUI.enable()
        HGQueueGUI.enable()
        //PlayerVisiblityGUI.enable()
        PetGUI.enable()
        PrefixGUI.enable()
        //PrivacySettingsGUI.enable()

        server.messenger.registerOutgoingPluginChannel(this, "BungeeCord")

        broadcast("${KColors.MEDIUMSPRINGGREEN}-> ENABLED PLUGIN")
        onlinePlayers.forEach { it.sound(Sound.BLOCK_BEACON_ACTIVATE) }

        console.success("Lobby plugin enabled.")
    }

    override fun shutdown() {
        console.info("Shutting down Lobby plugin...")

        DatabaseManager.statsClient.close()

        broadcast("${KColors.TOMATO}-> DISABLING PLUGIN ${KColors.DARKGRAY}(maybe a reload)")
        onlinePlayers.forEach { it.sound(Sound.BLOCK_BEACON_DEACTIVATE) }

        console.success("Shut down Lobby plugin.")
    }
}

val Manager by lazy { InternalMainClass.INSTANCE }
