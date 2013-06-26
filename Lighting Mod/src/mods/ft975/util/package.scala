package mods.ft975

import java.util.logging.Level._
import java.util.logging.Logger

package object util {
	var log: Logger = null
	private val isDebug = true

	def DebugOnly(op: => Unit) {
		if (isDebug) op
	}

	def logInfo(a: Any) {
		log.log(INFO, tS(a))
	}

	def tS(a: Any): String = {
		if (a != null) a.toString else "() NULL"
	}
}
