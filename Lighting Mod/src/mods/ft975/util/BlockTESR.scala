package mods.ft975.util

import net.minecraft.block.BlockContainer
import net.minecraft.world.IBlockAccess

trait BlockTESR extends BlockContainer {
	override def shouldSideBeRendered(par1IBlockAccess: IBlockAccess, par2: Int, par3: Int, par4: Int, par5: Int): Boolean = true

	override def getRenderType: Int = -1

	override def isOpaqueCube: Boolean = false

	override def hasTileEntity: Boolean = true

	override def hasTileEntity(metadata: Int): Boolean = true

	override def renderAsNormalBlock(): Boolean = false
}
