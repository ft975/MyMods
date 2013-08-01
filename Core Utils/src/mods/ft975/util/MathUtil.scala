package mods.ft975.util

object MathUtil {
	def clamp(v: Float, c: Float): Float = if (v > c) c else if (v < 0) 0 else v
	def clamp(v: Double, c: Double): Double = if (v > c) c else if (v < 0) 0 else v
	def clamp(v: Byte, c: Byte): Byte = if (v > c) c else if (v < 0) 0 else v
	def clamp(v: Short, c: Short): Short = if (v > c) c else if (v < 0) 0 else v
	def clamp(v: Int, c: Int): Int = if (v > c) c else if (v < 0) 0 else v
	def clamp(v: Long, c: Long): Long = if (v > c) c else if (v < 0) 0 else v
}
