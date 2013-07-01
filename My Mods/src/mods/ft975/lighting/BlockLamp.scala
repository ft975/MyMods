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
import mods.ft975.util.BlockTESR
import java.util

class BlockLamp(id: Int) extends BlockContainer(id, Material.redstoneLight) with BlockTESR {
	setLightValue(-1)

	override def getPickBlock(target: MovingObjectPosition, world: World, x: Int, y: Int, z: Int): ItemStack = {
		val tempTe = world.getBlockTileEntity(x, y, z)
		val te = tempTe match {
			case tempTe: TileLamp => tempTe
			case _ => throw new ClassCastException
		}
		DebugOnly {log.log(Level.INFO, ItemLamp.getData(ItemLamp.buildStack(1, te.color, te.shape, te.isOn)).toString())}
		ItemLamp.buildStack(1, te.color, te.shape, te.isOn)
	}

	override def getLightValue(iba: IBlockAccess, x: Int, y: Int, z: Int): Int = {
		val te = iba.getBlockTileEntity(x, y, z).asInstanceOf[TileLamp]
		//	DebugOnly(logInfo(te))
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

	override def isBlockNormalCube(world: World, x: Int, y: Int, z: Int): Boolean = {
		val te = world.getBlockTileEntity(x, y, z).asInstanceOf[TileLamp]
		if (te != null) te.shape == Shapes.Block
		else true
	}

	override def onBlockEventReceived(par1World: World, par2: Int, par3: Int, par4: Int, par5: Int, par6: Int): Boolean = super.onBlockEventReceived(par1World, par2, par3, par4, par5, par6)

	override def onBlockAdded(wrd: World, x: Int, y: Int, z: Int) {
		super.onBlockAdded(wrd, x, y, z)
		DebugOnly(logInfo("Block added"))
		onNeighborBlockChange(wrd, x, y, z, 1)
	}

	override def getSelectedBoundingBoxFromPool(wrd: World, x: Int, y: Int, z: Int): AxisAlignedBB = getCollisionBoundingBoxFromPool(wrd, x, y, z)

	override def setBlockBoundsBasedOnState(iba: IBlockAccess, x: Int, y: Int, z: Int) {
		val te = iba.getBlockTileEntity(x, y, z).asInstanceOf[TileLamp]
		setBlockBounds(TileLamp.getAABBFromTile(te))
	}

	override def getCollisionBoundingBoxFromPool(wrd: World, x: Int, y: Int, z: Int): AxisAlignedBB = {
		val te = wrd.getBlockTileEntity(x, y, z).asInstanceOf[TileLamp]
		val aabb = TileLamp.getAABBFromTile(te)
		aabb.minX += x
		aabb.minY += y
		aabb.minZ += z
		aabb.maxX += x
		aabb.maxY += y
		aabb.maxZ += z
		aabb
	}

	def setBlockBounds(aabb: AxisAlignedBB) {
		this.minX = aabb.minX
		this.minY = aabb.minY
		this.minZ = aabb.minZ
		this.maxX = aabb.maxX
		this.maxY = aabb.maxY
		this.maxZ = aabb.maxZ
	}

	def createNewTileEntity(world: World): TileEntity = new TileLamp()

	@SideOnly(Side.CLIENT)
	override def getRenderBlockPass: Int = 1

	@SideOnly(Side.CLIENT)
	override def registerIcons(iR: IconRegister) {}

	@SideOnly(Side.CLIENT)
	override def canRenderInPass(pass: Int): Boolean = pass == 0 || pass == 1

	@SideOnly(Side.CLIENT)
	override def getIcon(id: Int, meta: Int): Icon = Block.stone.getIcon(Block.stone.blockID, 0)
}