package mods.ft975.util.render.values

import java.util.InputMismatchException
import scala.xml.{Node, Elem}

case class UV(u: Double, v: Double) {
	def toXML: Elem = <uv><u>{u}</u><v>{v}</v></uv>
}

object UV {
	def fromXML(xml: Node): UV = {
		xml match {
			case <uv><u>{u}</u><v>{v}</v></uv> => UV(u.text.toDouble, v.text.toDouble)
			case _ => throw new InputMismatchException(xml.toString())
		}
	}
}
