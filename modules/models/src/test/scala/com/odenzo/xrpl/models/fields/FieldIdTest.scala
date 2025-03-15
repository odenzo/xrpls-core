package com.odenzo.xrpl.models.fields

import com.odenzo.xrpl.common.utils.BlindsightLogging
import com.odenzo.xrpl.models.data.models.fields.ids.FieldId
import com.tersesystems.blindsight.LoggerFactory

class FieldIdTest extends munit.FunSuite with BlindsightLogging {

  private val log = LoggerFactory.getLogger

  test("FieldId") {
    val id = FieldId.fromFieldCodeAndDataTypeCode(2, 25)
    log.info(s"FieldCode ${id.fieldCode.toHex}")
    log.info(s"FieldCode ${id.fieldCode.toBin}")
    log.info(s"FieldCode Size ${id.fieldCode.size}")
    log.info(s"DataCode ${id.dataTypeCode.toHex}")
    log.info(s"DataCode ${id.dataTypeCode.toBin}")
    log.info(s"Packed ${id.packedBinary.toBin}")
    log.info(s"Packed ${id.packedBinary.toHex}")
  }
}
