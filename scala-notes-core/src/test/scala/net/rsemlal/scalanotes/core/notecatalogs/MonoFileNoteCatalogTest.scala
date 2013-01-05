package net.rsemlal.scalanotes.core.notecatalogs

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import net.rsemlal.scalanotes.core.catalogs.MonoFileNoteCatalog
import net.rsemlal.scalanotes.core.services.impl.MD5HasherService
import net.rsemlal.scalanotes.core.services.impl.UTF8StringEncoderService
import net.rsemlal.scalanotes.core.services.impl.XorEncryptorService
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import java.io.File
import scala.util.Random
import org.apache.commons.io.FileUtils
import net.rsemlal.scalanotes.core.services.impl.ISO8859_1StringEncoderService

class MonoFileNoteCatalogTest extends NoteCatalogTest("",
  new MonoFileNoteCatalog(
    File.createTempFile("scala-note", ".note-catalog")) with MD5HasherService with XorEncryptorService with ISO8859_1StringEncoderService)