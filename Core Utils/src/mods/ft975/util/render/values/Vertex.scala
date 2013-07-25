package mods.ft975.util.render.values

import java.util.InputMismatchException
import scala.xml.{Node, Elem}

case class Vertex(x: Double, y: Double, z: Double) {
	def toXML: Elem = <vert><x>{x}</x><y>{y}</y><z>{z}</z></vert>
}

object Vertex {
	def fromXML(xml: Node): Vertex = {
		xml match {
			case <vert><x>{x}</x><y>{y}</y><z>{z}</z></vert> => Vertex(x.text.toDouble, y.text.toDouble, z.text.toDouble)
			case _ => throw new InputMismatchException(xml.toString())
		}
	}
}

