package net.rsemlal.scalanotes.core.services.impl

import scala.Array.canBuildFrom

import net.rsemlal.scalanotes.core.services.Encryptor
import net.rsemlal.scalanotes.core.services.EncryptorService
import net.rsemlal.scalanotes.core.services.StringEncoder
import net.rsemlal.scalanotes.core.services.StringEncoderService

trait XorEncryptorService extends EncryptorService with StringEncoderService {
  lazy val encryptor = new XorEncryptor(stringEncoder)
}

class XorEncryptor(stringEncoder: StringEncoder) extends Encryptor {
  private def xor(text: Array[Byte], password: Array[Byte]) = {
    //TODO make it beautiful
    val l = for {
      i ← 0 to (text.length - 1)
      c = text(i)
      p = password(i % password.length)
    } yield {
      c.^(p).toByte
    }

    l.toArray[Byte]
  }

  def encrypt(text: String, password: String) = {
    val textBytes = stringEncoder.stringToBytes(text)
    val passwordBytes = stringEncoder.stringToBytes(password)
    stringEncoder.bytesToHex(xor(textBytes, passwordBytes))
  }

  def decrypt(text: String, password: String) = {
    val textBytes = stringEncoder.hexToBytes(text)
    val passwordBytes = stringEncoder.stringToBytes(password)
    stringEncoder.bytesToString(xor(textBytes, passwordBytes))
  }
}