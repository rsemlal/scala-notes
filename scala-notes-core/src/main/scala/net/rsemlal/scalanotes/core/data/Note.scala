package net.rsemlal.scalanotes.core.data

import scala.collection.immutable.{ Map => ImMap }

object Note {
  type Note = GenericNote[NoteRef]
}

trait INoteInfo {
  val title: String
  val content: String
  val metadata: ImMap[String, String]
}

case class NoteInfo(val title: String,
  val content: String,
  val metadata: ImMap[String, String]) extends INoteInfo

/**
 * Classe Note, contient les informations d'une note.
 */
sealed trait GenericNote[+TNoteRef <: NoteRef] extends INoteInfo {
  val ref: TNoteRef
}

case class SecretNote(val ref: SecretNoteRef, infos: INoteInfo) extends GenericNote[SecretNoteRef] {
  val title = infos.title
  val content = infos.content
  val metadata = infos.metadata
}

case class ClearNote(val ref: ClearNoteRef, infos: INoteInfo) extends GenericNote[ClearNoteRef] {
  val title = infos.title
  val content = infos.content
  val metadata = infos.metadata
}