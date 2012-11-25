package net.rsemlal.scalanotes.core.notecatalogs

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import net.rsemlal.scalanotes.core.catalogs.VolatileNoteCatalog
import net.rsemlal.scalanotes.core.services.impl.MD5HasherService
import net.rsemlal.scalanotes.core.services.impl.UTF8StringEncoderService
import net.rsemlal.scalanotes.core.services.impl.XorEncryptorService
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

class VolatileNoteCatalogTest
  extends NoteCatalogTest("",
    new VolatileNoteCatalog with MD5HasherService with XorEncryptorService with UTF8StringEncoderService)