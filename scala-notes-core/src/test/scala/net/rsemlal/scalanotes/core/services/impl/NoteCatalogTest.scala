package net.rsemlal.scalanotes.core.services.impl

import org.junit.runner.RunWith
import net.rsemlal.scalanotes.core.NoteCatalog
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSpec
import net.rsemlal.scalanotes.core.data.NoteInfo

@RunWith(classOf[JUnitRunner])
abstract class NoteCatalogTest(val name: String) extends NoteCatalog with FunSpec
    with ShouldMatchers {

  describe(name) {
    it("should put notes") {
      val infos = NoteInfo.create(title = "titre 1", content = "contenu 1")

    }
  }
}