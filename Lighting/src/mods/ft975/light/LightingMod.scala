package mods.ft975.light

import cpw.mods.fml.client.registry.ClientRegistry
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.network.NetworkMod
import cpw.mods.fml.common.registry.{GameRegistry, LanguageRegistry}
import cpw.mods.fml.common.{Loader, Mod}
import cpw.mods.fml.relauncher.{Side, SideOnly}
import java.io.File
import java.util.logging.{Logger, Level}
import mods.ft975.light.block.{ItemLamp, BlockLamp}
import mods.ft975.light.render.{TileLampRender, ItemLampRender}
import mods.ft975.light.tileentity.TileLamp
import mods.ft975.light.util.{LampColor, LampShape}
import net.minecraft.block.Block
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.{Item, ItemStack}
import net.minecraftforge.client.MinecraftForgeClient
import net.minecraftforge.common.Configuration

@Mod(modid = ModInfo.modID, name = ModInfo.modName, version = ModInfo.version, acceptedMinecraftVersions = ModInfo.versionMinecraft, modLanguage = "scala")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
object LightingMod {
	var blockLamp  : BlockLamp = _
	var blockIDLamp: Int       = _

	@EventHandler
	def init(event: FMLInitializationEvent) {
		setupLog()
		setupConfig()
		setupBlocks()
		setupCreative()
		setupRender()
		setupCrafting()
	}

	def setupCreative() {
		val creativeTabLamp = new CreativeTabs("ftLighting") {
			LanguageRegistry.instance.addStringLocalization("itemGroup.ftLighting", "en_US", "Lighting")
			val icon = ItemLamp.buildStack(0, LampColor.Magenta, LampShape.Block, isOn = true)

			override def getIconItemStack = icon
		}

		blockLamp.setCreativeTab(creativeTabLamp)
	}

	@SideOnly(Side.CLIENT)
	def setupRender() {
		ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileLamp], TileLampRender)
		MinecraftForgeClient.registerItemRenderer(blockIDLamp, ItemLampRender)
	}

	def setupConfig() {
		val config = new Configuration(new File(Loader.instance.getConfigDir, ModInfo.configName + ".cfg"))
		config.load()
		blockIDLamp = config.getBlock("Lamp_Block", 2401).getInt
		for (i <- 0 to 15) {
			LampColor.colArray(i) = config.get("Colors", LampColor.colNameArray(i), LampColor.colArray(i)).getString
		}
		LampColor.darkenFactor = config.get("Special", "Darkening factor, all colors will be multiplied by this", LampColor.darkenFactor).getDouble(LampColor.darkenFactor).toFloat
		config.save()
	}

	def setupLog() {
		if (mods.ft975.util.log == null) mods.ft975.util.log = Logger.getLogger("FT:Util")
		log = Logger.getLogger("FT:Lights")
	}

	def setupBlocks() {
		blockLamp = new BlockLamp(blockIDLamp)
		blockLamp.setLightOpacity(0)
		GameRegistry.registerBlock(blockLamp, classOf[ItemLamp], "BlockLamp", ModInfo.modID)

		for (col: LampColor <- LampColor.vals;
				 sha: LampShape <- LampShape.vals;
				 inverted: Boolean <- List(true, false)) {
			val iS = ItemLamp.buildStack(1, col, sha, inverted)
			LanguageRegistry.addName(iS, (if (inverted) "Inverted " else "") + col.name + " " + sha.name)
			DebugOnly {
				log.log(Level.INFO, "registered " + col + sha + inverted)
			}
		}

		GameRegistry.registerTileEntity(classOf[TileLamp], "ftlamp")
	}

	def setupCrafting() {
		def buildStack(cnt: Int, col: LampColor)(sap: LampShape, isOn: Boolean): ItemStack = ItemLamp.buildStack(cnt, col, sap, isOn)

		val glassPane = new ItemStack(Block.thinGlass)
		val ironPane = new ItemStack(Block.fenceIron)
		val stoneSlab = new ItemStack(Block.stoneSingleSlab, 1, 0)
		val redstoneDust = new ItemStack(Item.redstone)
		val redstoneTorch = new ItemStack(Block.torchRedstoneActive)
		val dyeArray = Array("dyeBlack", "dyeRed", "dyeGreen", "dyeBrown", "dyeBlue", "dyePurple", "dyeCyan", "dyeLightGray", "dyeGray", "dyePink", "dyeLime", "dyeYellow", "dyeLightBlue", "dyeMagenta", "dyeOrange", "dyeWhite")
		val lampArray = for (col <- LampColor.vals) yield buildStack(1, col) _

		import mods.ft975.util.RecipeUtil._
		for (col <- dyeArray zip lampArray;
				 inverted <- Array(true, false)) {

			addRecipe(col._2(LampShape.Block, inverted),
				List(
					"GrG",
					"rCr",
					"GRG"),
				List(
					'G' -> Right(glassPane),
					'C' -> Left(col._1),
					'r' -> Right(redstoneDust),
					'R' -> Right(if (inverted) redstoneTorch else redstoneDust)
				)
			)

			addRecipe(col._2(LampShape.Caged, inverted),
				List(
					"IrI",
					"rCr",
					"SRS"),
				List(
					'I' -> Right(ironPane),
					'C' -> Left(col._1),
					'r' -> Right(redstoneDust),
					'R' -> Right(if (inverted) redstoneTorch else redstoneDust),
					'S' -> Right(stoneSlab)
				)
			)

			addRecipe(col._2(LampShape.Panel, inverted),
				List(
					"GCG",
					"rRr"),
				List(
					'C' -> Left(col._1),
					'G' -> Right(glassPane),
					'r' -> Right(redstoneDust),
					'R' -> Right(if (inverted) redstoneTorch else redstoneDust)
				)
			)

			addRecipe(col._2(LampShape.Bulb, inverted),
				List(
					"SrS",
					"rCr",
					"SRS"),
				List(
					'C' -> Left(col._1),
					'r' -> Right(redstoneDust),
					'R' -> Right(if (inverted) redstoneTorch else redstoneDust),
					'S' -> Right(stoneSlab)
				)
			)
		}
	}
}