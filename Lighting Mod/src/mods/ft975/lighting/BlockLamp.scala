package mods.ft975.lighting

import net.minecraft.block.{Block, BlockContainer}
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.{IBlockAccess, World}
import net.minecraft.item.ItemStack
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IconRegister
import net.minecraft.util.{MovingObjectPosition, Icon}
import cpw.mods.fml.relauncher.{Side, SideOnly}
import java.util.logging.Level
import mods.ft975.util.BlockHelper
import net.minecraft.entity.player.EntityPlayer

class BlockLamp(id: Int) extends BlockContainer(id, Material.redstoneLight) {

	@SideOnly(Side.CLIENT)
	override def getIcon(id: Int, meta: Int): Icon = Block.stone.getIcon(Block.stone.blockID, 0)

	@SideOnly(Side.CLIENT)
	override def registerIcons(iR: IconRegister) {
	}

	override def getRenderType: Int = -1

	override def isOpaqueCube: Boolean = false

	override def hasTileEntity: Boolean = true

	override def hasTileEntity(metadata: Int): Boolean = true

	override def renderAsNormalBlock(): Boolean = false

	override def getPickBlock(target: MovingObjectPosition, world: World, x: Int, y: Int, z: Int): ItemStack = {
		val tempTe = world.getBlockTileEntity(x, y, z)
		val te = tempTe match {
			case tempTe: TileLamp => tempTe
			case _ => throw new ClassCastException
		}
		println(te)
		DebugOnly {
			log.log(Level.INFO,
				(ItemLamp.buildStack(1, te.color, te.shape, te.isOn).getTagCompound.getByte("c").toString +
					ItemLamp.buildStack(1, te.color, te.shape, te.isOn).getTagCompound.getByte("s").toString))
		}
		ItemLamp.buildStack(1, te.color, te.shape, te.isOn)
	}

	def createNewTileEntity(world: World): TileEntity = new TileLamp()

	override def shouldSideBeRendered(par1IBlockAccess: IBlockAccess, par2: Int, par3: Int, par4: Int, par5: Int): Boolean = true

	override def getRenderBlockPass: Int = 0

	override def getLightValue(world: IBlockAccess, x: Int, y: Int, z: Int): Int = {
		world.getBlockTileEntity(x, y, z) match {
			case te: TileLamp => if (te.isOn) 15 else 0
			case _ => 15
		}
		15
	}


	override def harvestBlock(par1World: World, par2EntityPlayer: EntityPlayer, par3: Int, par4: Int, par5: Int, par6: Int) {}

	override def breakBlock(w: World, x: Int, y: Int, z: Int, meta: Int, par6: Int) {
		BlockHelper.dropItemStack(List(ItemLamp.buildStack(w.getBlockTileEntity(x, y, z).asInstanceOf[TileLamp])), w, x, y, z)
		super.breakBlock(w, x, y, z, meta, par6)
	}

	override def getLightOpacity(world: World, x: Int, y: Int, z: Int): Int = 0
}