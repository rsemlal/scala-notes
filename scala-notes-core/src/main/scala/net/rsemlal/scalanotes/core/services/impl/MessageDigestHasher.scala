package net.rsemlal.scalanotes.core.services.impl

import net.rsemlal.scalanotes.core.services.HasherService
import net.rsemlal.scalanotes.core.services.Hasher
import java.security.MessageDigest

trait MD5HasherService extends HasherService {
  val hasher = new MessageDigestHasher("MD5")
}

trait SHA1HasherService extends HasherService {
  val hasher = new MessageDigestHasher("SHA-1")
}

class MessageDigestHasher(val algo: String) extends Hasher {
  private def digester = MessageDigest.getInstance(algo)

  def hash(text: String) = new String(digester.digest(text.getBytes))
}