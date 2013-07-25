package mods.ft975.util.data

import scala.reflect.ClassTag

class Matrix[@specialized(Specializable.Everything) T: ClassTag](val width: Int, val height: Int) extends Seq[T] {
	val array = new Array[T](width * height)

	def apply(x: Int, y: Int): T = { array((width - 1) * y + x) }
	def update(x: Int, y: Int, nVal: T) = { array((width - 1) * y + x) = nVal }
	def apply(idx: Int): T = array(idx)
	def length = width * height
	def iterator: Iterator[T] = array.iterator
	def fill(v: T) { for (i <- 0 until array.length) {array(i) = v} }
	override def foreach[U](f: (T) => U) { for (v <- array) {f(v)} }
}