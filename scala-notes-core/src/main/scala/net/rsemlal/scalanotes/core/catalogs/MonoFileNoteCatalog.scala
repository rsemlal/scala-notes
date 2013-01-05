package net.rsemlal.scalanotes.core.catalogs

import net.rsemlal.scalanotes.core.NoteCatalog
import net.rsemlal.scalanotes.core.data.NoteRef
import scala.collection.Traversable
import net.rsemlal.scalanotes.core.data.INoteInfo
import java.io.File
import scala.xml.XML
import scala.xml.Elem
import scala.xml.Node
import net.rsemlal.scalanotes.core.ScalaNoteExceptions
import scala.collection.mutable.Queue
import net.rsemlal.scalanotes.core.data.SecretNoteRef
import scala.collection.immutable.Map

object MonoFileNoteCatalog {
  val root = <noterefs/>
  def root(subnodes: Traversable[Node]) =
    <noterefs>{ subnodes }</noterefs>

  def noteNode(name: String,
               encrypted: Boolean,
               password: String,
               title: String,
               content: String,
               metadata: Map[String, String]) =
    <note name={ name } encrypted={ encrypted.toString }>
      <password>{ password }</password>
      <title>{ title }</title>
      <content>{ content }</content>
      <metadata>
        {
          metadata.map(entry ⇒
            <entry key={ entry._1 } value={ entry._2 }/>)
        }
      </metadata>
    </note>

  object tags {
    val root = "noterefs"
    val note = "note"
    val password = "password"
    val title = "title"
    val content = "content"
    val metadata = "metadata"
    val entry = "entry"
  }

  object attrs {
    val name = "@name"
    val encrypted = "@encrypted"
    val key = "@key"
    val value = "@value"
  }

}

abstract class MonoFileNoteCatalog(index: File) extends NoteCatalog {
  import MonoFileNoteCatalog._

  private def loadXML = if (index.isFile) {
    try {
      if (index.length > 0)
        XML.loadFile(index)
      else
        root
    } catch {
      case e ⇒ throw new ScalaNoteExceptions.CatalogIOException(this,
        "Erreur de lecture du fichier '%s'".format(index), e)
    }
  } else
    root

  private def parseRef(node: Node) = {
    val encrypted = (node \ attrs.encrypted).headOption.map(_.text.toBoolean).getOrElse(false)
    try {
      val name = (node \ attrs.name).head.text.trim
      if (encrypted) {
        val passwordHash = (node \ tags.password).head.text.trim
        this.createRefUsingHash(name, passwordHash)
      } else
        this.createRef(name)
    } catch {
      case e ⇒ throw new ScalaNoteExceptions.CatalogIOException(this,
        "Attribut manquant, dans le noeud: " + node, e)
    }
  }

  private def parseNoteInfos(node: Node) = {
    try {
      val title = (node \ tags.title).head.text.trim
      val content = (node \ tags.content).head.text.trim
      val metadata = ((node \ tags.metadata \ tags.entry) map { entry ⇒
        {
          val key = (entry \ attrs.key).head.text
          val value = (entry \ attrs.value).head.text
          (key, value)
        }
      }).toMap

      this.createInfos(title, content, metadata)
    } catch {
      case e ⇒ throw new ScalaNoteExceptions.CatalogIOException(this,
        "Attribut manquant, dans le noeud: " + node, e)
    }
  }

  private def findNode(name: String) =
    ((loadXML \ tags.note) filter (node ⇒ parseRef(node).name == name)).headOption

  def allNoteRefs =
    (loadXML \ tags.note) map (parseRef _)

  def absoluteReadNote(ref: NoteRef) = findNode(ref.name) match {
    case Some(node) ⇒ parseNoteInfos(node)
    case None       ⇒ throw new ScalaNoteExceptions.UnkownNoteRefException(ref, this, null)
  }

  def absolutePutNote(ref: NoteRef, infos: INoteInfo) = {
    val doc = loadXML
    val notes = doc \ tags.note

    val nodes = new Queue[Node]()
    notes.foreach(nodes.enqueue(_))

    val node = noteNode(name = ref.name,
      encrypted = ref.isEncrypted,
      password = if (ref.isInstanceOf[SecretNoteRef])
        ref.asInstanceOf[SecretNoteRef].passwordHash
      else "",
      title = infos.title,
      content = infos.content,
      metadata = infos.metadata)

    nodes.enqueue(node)

    val result = root(nodes)
    XML.save(index.getAbsolutePath, result, XML.encoding, true, null)
  }

  def getRefByName(name: String) = findNode(name).map(parseRef _)

  def generateLocationForName(name: String): String = "X"

}