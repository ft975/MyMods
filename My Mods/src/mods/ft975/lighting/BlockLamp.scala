package mods.ft975.lighting

import net.minecraft.block.{ITileEntityProvider, Block}
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.{IBlockAccess, World}
import net.minecraft.item.ItemStack
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IconRegister
import net.minecraft.util.{AxisAlignedBB, MovingObjectPosition, Icon}
import cpw.mods.fml.relauncher.{Side, SideOnly}
import java.util.logging.Level
import java.util
import mods.ft975.util.block.BlockTESR
import mods.ft975.util.extensibles.BlockExt
import net.minecraft.client.particle.EffectRenderer
import mods.ft975.util.particle.EntityLampDiggingParticle

class BlockLamp(id: Int) extends BlockExt(id, Material.circuits) with BlockTESR with ITileEntityProvider {
  override def getPickBlock(target: MovingObjectPosition, world: World, x: Int, y: Int, z: Int): ItemStack = {
    val tempTe = world.getBlockTileEntity(x, y, z)
    val te = tempTe match {
      case tempTe: TileLamp => tempTe
      case _ => throw new ClassCastException
    }
    DebugOnly {
      log.log(Level.INFO, ItemLamp.getData(ItemLamp.buildStack(1, te.color, te.shape, te.isOn)).toString())
    }
    ItemLamp.buildStack(1, te.color, te.shape, te.isOn)
  }

  override def getLightValue(iba: IBlockAccess, x: Int, y: Int, z: Int): Int = {
    val te = iba.getBlockTileEntity(x, y, z).asInstanceOf[TileLamp]
    if (te.isOn) 15 else 0
  }

  override def getBlockDropped(world: World, x: Int, y: Int, z: Int, metadata: Int, fortune: Int): util.ArrayList[ItemStack] = {
    val te = world.getBlockTileEntity(x, y, z).asInstanceOf[TileLamp]
    val al = new java.util.ArrayList[ItemStack](1)
    if (te != null) al.add(ItemLamp.buildStack(te))
    al
  }

  override def onNeighborBlockChange(wrd: World, x: Int, y: Int, z: Int, blockID: Int) {
    if (!wrd.isRemote) {
      val powered = wrd.isBlockIndirectlyGettingPowered(x, y, z)
      val te = wrd.getBlockTileEntity(x, y, z)
      if (te != null) {
        te.asInstanceOf[TileLamp].setRedstoneState(powered)
      }
    }
  }

  override def isBlockNormalCube(world: World, x: Int, y: Int, z: Int): Boolean = {
    val te = world.getBlockTileEntity(x, y, z).asInstanceOf[TileLamp]
    if (te != null) te.shape == Shapes.Block
    else true
  }

  override def onBlockAdded(wrd: World, x: Int, y: Int, z: Int) {
    DebugOnly {
      logInfo("Block added")
    }
    onNeighborBlockChange(wrd, x, y, z, 1)
  }

  override def getSelectedBoundingBoxFromPool(wrd: World, x: Int, y: Int, z: Int): AxisAlignedBB = getCollisionBoundingBoxFromPool(wrd, x, y, z)

  def createNewTileEntity(world: World): TileEntity = new TileLamp()

  @SideOnly(Side.CLIENT)
  override def getRenderBlockPass: Int = 1

  @SideOnly(Side.CLIENT)
  override def registerIcons(iR: IconRegister) {
    blockIcon = iR.registerIcon("ft975/lighting:part_block")
  }

  @SideOnly(Side.CLIENT)
  override def canRenderInPass(pass: Int): Boolean = pass == 0 || pass == 1

  @SideOnly(Side.CLIENT)
  override def getIcon(id: Int, meta: Int): Icon = Block.stone.getIcon(Block.stone.blockID, 0)

  override def addBlockHitEffects(wrd: World, tgt: MovingObjectPosition, effRendr: EffectRenderer): Boolean = {
    val x: Int = tgt.blockX
    val y: Int = tgt.blockY
    val z: Int = tgt.blockZ
    val side: Int = tgt.sideHit

    if (wrd.getBlockId(x, y, z) == blockID) {
      val te = wrd.getBlockTileEntity(x, y, z).asInstanceOf[TileLamp]
      val f: Float = 0.1F
      var d0: Double = x + rand.nextDouble * (getBlockBoundsMaxX - getBlockBoundsMinX - f * 2.0F) + f + getBlockBoundsMinX
      var d1: Double = y + rand.nextDouble * (getBlockBoundsMaxY - getBlockBoundsMinY - f * 2.0F) + f + getBlockBoundsMinY
      var d2: Double = z + rand.nextDouble * (getBlockBoundsMaxZ - getBlockBoundsMinZ - f * 2.0F) + f + getBlockBoundsMinZ

      side match {
        case 0 =>
          d1 = y + getBlockBoundsMinY - f
        case 1 =>
          d1 = y + getBlockBoundsMaxY + f
        case 2 =>
          d2 = z + getBlockBoundsMinZ - f
        case 3 =>
          d2 = z + getBlockBoundsMaxZ + f
        case 4 =>
          d0 = x + getBlockBoundsMinX - f
        case 5 =>
          d0 = x + getBlockBoundsMaxX + f
      }

      effRendr.addEffect(new EntityLampDiggingParticle(wrd, d0, d1, d2, 0.0D, 0.0D, 0.0D, blockIcon, te.color.color.R, te.color.color.G, te.color.color.B).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F))
      return true
    }
    false
  }

  override def addBlockDestroyEffects(wrd: World, x: Int, y: Int, z: Int, meta: Int, effRendr: EffectRenderer): Boolean = {
    if (wrd.getBlockId(x, y, z) == blockID) {
      val te = wrd.getBlockTileEntity(x, y, z).asInstanceOf[TileLamp]
      for (j1: Double <- 0.0 until 4.0 by 1;
           k1: Double <- 0.0 until 4.0 by 1;
           l1: Double <- 0.0 until 4.0 by 1) {
        val d0: Double = x + ((j1 + 0.5D) / 4D)
        val d1: Double = y + ((k1 + 0.5D) / 4D)
        val d2: Double = z + ((l1 + 0.5D) / 4D)
        val side: Int = rand.nextInt(6)

        effRendr.addEffect(new EntityLampDiggingParticle(wrd, d0, d1, d2, d0 - x - 0.5D, d1 - y - 0.5D, d2 - z - 0.5D, blockIcon, te.color.color.R, te.color.color.G, te.color.color.B))
      }
      return true
    }
    false
  }
}