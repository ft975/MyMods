package mods.ft975.util.render.values

import java.lang.{Float => float}
import java.util.InputMismatchException
import scala.xml.{Node, Elem}

class Vector3(val i: Double, val j: Double, val k: Double) {
	def -(o: Vector3): Vector3 = Vector3(i - o.i, j - o.j, k - o.k)
	def +(o: Vector3): Vector3 = Vector3(o.i + i, o.j + j, o.j + k)
	def *(o: Vector3): Vector3 = Vector3(o.i * i, o.j * j, o.j * k)
	def x(o: Vector3): Vector3 = Vector3(j * o.k - k * o.j, k * o.i - i * o.k, i * o.j - j * o.i)
	def normalize: Vector3 = {
		val fac = length
		Vector3(i / fac, j / fac, k / fac)
	}
	val length: Float = math.sqrt((i * i) + (j * j) + (k * k)).toFloat

	def toXML: Elem = <vec3>
		<i>
			{i}
		</i> <j>
			{j}
		</j> <k>
			{k}
		</k>
	</vec3>
}
object Vector3 {

	def apply(i: Double, j: Double, k: Double) = new Vector3(i, j, k)

	def fromXML(xml: Node): Vector3 = {
		xml match {
			case <vec3>
				<i>
					{i}
					</i> <j>
				{j}
				</j> <k>
				{k}
				</k>
				</vec3> => Vector3(i.text.toFloat, j.text.toFloat, k.text.toFloat)
			case _ => throw new InputMismatchException(xml.toString())
		}
	}
}

@inline class Vector2(val vals: Long) extends AnyVal {
	def u: Float = float.intBitsToFloat((vals >> 32).toInt)
	def v: Float = float.intBitsToFloat((vals >> 0).toInt)

	def +(o: Vector2) = Vector2(u + o.u, v + o.v)
	def -(o: Vector2) = Vector2(u - o.u, v - o.v)
	def *(o: Vector2) = Vector2(u * o.u, v * o.v)

	def normalize = {
		val len = length
		Vector2(u / len, v / len)
	}

	def length = math.sqrt((u * u) + (v * v)).toFloat

	def toXML: Elem = <vec2>
		<u>
			{u}
		</u> <v>
			{v}
		</v>
	</vec2>
}
object Vector2 {
	def apply(u: Float, v: Float) = new Vector2(float.floatToRawIntBits(u).toLong << 32 | float.floatToRawIntBits(v).toLong)
	def apply(u: Double, v: Double) = new Vector2(float.floatToRawIntBits(u.toFloat).toLong << 32 | float.floatToRawIntBits(v.toFloat).toLong)

	def fromXML(xml: Node): Vector2 = {
		xml match {
			case <vec2>
				<u>
					{u}
					</u> <v>
				{v}
				</v>
				</vec2> => Vector2(u.text.toFloat, v.text.toFloat)
			case _ => throw new InputMismatchException(xml.toString())
		}
	}
}