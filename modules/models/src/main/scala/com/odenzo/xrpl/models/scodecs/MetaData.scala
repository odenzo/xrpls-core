package com.odenzo.xrpl.models.scodecs

import com.odenzo.xrpl.models.data.fields.ids.FieldId
import com.odenzo.xrpl.models.internal.definitions.{ FieldMetaData, XrpConstantsSetup }
import com.tersesystems.blindsight.{ Condition, LoggerFactory }
import scodec.bits.BitVector

/**
  * This is a bridge to the semi-static metadata that is loaded from
  * definitions.json now.
  */
object MetaData {

  private val log = LoggerFactory.getLogger.withCondition(Condition.never)
  def touch()     = ()

  private def findByNumber[T, U](data: Vector[(T, U)])(code: U): T =
    data.find(_._2 == code).getOrElse(throw IllegalAccessException(s"Unknown MetaData for Code  $code"))._1

  /**
    * Given a TypeCode from the FieldId find the name for the Ripple Data Type
    * (e.g. STObject, UINT8..)
    */
  def getDataTypeName(id: FieldId): String      = XrpConstantsSetup.dataTypeMeta.getFromUnsafe(id.dataTypeCodeAsInt)
  def getLedgerEntryName(num: Int): String      = XrpConstantsSetup.ledgerEntryTypeMeta.getFromUnsafe(num)
  def getLedgerEntryTypeCode(name: String): Int = XrpConstantsSetup.ledgerEntryTypeMeta.getToUnsafe(name)
  def getTransactionName(num: Int): String      = XrpConstantsSetup.txnTypeMeta.getFromUnsafe(num)

  def getTransactionTypeCode(name: String): Int = {
    log.debug(s"Looking Up TranactionTypeCode from $name")
    val code = XrpConstantsSetup.txnTypeMeta.getToUnsafe(name)
    log.debug(s"Transaction Code `$name` -> $code")
    code
  }

  /**
    * When encoding JSON we use the JSON member name as the field name to lookup
    * the FieldMetaData
    */
  def getFieldMetaData(fieldName: String): FieldMetaData = {
    log.debug(s"Getting Field Entry for [$fieldName]")
    XrpConstantsSetup.fieldNameToFieldMeta(fieldName)
  }

  /**
    * When encoding JSON we use the JSON member name as the field name to lookup
    * the FieldMetaData
    */
  def getFieldMetaData(fieldIdBinary: BitVector): FieldMetaData = {
    XrpConstantsSetup.fieldIdToFieldMeta.get(fieldIdBinary) match
      case Some(value) => value
      case None        => throw IllegalArgumentException(s"Field Entry  MetaData doesn't contain FieldId $fieldIdBinary")
  }

  /**
    * If we just have the name, and no FieldId we know to alias this for the
    * lookup. Why like this, dunno, legacy
    */
  def aliasFieldName(name: String): String = if name == "TransactionType" then "Transaction" else name

}
