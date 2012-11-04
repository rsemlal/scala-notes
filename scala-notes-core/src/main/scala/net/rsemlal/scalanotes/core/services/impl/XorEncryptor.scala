package net.rsemlal.scalanotes.core.services.impl

import net.rsemlal.scalanotes.core.services.Encryptor
import net.rsemlal.scalanotes.core.services.EncryptorService
import net.rsemlal.scalanotes.core.services.StringEncoder
import net.rsemlal.scalanotes.core.services.StringEncoderService

trait XorEncryptorService extends EncryptorService with StringEncoderService {
  val encryptor = new XorEncryptor(stringEncoder)
}

class XorEncryptor(stringEncoder: StringEncoder) extends Encryptor {
  private def xor(text: Array[Byte], password: Array[Byte]) = {
    //TODO make it beautiful
    var i = 0
    for {
      c <- text;
      p = password(i)
    } yield {
      i = (i + 1) % password.length
      c.^(p).toByte
    }
  }

  def encrypt(text: String, password: String) = {
    val textBytes = stringEncoder.stringToBytes(text)
    val passwordBytes = stringEncoder.stringToBytes(text)
    stringEncoder.bytesToHex(xor(textBytes, passwordBytes))
  }
  
  def decrypt(text: String, password: String) = {
    val textBytes = stringEncoder.hexToBytes(text)
    val passwordBytes = stringEncoder.stringToBytes(text)
    stringEncoder.bytesToString(xor(textBytes, passwordBytes))
  }
}