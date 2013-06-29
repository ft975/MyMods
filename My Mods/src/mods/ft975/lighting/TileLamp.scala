package mods.ft975.lighting

import net.minecraft.tileentity.TileEntity
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.packet.{Packet, Packet132TileEntityData}
import net.minecraft.network.INetworkManager
import mods.ft975.lighting.render.RenderUtil
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraftforge.common.ForgeDirection

class TileLamp extends TileEntity {
	var color: Colors = null
	var shape: Shapes = null
	var inverted: Boolean = false
	var side: ForgeDirection = null

	def isOn: Boolean = { worldObj.getBlockMetadata(xCoord, yCoord, zCoord) == 1 }

	def setRedstoneState(redstoneOn: Boolean) {
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, if (inverted && redstoneOn) 0 else if (inverted && !redstoneOn) 1 else if (!inverted && redstoneOn) 1 else 0, 3)
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord)
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

	override def canUpdate: Boolean = false

	override def toString: String = "TileLamp @" + xCoord + ", " + yCoord + ", " + zCoord + ", With values: " + color + ", " + shape + ", isOn: " + isOn

	@SideOnly(Side.CLIENT)
	override def shouldRenderInPass(pass: Int): Boolean = { RenderUtil.renderPass = pass; true }
}




