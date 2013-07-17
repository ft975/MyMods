package mods.ft975.light

import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.event.{FMLInitializationEvent, FMLPreInitializationEvent}
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.network.NetworkMod
import net.minecraftforge.common.Configuration
import cpw.mods.fml.common.registry.{GameRegistry, LanguageRegistry}
import net.minecraft.creativetab.CreativeTabs
import cpw.mods.fml.relauncher.{Side, SideOnly}
import cpw.mods.fml.client.registry.ClientRegistry
import net.minecraftforge.client.MinecraftForgeClient
import java.util.logging.Level
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.block.Block
import mods.ft975.light.render.{TileLampRender, ItemLampRender}
import mods.ft975.util.RecipeUtil

@Mod(modid = ModInfo.modID, name = ModInfo.modName, version = ModInfo.version, acceptedMinecraftVersions = ModInfo.versionMinecraft, modLanguage = "scala")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
object LightingMod {
	var config: Configuration = _
	var blockLamp: BlockLamp = _
	var blockIDLamp: Int = _
	var creativeTabLighting: CreativeTabs = _

	@EventHandler
	def preInit(event: FMLPreInitializationEvent) {
		config = new Configuration(event.getSuggestedConfigurationFile)
		mods.ft975.util.log = event.getModLog
		log = event.getModLog
		setupConfig()
	}

	@EventHandler
	def init(event: FMLInitializationEvent) {
		GameRegistry.registerTileEntity(classOf[TileLamp], "ftlamp")
		creativeTabLighting = new CreativeTabs("ftLighting") {
			LanguageRegistry.instance.addStringLocalization("itemGroup.ftLighting", "en_US", "Lighting")
			lazy val icon = ItemLamp.buildStack(0, Colors.Magenta, Shapes.Block, isOn = true)

			override def getIconItemStack = icon
		}

		setupBlocks()
		registerRenders()
		registerCraft()
	}

	@SideOnly(Side.CLIENT)
	def registerRenders() {
		ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileLamp], TileLampRender)
		MinecraftForgeClient.registerItemRenderer(blockIDLamp, ItemLampRender)
	}

	def setupConfig() {
		config.load()
		blockIDLamp = config.getBlock("Lamp_Block", 2401).getInt
		for (i <- 0 to 15) {
			Colors.colArray(i) = config.get("Colors", Colors.colNameArray(i), Colors.colArray(i)).getString
		}
		Colors.darkenFactor = config.get("Special", "Darkening factor, all colors will be multiplied by this", Colors.darkenFactor).getDouble(Colors.darkenFactor).toFloat
		config.save()
	}

	def setupBlocks() {
		blockLamp = new BlockLamp(blockIDLamp)
		blockLamp.setLightOpacity(0)
		GameRegistry.registerBlock(blockLamp, classOf[ItemLamp], "BlockLamp", ModInfo.modID)
		blockLamp.setCreativeTab(creativeTabLighting)
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


	def registerCraft() {
		def buildStack(cnt: Int, col: Colors)(sap: Shapes, isOn: Boolean): ItemStack = ItemLamp.buildStack(cnt, col, sap, isOn)

		val glassPane = new ItemStack(Block.thinGlass)
		val ironPane = new ItemStack(Block.fenceIron)
		val stoneSlab = new ItemStack(Block.stoneSingleSlab, 1, 0)
		val redstoneDust = new ItemStack(Item.redstone)
		val redstoneTorch = new ItemStack(Block.torchRedstoneActive)
		val dyeArray = Array("dyeBlack", "dyeRed", "dyeGreen", "dyeBrown", "dyeBlue", "dyePurple", "dyeCyan", "dyeLightGray", "dyeGray", "dyePink", "dyeLime", "dyeYellow", "dyeLightBlue", "dyeMagenta", "dyeOrange", "dyeWhite")
		val lampArray = for (col <- Colors.vals) yield buildStack(1, col) _

		import RecipeUtil._
		for (col <- dyeArray zip lampArray;
				 inverted <- Array(true, false)) {

			addRecipe(col._2(Shapes.Block, inverted),
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

			addRecipe(col._2(Shapes.Caged, inverted),
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

			addRecipe(col._2(Shapes.Panel, inverted),
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

			addRecipe(col._2(Shapes.Bulb, inverted),
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