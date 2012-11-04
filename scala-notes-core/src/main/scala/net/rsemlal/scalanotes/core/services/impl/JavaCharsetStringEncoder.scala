package net.rsemlal.scalanotes.core.services.impl

import net.rsemlal.scalanotes.core.services.StringEncoderService
import net.rsemlal.scalanotes.core.services.StringEncoder

trait UTF8StringEncoderService extends StringEncoderService {
  val stringEncoder = new JavaCharsetStringEncoder("UTF-8")
}

trait USASCIIStringEncoderService extends StringEncoderService {
  val stringEncoder = new JavaCharsetStringEncoder("US-ASCII")
}

trait ISO8859_1StringEncoderService extends StringEncoderService {
  val stringEncoder = new JavaCharsetStringEncoder("ISO-8859-1")
}

class JavaCharsetStringEncoder(val charset: String) extends StringEncoder {
  private def chunk[T](ls: Traversable[T], size: Int) =
    List.range(0, ls.size, size).map { i => ls.slice(i, i + size) }

  def stringToBytes(string: String) =
    string.getBytes(charset)
    
  def bytesToString(bytes: Array[Byte]) =
    new String(bytes, charset)

  def bytesToHex(bytes: Array[Byte]) =
    if (bytes == null) ""
    else bytes.map(b => String.format("%02X", java.lang.Byte.valueOf(b))).mkString

  def hexToBytes(hex: String) = {
    val cleanInput = hex.collect {
      case c if ("0123456789ABCDEFabcdef".contains(c)) => c.toUpper
    }
    val evenHex = if (cleanInput.length % 2 == 0) cleanInput else "0" + cleanInput
    val hexPairs = chunk(evenHex.toList, 2)
    val bytes = hexPairs.map { pair => java.lang.Integer.parseInt(pair.mkString, 16).toByte }
    bytes.toArray
  }
}