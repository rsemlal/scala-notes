package net.rsemlal.scalanotes.core.notecatalogs

import net.rsemlal.scalanotes.core.NoteCatalog
import net.rsemlal.scalanotes.core.ScalaNoteExceptions.CatalogIOException
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
abstract class NoteCatalogTest(val name: String, catalogGen: ⇒ NoteCatalog) extends FunSpec
    with ShouldMatchers {

  def title(i: Int) = "titre " + i
  def content(i: Int) = "contenu " + i
  def name(i: Int) = "note " + i
  def password(i: Int) = "password " + i

  describe("handling clear notes") {
    val catalog = catalogGen
    val fixture = 1

    it("should be empty in the begining") {
      catalog.allNoteRefs should have size (0)
    }

    it("should add clear note") {
      val infos = catalog.createInfos(title = title(fixture), content = content(fixture))
      val ref = catalog.createRef(name(fixture))

      catalog.write(ref, infos)
      catalog.allNoteRefs should have size (1)
    }

    it("should retrieve ref by name") {
      catalog.getRefByName(name(fixture)) should be(Some(catalog.createRef(name(fixture))))
      catalog.getRefByName(name(fixture + 100)) should be('empty)
    }

    it("should retrieve note infos") {
      val ref = catalog.createRef(name(fixture))
      val note = catalog.read(ref)
      note.ref should be(ref)
      note.title should be(title(fixture))
      note.content should be(content(fixture))
    }
  }

  describe("handling secret notes") {
    val catalog = catalogGen
    val fixture = 1

    it("should be empty in the begining") {
      catalog.allNoteRefs should have size (0)
    }

    it("should add secret note") {
      val infos = catalog.createInfos(title = title(fixture), content = content(fixture))
      val ref = catalog.createRef(name(fixture), password(fixture))

      val utoken = catalog.unlockTocken(password(fixture), ref.generateEncryptionToken)
      utoken should be('defined)

      catalog.write(ref, utoken.get, infos)

      catalog.allNoteRefs should have size (1)
    }

    it("should retrieve ref by name") {
      catalog.getRefByName(name(fixture)) should be(Some(catalog.createRef(name(fixture), password(fixture))))
      catalog.getRefByName(name(fixture + 100)) should be('empty)
    }

    it("should retrieve note infos") {
      val ref = catalog.createRef(name(fixture), password(fixture))
      val ltoken = ref.generateEncryptionToken
      val utoken = catalog.unlockTocken(password(fixture), ltoken)
      val note = catalog.read(ref, utoken.get)
      note.ref should be(ref)
      note.title should be(title(fixture))
      note.content should be(content(fixture))
    }
  }
}