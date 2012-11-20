package net.rsemlal.scalanotes.core.services

trait StringEncoderService {
  implicit val stringEncoder: StringEncoder
}

trait StringEncoder {
  val charset: String
  def stringToBytes(string: String): Array[Byte]
  def bytesToString(bytes: Array[Byte]): String

  def hexToBytes(hex: String): Array[Byte]
  def bytesToHex(bytes: Array[Byte]): String
}