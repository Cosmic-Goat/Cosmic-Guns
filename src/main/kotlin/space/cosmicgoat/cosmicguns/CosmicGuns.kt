package space.cosmicgoat.cosmicguns

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File

// For support join https://discord.gg/v6v4pMv

const val MOD_ID = "cosmic-guns"
val LOGGER: Logger = LogManager.getFormatterLogger("Cosmic Guns")
val ITEM_GROUP: ItemGroup = FabricItemGroupBuilder.build(Identifier(MOD_ID, "tab")) { ItemStack(Items.ARROW) }

//@Suppress("unused")
object CosmicGuns : ModInitializer {


    override fun onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        LOGGER.info("Hello World!")
        val userDataDir = FabricLoader.getInstance().configDir.resolve(MOD_ID).toFile()
        val defaultDataDir = File(javaClass.getResource("/assets/data/").toURI())

        val guns = gunResourceLoader(userDataDir) + gunResourceLoader(defaultDataDir)

        guns.forEach {
//            LOGGER.info("${it.name}: ${it.damage}")
            Registry.register(
                Registry.ITEM,
                Identifier(MOD_ID, it.id),
                Item(FabricItemSettings().group(ITEM_GROUP))
            )
        }
    }
}

