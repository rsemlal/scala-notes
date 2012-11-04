package net.rsemlal.scalanotes.core.data

import scala.collection.immutable.{ Map => ImMap }

object Note {
  type Note = GenericNote[NoteRef]
}

/**
 * Classe Note, contient les informations d'une note.
 */
sealed trait GenericNote[+TNoteRef <: NoteRef] {
  val ref: TNoteRef;
  val title: String;
  val content: String;
  val metadata: ImMap[String, String]
}

case class SecretNote(val ref: SecretNoteRef,
  val title: String,
  val content: String,
  val metadata: ImMap[String, String]) extends GenericNote[SecretNoteRef]

case class ClearNote(val ref: ClearNoteRef,
  val title: String,
  val content: String,
  val metadata: ImMap[String, String]) extends GenericNote[ClearNoteRef]