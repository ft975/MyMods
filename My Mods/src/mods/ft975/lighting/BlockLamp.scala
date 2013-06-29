package mods.ft975.lighting

import net.minecraft.block.{Block, BlockContainer}
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.{IBlockAccess, World}
import net.minecraft.item.ItemStack
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IconRegister
import net.minecraft.util.{AxisAlignedBB, MovingObjectPosition, Icon}
import cpw.mods.fml.relauncher.{Side, SideOnly}
import java.util.logging.Level
import mods.ft975.util.{BlockHelper, BlockTESR}
import java.util
import mods.ft975.lighting.Shapes.{Bulb, Panel, Caged}

class BlockLamp(id: Int) extends BlockContainer(id, Material.redstoneLight) with BlockTESR {
	@SideOnly(Side.CLIENT)
	override def getIcon(id: Int, meta: Int): Icon = Block.stone.getIcon(Block.stone.blockID, 0)

	@SideOnly(Side.CLIENT)
	override def registerIcons(iR: IconRegister) {
	}

	override def getPickBlock(target: MovingObjectPosition, world: World, x: Int, y: Int, z: Int): ItemStack = {
		val tempTe = world.getBlockTileEntity(x, y, z)
		val te = tempTe match {
			case tempTe: TileLamp => tempTe
			case _ => throw new ClassCastException
		}
		println(te)
		DebugOnly {
			log.log(Level.INFO, ItemLamp.getData(ItemLamp.buildStack(1, te.color, te.shape, te.isOn)).toString())
		}
		ItemLamp.buildStack(1, te.color, te.shape, te.isOn)
	}

	def createNewTileEntity(world: World): TileEntity = new TileLamp()

	override def getLightValue(world: IBlockAccess, x: Int, y: Int, z: Int): Int = {
		val te = world.getBlockTileEntity(x, y, z).asInstanceOf[TileLamp]
		if (te != null && te.isOn) 15 else 0
	}

	override def getBlockDropped(world: World, x: Int, y: Int, z: Int, metadata: Int, fortune: Int): util.ArrayList[ItemStack] = {
		val te = world.getBlockTileEntity(x, y, z).asInstanceOf[TileLamp]
		val al = new java.util.ArrayList[ItemStack](1)
		if (te != null) al.add(ItemLamp.buildStack(te))
		al
	}

	override def onNeighborBlockChange(wrd: World, x: Int, y: Int, z: Int, blockID: Int) {
		val powered = wrd.isBlockIndirectlyGettingPowered(x, y, z)
		val te = wrd.getBlockTileEntity(x, y, z)
		if (te != null) {
			te.asInstanceOf[TileLamp].setRedstoneState(powered)
		}
	}

	override def setBlockBoundsBasedOnState(iba: IBlockAccess, x: Int, y: Int, z: Int) {
		val te = iba.getBlockTileEntity(x, y, z).asInstanceOf[TileLamp]
		if (te != null) {
			te.shape match {
				case Caged => setBlockBounds(BlockHelper.getSidedAABB(0.1875F, 0, 0.1875F, 0.8125F, 0.4375F, 0.8125F, te.side))
				case Shapes.Block => setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F)
				case Panel => setBlockBounds(BlockHelper.getSidedAABB(0.0F, 0.0F, 0.0F, 1.0F, .0625F, 1.0F, te.side))
				case Bulb => setBlockBounds(BlockHelper.getSidedAABB(0.125F, 0, 0.125F, 0.875F, 0.40625F, 0.875F, te.side))
			}
		} else {
			setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F)
		}
	}

	def setBlockBounds(aabb: AxisAlignedBB) {
		this.minX = aabb.minX
		this.minY = aabb.minY
		this.minZ = aabb.minZ
		this.maxX = aabb.maxX
		this.maxY = aabb.maxY
		this.maxZ = aabb.maxZ
	}

	override def getLightOpacity(world: World, x: Int, y: Int, z: Int): Int = 0

	@SideOnly(Side.CLIENT)
	override def getRenderBlockPass: Int = 1

	@SideOnly(Side.CLIENT)
	override def canRenderInPass(pass: Int): Boolean = pass == 0 || pass == 1
}