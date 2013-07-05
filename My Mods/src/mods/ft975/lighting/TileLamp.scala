package mods.ft975.lighting

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.packet.{Packet, Packet132TileEntityData}
import net.minecraft.network.INetworkManager
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraftforge.common.ForgeDirection
import net.minecraft.util.AxisAlignedBB
import mods.ft975.lighting.Shapes.{Bulb, Panel, Caged}
import mods.ft975.util.AABBUtil
import net.minecraft.world.World
import mods.ft975.util.extensibles.TileExt

class TileLamp extends TileExt {
	override def updateEntity() { setRedstoneState(worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord)) }

	var color: Colors = null
	var shape: Shapes = null
	var inv: Boolean = false
	var side: ForgeDirection = null
	var isOn: Boolean = false

	def setRedstoneState(red: Boolean) {
		isOn = if (inv) !red else red
		worldObj.markBlockForUpdate(x, y, z)
		worldObj.updateAllLightTypes(x, y, z)
		if (metadata == 1) metadata = 0 else metadata = 1
		DebugOnly(logInfo("Inverted: " + inv + " & IsOn: " + isOn))
	}

	override def readFromNBT(NBTag: NBTTagCompound) {
		super.readFromNBT(NBTag)
		color = Colors.fromID(NBTag.getByte("C"))
		shape = Shapes.fromID(NBTag.getByte("S"))
		isOn = NBTag.getBoolean("O")
		inv = NBTag.getBoolean("I")
		side = ForgeDirection.getOrientation(NBTag.getByte("P"))
	}

	override def writeToNBT(NBTag: NBTTagCompound) {
		super.writeToNBT(NBTag)
		NBTag.setByte("S", if (shape != null) shape.meta else -1)
		NBTag.setByte("C", if (color != null) color.meta else -1)
		NBTag.setBoolean("O", isOn)
		NBTag.setBoolean("I", inv)
		NBTag.setByte("P", if (side != null) side.ordinal.toByte else ForgeDirection.UNKNOWN.ordinal.toByte)
	}

	override def getDescriptionPacket: Packet = {
		val tc: NBTTagCompound = new NBTTagCompound
		this.writeToNBT(tc)
		new Packet132TileEntityData(x, y, z, 2, tc)
	}

	override def onDataPacket(net: INetworkManager, pkt: Packet132TileEntityData) {
		super.onDataPacket(net, pkt)
		readFromNBT(pkt.customParam1)
	}

	override def getRenderBoundingBox: AxisAlignedBB = AxisAlignedBB.getAABBPool.getAABB(0 + x, 0 + y, 0 + z, 1 + x, 1 + y, 1 + z)

	override def canUpdate: Boolean = false

	override def toString: String = "TileLamp @" + x + ", " + y + ", " + z + ", With values: " + color + ", " + shape + ", isOn: " + isOn

	override def shouldRefresh(oldID: Int, newID: Int, oldMeta: Int, newMeta: Int, world: World, x: Int, y: Int, z: Int): Boolean = false

	@SideOnly(Side.CLIENT)
	override def shouldRenderInPass(pass: Int): Boolean = { true }
}

object TileLamp {
	def getAABBFromTile(te: TileLamp): AxisAlignedBB = {
		if (te != null) {
			getAABBFromShape(te.shape, te.side)
		} else {
			AxisAlignedBB.getAABBPool.getAABB(0, 0, 0, 1, 1, 1)
		}
	}

	private def getAABBFromShape(s: Shapes, side: ForgeDirection): AxisAlignedBB = {
		s match {
			case Caged => AABBUtil.getSidedAABB(0.1875F, 0, 0.1875F, 0.8125F, 0.4375F, 0.8125F, side)
			case Shapes.Block => AxisAlignedBB.getAABBPool.getAABB(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F)
			case Panel => AABBUtil.getSidedAABB(0.0F, 0.0F, 0.0F, 1.0F, .0625F, 1.0F, side)
			case Bulb => AABBUtil.getSidedAABB(0.125F, 0, 0.125F, 0.875F, 0.40625F, 0.875F, side)
			case _ => {
				DebugOnly {new Exception("Invalid shape " + s).printStackTrace()}
				AxisAlignedBB.getAABBPool.getAABB(0, 0, 0, 1, 1, 1)
			}
		}
	}
}




