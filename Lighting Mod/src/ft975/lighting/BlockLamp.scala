package ft975.lighting

import net.minecraft.block.{Block, BlockContainer}
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import net.minecraft.item.ItemStack
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IconRegister
import net.minecraft.util.{MovingObjectPosition, Icon}
import cpw.mods.fml.relauncher.{Side, SideOnly}

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
		ItemLamp.buildStack(1, te.color, te.shape)
	}

	def createNewTileEntity(world: World): TileEntity = new TileLamp()
}