package net.rsemlal.scalanotes.core

import net.rsemlal.scalanotes.core.ScalaNoteExceptions.CatalogIOException
import net.rsemlal.scalanotes.core.ScalaNoteExceptions.UnkownNoteRefException
import net.rsemlal.scalanotes.core.data.ClearNote
import net.rsemlal.scalanotes.core.data.ClearNoteRef
import net.rsemlal.scalanotes.core.data.EncryptionToken
import net.rsemlal.scalanotes.core.data.INoteInfo
import net.rsemlal.scalanotes.core.data.LockedEncryptionToken
import net.rsemlal.scalanotes.core.data.Note.Note
import net.rsemlal.scalanotes.core.data.NoteInfo
import net.rsemlal.scalanotes.core.data.NoteRef
import net.rsemlal.scalanotes.core.data.SecretNote
import net.rsemlal.scalanotes.core.data.SecretNoteRef
import net.rsemlal.scalanotes.core.data.UnlockedEncryptionToken
import net.rsemlal.scalanotes.core.services.Encryptor
import net.rsemlal.scalanotes.core.services.EncryptorService
import net.rsemlal.scalanotes.core.services.HasherService
import net.rsemlal.scalanotes.core.services.Hasher

object NoteCatalog {
  implicit protected def infos2Encryptable(infos: INoteInfo)(implicit encryptor: Encryptor) = new {
    def encrypt(token: UnlockedEncryptionToken) = {
      val etitle = encryptor.encrypt(infos.title, token)
      val econtent = encryptor.encrypt(infos.content, token)
      val emetadata = infos.metadata.map {
        case (key, value) ⇒ (
          encryptor.encrypt(key, token),
          encryptor.encrypt(value, token))
      }
      NoteInfo(etitle, econtent, emetadata)
    }

    def decrypt(token: UnlockedEncryptionToken) = {
      val dtitle = encryptor.decrypt(infos.title, token)
      val dcontent = encryptor.decrypt(infos.content, token)
      val dmetadata = infos.metadata.map {
        case (key, value) ⇒ (
          encryptor.decrypt(key, token),
          encryptor.decrypt(value, token))
      }
      NoteInfo(dtitle, dcontent, dmetadata)
    }
  }

  protected def verify(ref: SecretNoteRef, token: UnlockedEncryptionToken)(implicit hasher: Hasher) = {
    hasher.hash(token.password) == ref.passwordHash
  }
}

/**
 * Catalogue de note, fournit des méthodes pour lire et écrire des notes dans un catalogue.
 */
trait NoteCatalog extends EncryptorService with HasherService {
  import NoteCatalog._

  /**
   * Récupère une liste exhaustive des référence aux notes présentes dans le catalogue.
   *
   * @throws ScalaNoteExceptions.CatalogIOException En cas d'erreur d'entrée/sortie lors de l'accès au catalogue.
   */
  @throws(classOf[ScalaNoteExceptions.CatalogIOException])
  def allNoteRefs: Traversable[NoteRef]

  /**
   * Lit le contenu d'une note du catalogue.
   *
   * @throws ScalaNoteExceptions.UnkownNoteRefException Si la référence à la note n'appartient pas à ce catalogue.
   * @throws ScalaNoteExceptions.CatalogIOException En cas d'erreur d'entrée/sortie lors de l'accès au catalogue.
   */
  @throws(classOf[ScalaNoteExceptions.UnkownNoteRefException])
  @throws(classOf[ScalaNoteExceptions.CatalogIOException])
  protected def absoluteReadNote(ref: NoteRef): INoteInfo

  /**
   * écrit le contenu d'une note dans le catalogue.
   *
   * @throws ScalaNoteExceptions.CatalogIOException En cas d'erreur d'entrée/sortie lors de l'accès au catalogue.
   */
  @throws(classOf[ScalaNoteExceptions.CatalogIOException])
  protected def absolutePutNote(ref: NoteRef, infos: INoteInfo)

