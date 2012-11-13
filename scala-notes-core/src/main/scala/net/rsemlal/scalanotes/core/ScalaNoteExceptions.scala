package net.rsemlal.scalanotes.core

import net.rsemlal.scalanotes.core.data.NoteRef

object ScalaNoteExceptions {
  sealed abstract class ScalaNoteException(message: String, innerException: Throwable = null)
    extends Exception(message, innerException)

  final class CatalogIOException(val noteRef: NoteRef, message: String, innerException: Throwable = null)
    extends Exception(message, innerException)
  final class UnkownNoteRefException(val noteRef: NoteRef, message: String, innerException: Throwable = null)
    extends Exception(message, innerException)
  sealed class EncryptionException(val noteRef: NoteRef, message: String, innerException: Throwable = null)
    extends ScalaNoteException(message, innerException)

  final class UnlockedTokenNeededException(_noteRef: NoteRef)
    extends EncryptionException(_noteRef,
      "La note '%s' est encryptée, elle nécessite un token déverouillé".format(_noteRef.name))

  final class NoTokenNeededException(_noteRef: NoteRef)
    extends EncryptionException(_noteRef,
      "La note '%s' n'est pas encryptée, elle ne nécessite pas de token".format(_noteRef.name))
}