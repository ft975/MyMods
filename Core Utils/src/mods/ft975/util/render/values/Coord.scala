package mods.ft975.util.render.values

import java.util.InputMismatchException
import scala.xml.{Node, Elem}

case class Coord(x: Double, y: Double, z: Double) {
	def toXML: Elem = <cord>
		<x>
			{x}
		</x> <y>
			{y}
		</y> <z>
			{z}
		</z>
	</cord>
}

object Coord {
	def fromXML(xml: Node): Coord = {
		xml match {
			case <cord>
				<x>
					{x}
					</x> <y>
				{y}
				</y> <z>
				{z}
				</z>
				</cord> => Coord(x.text.toDouble, y.text.toDouble, z.text.toDouble)
			case _ => throw new InputMismatchException(xml.toString())
		}
	}
}

