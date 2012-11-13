package net.rsemlal.scalanotes.core.services

import net.rsemlal.scalanotes.core.data.UnlockedEncryptionToken

trait EncryptorService {
  implicit val encryptor: Encryptor
}

trait Encryptor {
  def encrypt(text: String, password: String): String
  def encrypt(text: String, token: UnlockedEncryptionToken): String =
    encrypt(text, token.password)

  def decrypt(text: String, password: String): String
  def decrypt(text: String, token: UnlockedEncryptionToken): String =
    decrypt(text, token.password)

}