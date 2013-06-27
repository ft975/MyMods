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
import mods.ft975.util.{BlockTESR, BlockHelper}
import net.minecraft.entity.player.EntityPlayer

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
		world.getBlockTileEntity(x, y, z) match {
			case te: TileLamp => if (te.isOn) 15 else 0
			case _ => 15
		}
		15
	}

	override def harvestBlock(par1World: World, par2EntityPlayer: EntityPlayer, par3: Int, par4: Int, par5: Int, par6: Int) {}

	override def onBlockHarvested(par1World: World, par2: Int, par3: Int, par4: Int, par5: Int, par6EntityPlayer: EntityPlayer) { super.onBlockHarvested(par1World, par2, par3, par4, par5, par6EntityPlayer) }

	override def breakBlock(w: World, x: Int, y: Int, z: Int, meta: Int, par6: Int) {
		BlockHelper.dropItemStack(List(
			ItemLamp.buildStack(w.getBlockTileEntity(x, y, z).asInstanceOf[TileLamp]))
			, w, x, y, z)

		super.breakBlock(w, x, y, z, meta, par6)
	}

	override def getLightOpacity(world: World, x: Int, y: Int, z: Int): Int = 0

	override def getRenderBlockPass: Int = 1

	override def canRenderInPass(pass: Int): Boolean = true
}