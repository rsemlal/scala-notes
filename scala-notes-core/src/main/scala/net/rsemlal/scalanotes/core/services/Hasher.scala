package net.rsemlal.scalanotes.core.services

import net.rsemlal.scalanotes.core.data.LockedEncryptionToken
import net.rsemlal.scalanotes.core.data.UnlockedEncryptionToken

trait HasherService {
  implicit val hasher: Hasher
}

trait Hasher {
  def hash(text: String): String

  def checkPassword(password: String, token: LockedEncryptionToken) =
    hash(password).equals(token.passwordHash)

  def unlockTocken(password: String, token: LockedEncryptionToken): Option[UnlockedEncryptionToken] =
    if (checkPassword(password, token))
      Some(UnlockedEncryptionToken(password))
    else
      None

  def lockTocken(password: String, token: UnlockedEncryptionToken): LockedEncryptionToken =
    LockedEncryptionToken(hash(password))
}