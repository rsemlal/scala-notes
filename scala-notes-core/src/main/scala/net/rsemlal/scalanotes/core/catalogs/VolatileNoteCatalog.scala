package net.rsemlal.scalanotes.core.catalogs

import net.rsemlal.scalanotes.core.NoteCatalog
import net.rsemlal.scalanotes.core.data.NoteRef
import scala.collection.Traversable
import net.rsemlal.scalanotes.core.services.Hasher
import net.rsemlal.scalanotes.core.data.INoteInfo
import net.rsemlal.scalanotes.core.ScalaNoteExceptions

/**
 * Catalogue de notes volatil (non persistant).
 */
abstract class VolatileNoteCatalog extends NoteCatalog {

  private val memory = new scala.collection.mutable.HashMap[NoteRef, INoteInfo]

  def allNoteRefs(): Traversable[NoteRef] =
    memory.keySet

  def absoluteReadNote(ref: NoteRef): INoteInfo = memory.get(ref) match {
    case Some(infos) ⇒ infos
    case None        ⇒ throw new ScalaNoteExceptions.UnkownNoteRefException(ref, this)
  }

  def absolutePutNote(ref: NoteRef, infos: INoteInfo): Unit =
    memory.put(ref, infos)
}