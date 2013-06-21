package ft975.lighting

import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}
import cpw.mods.fml.common.Mod.{Init, PostInit, PreInit}
import cpw.mods.fml.common.network.NetworkMod
import net.minecraftforge.common.Configuration
import cpw.mods.fml.common.registry.{GameRegistry, LanguageRegistry}
import net.minecraft.creativetab.CreativeTabs
import ft975.base.util.Log
import cpw.mods.fml.relauncher.{Side, SideOnly}
import cpw.mods.fml.client.registry.ClientRegistry
import net.minecraftforge.client.MinecraftForgeClient
import ft975.lighting.render.{TileLampRender, ItemLampRender}

@Mod(modid = ModInfo.modID, name = ModInfo.modName, version = ModInfo.version, acceptedMinecraftVersions = ModInfo.versionMinecraft, modLanguage = "scala")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
object LightingMod {
	var config: Configuration = null
	var blockLamp: BlockLamp = null
	var blockID: Int = 0

	@PreInit
	def preInit(event: FMLPreInitializationEvent) {
		config = new Configuration(event.getSuggestedConfigurationFile)
		blockID = config.getBlock("Lamp_Block", 2401).getInt
		config.save()
	}

	@Init
	def init(event: FMLInitializationEvent) {
		blockLamp = new BlockLamp(blockID)
		blockLamp.setLightOpacity(0)
		GameRegistry.registerBlock(blockLamp, classOf[ItemLamp], "BlockLamp", ModInfo.modID)
		GameRegistry.registerTileEntity(classOf[TileLamp], "ftlamp")
		blockLamp.setCreativeTab(CreativeTabs.tabBlock)
		registerRenders()
	}

	@PostInit
	def postInit(event: FMLPostInitializationEvent) {
		for (col: Colors <- Colors.vals;
				 sha: Shapes <- Shapes.vals) {
			val iS = ItemLamp.buildStack(1, col, sha)
			LanguageRegistry.addName(iS, col.name + " " + sha.name)
			Log.logDebug("registered " + col + sha)
		}
	}

	@SideOnly(Side.CLIENT)
	def registerRenders() {
		ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileLamp], new TileLampRender)
		MinecraftForgeClient.registerItemRenderer(blockID, new ItemLampRender)
	}
}