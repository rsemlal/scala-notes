package net.rsemlal.scalanotes.core.services.impl

import org.junit.runner.RunWith
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers

import net.rsemlal.scalanotes.core.data.UnlockedEncryptionToken
import net.rsemlal.scalanotes.core.services.EncryptorService

@RunWith(classOf[JUnitRunner])
abstract class EncryptorServiceTest extends FunSpec with EncryptorService
    with ShouldMatchers {
  val text = "A lion and lioness asleep in the midday sun appear to portray" +
    " deep human emotion. The lion shows benign ownership while the " +
    "lioness expresses deep contentment in her relationship with him."

  val password = "password"
  val utoken = new UnlockedEncryptionToken(password)

  describe(encryptor.getClass.getSimpleName) {
    it("encrypt([text], [password]) == encrypt([text], Token([password])) ") {
      val encrypted1 = encryptor.encrypt(text, utoken)
      val encrypted2 = encryptor.encrypt(text, password)
      encrypted1 should be === encrypted2
    }

    it("encrypt([text], [password1]) != encrypt([text], [password2]) ") {
      val encrypted1 = encryptor.encrypt(text, password)
      val encrypted2 = encryptor.encrypt(text, password + "a44")
      encrypted1 should not(be === encrypted2)
    }

    it("encrypt([text1], [password]) != encrypt([text2], [password]) ") {
      val encrypted1 = encryptor.encrypt(text, password)
      val encrypted2 = encryptor.encrypt(text + "a", password)
      encrypted1 should not(be === encrypted2)
    }

    it("decrypt(encrypt([text], [password]), [password]) == [text]") {
      val encrypted = encryptor.encrypt(text, password)
      val decrypted = encryptor.decrypt(encrypted, password)
      decrypted should be === text
    }
  }
}

class XorEncryptorServiceTest
  extends EncryptorServiceTest
  with XorEncryptorService
  with UTF8StringEncoderService