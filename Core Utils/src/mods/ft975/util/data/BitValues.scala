package mods.ft975.util.data

import mods.ft975.util.BitUtil.{byte => b, nibble => n}

@inline class BitLong(val v: Long) extends AnyVal {
	def nibbleSize: Byte = 16
	def byteSize: Byte = 8

	def nibble(pos: Byte): Byte = ((v >> ((nibbleSize - pos) * n)) & 0xF).toByte
	def byte(pos: Byte): Short = ((v >> ((byteSize - pos) * b)) & 0xFF).toShort
}

@inline class BitInt(val v: Int) extends AnyVal {
	def nibbleSize: Byte = 8
	def byteSize: Byte = 4

	def nibble(pos: Byte): Byte = ((v >> ((nibbleSize - pos) * n)) & 0xF).toByte
	def byte(pos: Byte): Short = ((v >> ((byteSize - pos) * b)) & 0xFF).toShort
}

@inline class BitShort(val v: Short) extends AnyVal {
	def nibbleSize: Byte = 4
	def byteSize: Byte = 2

	def nibble(pos: Byte): Byte = ((v >> ((nibbleSize - pos) * n)) & 0xF).toByte
	def byte(pos: Byte): Short = ((v >> ((byteSize - pos) * b)) & 0xFF).toShort
}