package mods.ft975.tubes

import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}
import cpw.mods.fml.common.Mod.{Init, PostInit, PreInit}
import cpw.mods.fml.common.network.NetworkMod
import net.minecraftforge.common.Configuration
import cpw.mods.fml.relauncher.{Side, SideOnly}

@Mod(modid = ModInfo.modID, name = ModInfo.modName, version = ModInfo.version, acceptedMinecraftVersions = ModInfo.versionMinecraft, modLanguage = "scala")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
object LightingMod {
	var config: Configuration = null

	@PreInit
	def preInit(event: FMLPreInitializationEvent) {
		config = new Configuration(event.getSuggestedConfigurationFile)
		log = event.getModLog
		setupConfig()
	}

	@Init
	def init(event: FMLInitializationEvent) {
		setupBlocks()
		registerRenders()
	}

	@PostInit
	def postInit(event: FMLPostInitializationEvent) {

	}

	@SideOnly(Side.CLIENT)
	def registerRenders() {

	}

	def setupConfig() {
		config.load()

		config.save()
	}

	def setupBlocks() {}

	def setupTileEntities() {}

	def setupRender() {}
}