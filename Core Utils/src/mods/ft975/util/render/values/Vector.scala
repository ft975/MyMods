package mods.ft975.util.render.values

import java.util.InputMismatchException
import scala.xml.{Node, Elem}

case class Vector(i: Double, j: Double, k: Double) {
	def toXML: Elem = <vec><i>{i}</i><j>{j}</j><k>{k}</k></vec>
}

object Vector {
	def fromXML(xml: Node): Vector = {
		xml match {
			case <vec><i>{i}</i><j>{j}</j><k>{k}</k></vec> => Vector(i.text.toDouble, j.text.toDouble, k.text.toDouble)
			case _ => throw new InputMismatchException(xml.toString())
		}
	}
}
