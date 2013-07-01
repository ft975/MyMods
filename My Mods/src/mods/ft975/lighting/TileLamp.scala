package mods.ft975.lighting

import net.minecraft.tileentity.TileEntity
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.packet.{Packet, Packet132TileEntityData}
import net.minecraft.network.INetworkManager
import mods.ft975.lighting.render.RenderUtil
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraftforge.common.ForgeDirection
import net.minecraft.util.AxisAlignedBB
import mods.ft975.lighting.Shapes.{Bulb, Panel, Caged}
import mods.ft975.util.BlockHelper

class TileLamp extends TileEntity {
	override def updateEntity() { setRedstoneState(worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord)) }

	var color: Colors = null
	var shape: Shapes = null
	var inverted: Boolean = false
	var side: ForgeDirection = null

	def isOn: Boolean = { getBlockMetadata == 1 }

	def setRedstoneState(redstoneOn: Boolean) {
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, if (inverted && redstoneOn) 0 else if (inverted && !redstoneOn) 1 else if (!inverted && redstoneOn) 1 else 0, 2)
		DebugOnly(logInfo("Inverted: " + inverted + " & IsOn: " + isOn))
	}

	override def readFromNBT(NBTag: NBTTagCompound) {
		super.readFromNBT(NBTag)
		color = Colors.fromID(NBTag.getByte("C"))
		shape = Shapes.fromID(NBTag.getByte("S"))
		inverted = NBTag.getBoolean("I")
		side = ForgeDirection.getOrientation(NBTag.getByte("P"))
	}

	override def writeToNBT(NBTag: NBTTagCompound) {
		super.writeToNBT(NBTag)
		NBTag.setByte("S", if (shape != null) shape.meta else -1)
		NBTag.setByte("C", if (color != null) color.meta else -1)
		NBTag.setBoolean("I", inverted)
		NBTag.setByte("P", if (side != null) side.ordinal.toByte else ForgeDirection.UNKNOWN.ordinal.toByte)
	}

	override def getDescriptionPacket: Packet = {
		val tc: NBTTagCompound = new NBTTagCompound
		this.writeToNBT(tc)
		new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 2, tc)
	}

	override def onDataPacket(net: INetworkManager, pkt: Packet132TileEntityData) {
		super.onDataPacket(net, pkt)
		readFromNBT(pkt.customParam1)
	}

	override def getRenderBoundingBox: AxisAlignedBB = AxisAlignedBB.getAABBPool.getAABB(0 + xCoord, 0 + yCoord, 0 + zCoord, 1 + xCoord, 1 + yCoord, 1 + zCoord)

	override def canUpdate: Boolean = false

	override def toString: String = "TileLamp @" + xCoord + ", " + yCoord + ", " + zCoord + ", With values: " + color + ", " + shape + ", isOn: " + isOn

	@SideOnly(Side.CLIENT)
	override def shouldRenderInPass(pass: Int): Boolean = { RenderUtil.renderPass = pass; true }
}

object TileLamp {
	private var prevShape: Shapes = Shapes.Block

	def getAABBFromTile(te: TileLamp): AxisAlignedBB = {
		if (te != null) {
			getAABBFromShape(te.shape, te.side)
		} else {
			AxisAlignedBB.getAABBPool.getAABB(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F)
		}
	}

	private def getAABBFromShape(s: Shapes, side: ForgeDirection): AxisAlignedBB = {
		if (s != null) prevShape = s

		s match {
			case Caged => BlockHelper.getSidedAABB(0.1875F, 0, 0.1875F, 0.8125F, 0.4375F, 0.8125F, side)
			case Shapes.Block => AxisAlignedBB.getAABBPool.getAABB(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F)
			case Panel => BlockHelper.getSidedAABB(0.0F, 0.0F, 0.0F, 1.0F, .0625F, 1.0F, side)
			case Bulb => BlockHelper.getSidedAABB(0.125F, 0, 0.125F, 0.875F, 0.40625F, 0.875F, side)
			case _ => {
				DebugOnly {new Exception("Invalid shape " + s).printStackTrace()}
				getAABBFromShape(prevShape, side)
			}
		}
	}
}




