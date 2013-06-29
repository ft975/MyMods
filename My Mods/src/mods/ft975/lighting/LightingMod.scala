package mods.ft975.lighting

import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}
import cpw.mods.fml.common.Mod.{Init, PostInit, PreInit}
import cpw.mods.fml.common.network.NetworkMod
import net.minecraftforge.common.Configuration
import cpw.mods.fml.common.registry.{GameRegistry, LanguageRegistry}
import net.minecraft.creativetab.CreativeTabs
import cpw.mods.fml.relauncher.{Side, SideOnly}
import cpw.mods.fml.client.registry.ClientRegistry
import net.minecraftforge.client.MinecraftForgeClient
import java.util.logging.Level
import mods.ft975.lighting.render.{TileLampRender, ItemLampRender}

@Mod(modid = ModInfo.modID, name = ModInfo.modName, version = ModInfo.version, acceptedMinecraftVersions = ModInfo.versionMinecraft, modLanguage = "scala")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
object LightingMod {
	var config: Configuration = null
	var blockLamp: BlockLamp = null
	var blockID: Int = 0

	@PreInit
	def preInit(event: FMLPreInitializationEvent) {
		config = new Configuration(event.getSuggestedConfigurationFile)
		log = event.getModLog
		setupConfig()
	}

	@Init
	def init(event: FMLInitializationEvent) {
		GameRegistry.registerTileEntity(classOf[TileLamp], "ftlamp")
		setupBlocks()
		registerRenders()
	}

	@PostInit
	def postInit(event: FMLPostInitializationEvent) {

	}

	@SideOnly(Side.CLIENT)
	def registerRenders() {
		ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileLamp], new TileLampRender)
		MinecraftForgeClient.registerItemRenderer(blockID, new ItemLampRender)
	}

	def setupConfig() {
		config.load()
		blockID = config.getBlock("Lamp_Block", 2401).getInt
		for (i <- 0 to 15) {
			Colors.colArray(i) = config.get("Colors", Colors.colNameArray(i), Colors.colArray(i)).getString
		}
		Colors.darkenFactor = config.get("Special", "Darkening factor, all colors will be multiplied by this", Colors.darkenFactor).getDouble(Colors.darkenFactor).toFloat
		config.save()
	}

	def setupBlocks() {
		blockLamp = new BlockLamp(blockID)
		blockLamp.setLightOpacity(0)
		GameRegistry.registerBlock(blockLamp, classOf[ItemLamp], "BlockLamp", ModInfo.modID)
		blockLamp.setCreativeTab(CreativeTabs.tabBlock)
		for (col: Colors <- Colors.vals;
		     sha: Shapes <- Shapes.vals;
		     inverted: Boolean <- List(true, false)) {
			val iS = ItemLamp.buildStack(1, col, sha, inverted)
			LanguageRegistry.addName(iS, (if (inverted) "Inverted " else "") + col.name + " " + sha.name)
			DebugOnly {
				log.log(Level.INFO, "registered " + col + sha + inverted)
			}
		}
	}

	def setupTileEntities() {}

	def setupRender() {}
}