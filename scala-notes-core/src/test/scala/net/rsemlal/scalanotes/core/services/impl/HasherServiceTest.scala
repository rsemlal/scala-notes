package net.rsemlal.scalanotes.core.services.impl

import org.junit.runner.RunWith
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers

import net.rsemlal.scalanotes.core.data.LockedEncryptionToken
import net.rsemlal.scalanotes.core.services.HasherService

@RunWith(classOf[JUnitRunner])
abstract class HasherServiceTest extends FunSpec with HasherService
    with ShouldMatchers {

  val text1 = "MATH. Relation réflexive. Relation qui lie certains couples " +
    "d'éléments d'un ensemble et qui est vérifiée si ces deux éléments sont" +
    " identiques. La réflexivité (la relation R relie toujours un élément x" +
    " de E à lui-même) (...) sur un graphe orienté: une relation réflexive " +
    "crée une boucle à chaque sommet (Warusfel, Math. mod.,1969, p. 74)."
  val text2 = "A lion and lioness asleep in the midday sun appear to portray" +
    " deep human emotion. The lion shows benign ownership while the " +
    "lioness expresses deep contentment in her relationship with him."

  describe(hasher.getClass.getSimpleName + " hashing") {
    it(".hash should return different results for different inputs") {
      val hash1 = hasher.hash(text1)
      val hash2 = hasher.hash(text2)
      hash2 should not be (hash1)
    }

    it("should unlock token") {
      val truepassword = text2
      val falsepassword = text1
      val hash = hasher.hash(truepassword)
      val ltoken = new LockedEncryptionToken(hash)

      assert(hasher.unlockTocken(truepassword, ltoken).isDefined,
        "Cannot unlock token with true password")

      assert(hasher.unlockTocken(falsepassword, ltoken).isEmpty,
        "Token unlocked with false password")
    }
  }
}

final class MD5HasherServiceTest
  extends HasherServiceTest
  with MD5HasherService
  with UTF8StringEncoderService
final class SHA1HasherServiceTest
  extends HasherServiceTest
  with SHA1HasherService
  with UTF8StringEncoderService