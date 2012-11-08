package net.rsemlal.scalanotes.core.services

trait StringEncoderService {
  val stringEncoder: StringEncoder
}

trait StringEncoder {
  def stringToBytes(string: String): Array[Byte]
  def bytesToString(bytes: Array[Byte]): String

  def hexToBytes(hex: String): Array[Byte]
  def bytesToHex(bytes: Array[Byte]): String
}