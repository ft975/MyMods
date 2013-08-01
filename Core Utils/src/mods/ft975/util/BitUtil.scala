package mods.ft975.util

object BitUtil {

	final val byte  : Byte = 8
	final val nibble: Byte = 4

	def splitShortToNibbles(x: Short): Array[Byte] = {
		val byteArray = new Array[Byte](4)
		byteArray(0) = ((x & 0x000F) >> 0).toByte
		byteArray(1) = ((x & 0x00F0) >> 4).toByte
		byteArray(2) = ((x & 0x0F00) >> 8).toByte
		byteArray(3) = ((x & 0xF000) >> 12).toByte

		byteArray
	}

	def splitIntToNibbles(x: Int): Array[Byte] = {
		val byteArray = new Array[Byte](8)
		byteArray(0) = ((x & 0x0000000F) >> 0).toByte
		byteArray(1) = ((x & 0x000000F0) >> 4).toByte
		byteArray(2) = ((x & 0x00000F00) >> 8).toByte
		byteArray(3) = ((x & 0x0000F000) >> 12).toByte

		byteArray(4) = ((x & 0x000F0000) >> 16).toByte
		byteArray(5) = ((x & 0x00F00000) >> 20).toByte
		byteArray(6) = ((x & 0x0F000000) >> 24).toByte
		byteArray(7) = ((x & 0xF0000000) >> 28).toByte

		byteArray
	}


	def getShortFromNibbles(x: Array[Byte]): Short = {
		var r: Short = 0
		for (i <- 0 to 3) {
			r = (r | x(i) << i * 4).toShort
		}
		r
	}

	def getIntFromNibbles(x: Array[Byte]): Int = {
		var r: Int = 0
		for (i <- 0 to 7) {
			r = r | x(i) << i * 4
		}
		r
	}
}
