package mods.ft975.util.render.values

import java.util.InputMismatchException
import scala.xml.Node

@inline class Light(val lvl: Int) extends AnyVal {
	def toXML: Node = <lght>{lvl}</lght>
}

object Light {
	def apply(lvl: Int): Light = new Light(lvl)

	def fromXML(xml: Node): Light = {
		xml match {
			case <lght>{lvl}</lght> => Light(lvl.text.toInt)
			case _ => throw new InputMismatchException(xml.toString())
		}
	}
}