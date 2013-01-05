package net.rsemlal.scalanotes.core.services.impl

import java.security.MessageDigest

import net.rsemlal.scalanotes.core.services.Hasher
import net.rsemlal.scalanotes.core.services.HasherService
import net.rsemlal.scalanotes.core.services.StringEncoder
import net.rsemlal.scalanotes.core.services.StringEncoderService

trait MD5HasherService extends HasherService with StringEncoderService {
  lazy val hasher = new MessageDigestHasher("MD5", stringEncoder)
}

trait SHA1HasherService extends HasherService with StringEncoderService {
  lazy val hasher = new MessageDigestHasher("SHA-1", stringEncoder)
}

class MessageDigestHasher(val algo: String, stringEncoder: StringEncoder) extends Hasher {
  private def digester = MessageDigest.getInstance(algo)

  def hash(text: String) =
    stringEncoder.bytesToHex(digester.digest(stringEncoder.stringToBytes(text)))
}