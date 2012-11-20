package net.rsemlal.scalanotes.core.services.impl

import org.junit.runner.RunWith
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers

import net.rsemlal.scalanotes.core.services.StringEncoderService

@RunWith(classOf[JUnitRunner])
abstract class JavaCharsetStringEncoderServiceTest extends FunSpec with ShouldMatchers
    with StringEncoderService {

  val string = "MATH. Relation réflexive. Relation qui lie certains couples " +
    "d'éléments d'un ensemble et qui est vérifiée si ces deux éléments sont" +
    " identiques. La réflexivité (la relation R relie toujours un élément x" +
    " de E à lui-même) (...) sur un graphe orienté: une relation réflexive " +
    "crée une boucle à chaque sommet (Warusfel, Math. mod.,1969, p. 74)."

  val hex = "4D4154482E2052656C6174696F6E2072C3A9666C65786976652E2052656C6" +
    "174696F6E20717569206C6965206365727461696E7320636F75706C6573206427C3A96C" +
    "C3A96D656E7473206427756E20656E73656D626C6520657420717569206573742076C3A" +
    "972696669C3A96520736920636573206465757820C3A96CC3A96D656E747320736F6E74" +
    "206964656E7469717565732E204C612072C3A9666C657869766974C3A920286C6120726" +
    "56C6174696F6E20522072656C696520746F756A6F75727320756E20C3A96CC3A96D656E" +
    "742078206465204520C3A0206C75692D6DC3AA6D652920282E2E2E292073757220756E2" +
    "0677261706865206F7269656E74C3A93A20756E652072656C6174696F6E2072C3A9666C" +
    "6578697665206372C3A96520756E6520626F75636C6520C3A02063686171756520736F6" +
    "D6D65742028576172757366656C2C204D6174682E206D6F642E2C313936392C20702E20" +
    "3734292E"

  describe(stringEncoder.charset + " - bytesToString ° stringToBytes") {
    it("should be identity") {
      val textAsBytes = stringEncoder.stringToBytes(string)
      val bytesAsString = stringEncoder.bytesToString(textAsBytes)
      withClue("bytesToString(stringToBytes([string = '%s'])):\n".format(string) +
        "expected [string]\n" +
        "found '%s'".format(bytesAsString)) {
        bytesAsString should be === string
      }
    }
  }

  describe(stringEncoder.charset + " - bytesToHex ° hexToBytes") {
    it("should be identity") {
      val hexAsBytes = stringEncoder.hexToBytes(hex)
      val bytesAsHex = stringEncoder.bytesToHex(hexAsBytes)

      withClue("bytesToHex(hexToBytes(textAsHex))") {
        bytesAsHex should be === hex
      }
    }
  }
}

final class UTF8StringEncoderServiceTest extends JavaCharsetStringEncoderServiceTest
  with UTF8StringEncoderService

final class ISO8859_1StringEncoderServiceTest extends JavaCharsetStringEncoderServiceTest
  with ISO8859_1StringEncoderService