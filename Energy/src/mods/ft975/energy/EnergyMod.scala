package mods.ft975.energy

import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.network.NetworkMod
import cpw.mods.fml.common.{Loader, Mod}
import cpw.mods.fml.relauncher.{Side, SideOnly}
import java.io.File
import java.util.logging.Logger
import net.minecraftforge.common.Configuration

@Mod(modid = ModInfo.modID, name = ModInfo.modName, version = ModInfo.version, acceptedMinecraftVersions = ModInfo.versionMinecraft, modLanguage = "scala")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
object EnergyMod {

	@EventHandler
	def init(event: FMLInitializationEvent) {
		setupLog()
		setupConfig()
		setupBlocks()
		setupCreative()
		setupRender()
		setupCrafting()
	}

	def setupCreative() {}

	@SideOnly(Side.CLIENT)
	def setupRender() {}

	def setupConfig() {
		val config = new Configuration(new File(Loader.instance.getConfigDir, ModInfo.configName + ".cfg"))
		config.load()

		config.save()
	}

	def setupLog() {
		if (mods.ft975.util.log == null) mods.ft975.util.log = Logger.getLogger("FT_Util")
		log = Logger.getLogger("FT_Energy")
	}

	def setupBlocks() {}

	def setupCrafting() {}
}