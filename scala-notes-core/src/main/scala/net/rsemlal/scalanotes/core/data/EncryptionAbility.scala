package net.rsemlal.scalanotes.core.data

trait EncryptionAbility {
  val isEncrypted: Boolean
}

trait SecretNoteRefEncryptionAbility extends EncryptionAbility {
  override val isEncrypted = true
  protected val passwordHash: String
  def generateEncryptionToken = LockedEncryptionToken(passwordHash)
}

trait ClearNoteRefEncryptionAbility extends EncryptionAbility {
  override val isEncrypted = false
}