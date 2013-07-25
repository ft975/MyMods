package mods.ft975.cloud

import aurelienribon.tweenengine.Tween
import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.{FMLServerStartedEvent, FMLInitializationEvent}
import cpw.mods.fml.common.network.NetworkMod
import cpw.mods.fml.common.registry.TickRegistry
import cpw.mods.fml.relauncher.{Side, SideOnly}
import java.util.logging.Logger
import mods.ft975.cloud.CloudState.CloudTweener
import net.minecraft.client.renderer.RenderGlobal
import net.minecraftforge.common.DimensionManager

@Mod(modid = ModInfo.modID, name = ModInfo.modName, version = ModInfo.version, acceptedMinecraftVersions = ModInfo.versionMinecraft, modLanguage = "scala")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
object CloudMod {

	@EventHandler
	def init(event: FMLInitializationEvent) {
		RenderGlobal.cloudsMoving = false
		setupLog()
		setupConfig()
		setupBlocks()
		setupCreative()
		setupRender()
		setupCrafting()
		Tween.registerAccessor(CloudState.getClass, CloudTweener)
	}

	@EventHandler
	def serverStarted(event: FMLServerStartedEvent) {
		RenderGlobal.cloudsMoving = true
		TickRegistry.registerTickHandler(TickHandler, Side.CLIENT)
		DimensionManager.getProvider(0).setCloudRenderer(CloudRender)
	}

	def setupCreative() {}

	@SideOnly(Side.CLIENT)
	def setupRender() {}

	def setupConfig() {}

	def setupLog() {
		if (mods.ft975.util.log == null) mods.ft975.util.log = Logger.getLogger("FT:Util")
		log = Logger.getLogger("FT:Lights")
	}

	def setupBlocks() {}

	def setupCrafting() {}
}