  /**
   * Lit le contenu d'une note non encrypté du catalogue.
   *
   * @throws ScalaNoteExceptions.UnkownNoteRefException Si la référence à la note n'appartient pas à ce catalogue.
   * @throws ScalaNoteExceptions.CatalogIOException En cas d'erreur d'entrée/sortie lors de l'accès au catalogue.
   */
  def readNote(ref: ClearNoteRef) = {
    val infos = absoluteReadNote(ref)
    ClearNote(ref, infos)
  }

  /**
   * Lit le contenu d'une note encrypté avec un token du catalogue.
   *
   * @throws ScalaNoteExceptions.UnkownNoteRefException Si la référence à la note n'appartient pas à ce catalogue.
   * @throws ScalaNoteExceptions.CatalogIOException En cas d'erreur d'entrée/sortie lors de l'accès au catalogue.
   * @throws ScalaNoteExceptions.WrongPasswordException En cas de mot de passe incorrect.
   */
  def readNote(ref: SecretNoteRef, token: UnlockedEncryptionToken) = {
    if (verify(ref, token)) {
      val infos = absoluteReadNote(ref)
      val dinfos = infos.decrypt(token)
      SecretNote(ref, dinfos)
    } else {
      throw new ScalaNoteExceptions.WrongPasswordException(ref, token)
      null
    }

  }

  def readNote(ref: SecretNoteRef, token: EncryptionToken): SecretNote = token match {
    case utoken: UnlockedEncryptionToken ⇒ readNote(ref, utoken)
    case _                               ⇒ throw new ScalaNoteExceptions.UnlockedTokenNeededException(ref)
  }

  def readNote(ref: NoteRef, token: UnlockedEncryptionToken): SecretNote = ref match {
    case sref: SecretNoteRef ⇒ readNote(sref, token)
    case _                   ⇒ throw new ScalaNoteExceptions.NoTokenNeededException(ref)
  }

  def readNote(ref: NoteRef, token: LockedEncryptionToken): SecretNote = ref match {
    case cref: ClearNoteRef ⇒ throw new ScalaNoteExceptions.NoTokenNeededException(ref)
    case _                  ⇒ throw new ScalaNoteExceptions.UnlockedTokenNeededException(ref)
  }

  def readNote(ref: NoteRef, token: EncryptionToken): SecretNote = token match {
    case utoken: UnlockedEncryptionToken ⇒ readNote(ref, utoken)
    case ltoken: LockedEncryptionToken   ⇒ readNote(ref, ltoken)
  }

  def readNote(ref: NoteRef, tokenOpt: Option[EncryptionToken] = None): Note = tokenOpt match {
    case Some(token) ⇒ readNote(ref, token)
    case None ⇒ ref match {
      case cref: ClearNoteRef ⇒ readNote(cref)
      case _                  ⇒ throw new ScalaNoteExceptions.UnlockedTokenNeededException(ref)
    }
  }

  /**
   * écrit le contenu d'une note en clair dans le catalogue.
   *
   * @throws ScalaNoteExceptions.CatalogIOException En cas d'erreur d'entrée/sortie lors de l'accès au catalogue.
   */
  def putNote(ref: ClearNoteRef, infos: INoteInfo) = {
    absolutePutNote(ref, infos)
  }

  /**
   * écrit le contenu d'une note encryptée avec un token dans le catalogue.
   *
   * @throws ScalaNoteExceptions.CatalogIOException En cas d'erreur
   * d'entrée/sortie lors de l'accès au catalogue.
   * @throws ScalaNoteExceptions.WrongPasswordException En cas de mot de passe incorrect.
   */
  def putNote(ref: SecretNoteRef, token: UnlockedEncryptionToken, infos: INoteInfo) = {
    if (verify(ref, token)) {
      val einfos = infos.encrypt(token)
      absolutePutNote(ref, einfos)
    } else {
      throw new ScalaNoteExceptions.WrongPasswordException(ref, token)
    }
  }
}