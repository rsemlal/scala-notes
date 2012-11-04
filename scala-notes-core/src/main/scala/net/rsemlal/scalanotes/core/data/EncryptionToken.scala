package net.rsemlal.scalanotes.core.data

sealed trait EncryptionToken {
  def isUnlocked: Boolean
}

case class LockedEncryptionToken(val passwordHash: String) extends EncryptionToken {
  override def isUnlocked = false
}

case class UnlockedEncryptionToken(val password: String) extends EncryptionToken {
  override def isUnlocked = true
}