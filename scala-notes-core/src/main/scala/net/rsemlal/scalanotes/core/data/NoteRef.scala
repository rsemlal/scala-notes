package net.rsemlal.scalanotes.core.data

import java.io.File

sealed trait NoteRef
    extends EncryptionAbility {
  val name: String
  val location: String
}

case class SecretNoteRef(
  val name: String,
  val location: String,
  val passwordHash: String) extends NoteRef with SecretNoteRefEncryptionAbility

case class ClearNoteRef(val name: String,
                        val location: String) extends NoteRef with ClearNoteRefEncryptionAbility