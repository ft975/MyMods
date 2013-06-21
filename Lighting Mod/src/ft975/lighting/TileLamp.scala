package ft975.lighting

import net.minecraft.tileentity.TileEntity
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.packet.{Packet, Packet132TileEntityData}
import net.minecraft.network.INetworkManager

class TileLamp extends TileEntity {
	var color: Colors = null
	var shape: Shapes = null

	override def readFromNBT(NBTag: NBTTagCompound) {
		super.readFromNBT(NBTag)
		color = Colors.fromID(NBTag.getByte("color"))
		shape = Shapes.fromID(NBTag.getByte("shape"))
	}

	override def writeToNBT(NBTag: NBTTagCompound) {
		super.writeToNBT(NBTag)
		NBTag.setByte("shape", shape.meta)
		NBTag.setByte("color", color.meta)
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

	override def canUpdate: Boolean = true

	override def toString: String = "TileLamp @" + xCoord + ", " + yCoord + ", " + zCoord + ", With values: " + color + ", " + shape
}




