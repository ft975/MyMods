package mods.ft975.light

import net.minecraft.item.{ItemStack, ItemBlock}
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.world.World
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.common.ForgeDirection
import java.util.logging.Level
import cpw.mods.fml.relauncher.{SideOnly, Side}
import mods.ft975.util.BitUtil

class ItemLamp(itemID: Int) extends ItemBlock(itemID) {

	setHasSubtypes(true)

	override def shouldPassSneakingClickToBlock(world: World, par4: Int, par5: Int, par6: Int): Boolean = super.shouldPassSneakingClickToBlock(world, par4, par5, par6)

	override def placeBlockAt(stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float, metadata: Int): Boolean = {
		if (super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata)) {
			val te = world.getBlockTileEntity(x, y, z)
			val tile = te match {
				case te: TileLamp => te
				case _ => throw new ClassCastException
			}
			val data = ItemLamp.getData(stack)
			tile.color = data._1
			tile.shape = data._2
			tile.inv = data._3
			tile.side = ForgeDirection.getOrientation(side).getOpposite
			tile.setRedstoneState(world.isBlockIndirectlyGettingPowered(x, y, z))
			true
		} else {
			false
		}
	}

	override def getUnlocalizedName(iS: ItemStack): String = {
		val data = ItemLamp.getData(iS)

		val shape = data._2.unLocName
		val col = data._1.name
		val inverted = data._3
		"item." + true + col + shape + "lamp" + inverted
	}

	@SideOnly(Side.CLIENT)
	override def getSubItems(n1: Int, n2: CreativeTabs, iList: java.util.List[_]) {
		iList.asInstanceOf[java.util.List[ItemStack]].addAll(subtypes)
	}

	@SideOnly(Side.CLIENT)
	lazy val subtypes = {
		val list = new java.util.ArrayList[ItemStack]
		for (inverted: Boolean <- List(true, false);
				 sha: Shapes <- Shapes.vals;
				 col: Colors <- Colors.vals) {
			list.add(ItemLamp.buildStack(1, col, sha, inverted))
			DebugOnly {log.log(Level.INFO, "Sub Item Added: " + col + sha)}
		}
		list
	}

	override def canPlaceItemBlockOnSide(wrd: World, x: Int, y: Int, z: Int, side: Int, player: EntityPlayer, iStack: ItemStack): Boolean = {
		if (ItemLamp.getData(iStack)._2 != Shapes.Block)
			wrd.isBlockSolidOnSide(x, y, z, ForgeDirection.getOrientation(side)) && super.canPlaceItemBlockOnSide(wrd, x, y, z, side, player, iStack)
		else
			super.canPlaceItemBlockOnSide(wrd, x, y, z, side, player, iStack)
	}
}

/*
	Item Damage Format:
	Nibble 0: Color
	Nibble 1: Shape
	Nibble 2: 0 is regular, 1 is inverted
 */
object ItemLamp {
	def buildStack(cnt: Int, col: Colors, sap: Shapes, isOn: Boolean): ItemStack = {
		val iS = new ItemStack(LightingMod.blockLamp, cnt)
		iS.setItemDamage(BitUtil.getShortFromNibbles(Array[Byte](col.meta, sap.meta, if (isOn) 1 else 0, 0)))
		iS
	}

	def buildStack(cnt: Int, dam: Short): Option[ItemStack] = {
		if (isValidDamage(dam)) {
			val data = getData(dam)
			buildStack(cnt, data._1, data._2, data._3)
		}
		None
	}

	def buildStack(te: TileLamp): ItemStack = {
		DebugOnly {logInfo(te)}
		if (te != null && te.color != null && te.shape != null)
			buildStack(1, te.color, te.shape, te.inv)
		else
			new ItemStack(0, 0, 0)
	}

	private def isValidDamage(damage: Short): Boolean = {
		val info = BitUtil.splitShortToNibbles(damage)
		info(0) <= 15 && info(0) >= 0 && info(1) <= Shapes.vals.size && info(1) >= 0 && (info(3) == 1 || info(3) == 0)
	}

	def getData(input: Short): (Colors, Shapes, Boolean) = {
		if (!dataMap.contains(input)) {
			val info = BitUtil.splitShortToNibbles(input)
			dataMap += input ->(Colors.fromID(info(0)), Shapes.fromID(info(1)), info(2) == 1)
		}
		dataMap(input)
	}

	def getData(iStack: ItemStack): (Colors, Shapes, Boolean) = {
		getData(iStack.getItemDamage.toShort)
	}

	private val dataMap = collection.mutable.Map.empty[Short, (Colors, Shapes, Boolean)]
}

