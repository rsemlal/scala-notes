package net.rsemlal.scalanotes.core.data

import java.io.File


sealed trait NoteRef
  extends EncryptionAbility {
  val name: String;
  val file: File
}

case class SecretNoteRef(
  val name: String,
  val file: File,
  val passwordHash: String) extends NoteRef with SecretNoteRefEncryptionAbility

case class ClearNoteRef(val name: String,
  val file: File) extends NoteRef with ClearNoteRefEncryptionAbility