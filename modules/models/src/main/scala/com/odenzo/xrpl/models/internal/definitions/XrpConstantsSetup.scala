package com.odenzo.xrpl.models.internal.definitions

import _root_.scodec.bits.BitVector
import com.odenzo.xrpl.common.collections.{ IsoMorphicKeyMap, KeyedMap }
import com.odenzo.xrpl.common.utils.CirceUtils
import com.tersesystems.blindsight.{ Condition, LoggerFactory }

import scala.reflect.ClassTag

/**
  * Rushed first cut for lookup. Actual lookup case is just binary for now,
  * hoping for some macro magic later. See MetaData in the bincodec library.
  */
object XrpConstantsSetup {
  import com.odenzo.xrpl.common.utils.ErrorOr.*
  private val log = LoggerFactory.getLogger.withCondition(Condition.never)

  private val baseConfig                                     = loadFromResource()
  val dataTypeMeta: IsoMorphicKeyMap[String, Int]            = toIsoMap(filtered(baseConfig.types))
  val txnTypeMeta: IsoMorphicKeyMap[String, Int]             = toIsoMap(filtered(baseConfig.transactionTypes))
  val ledgerEntryTypeMeta: IsoMorphicKeyMap[String, Int]     = toIsoMap(filtered(baseConfig.ledgerEntryTypes))
  val fieldMetaData: List[FieldMetaData]                     = normalizedFieldEntries(baseConfig.fields, dataTypeMeta)
  val fieldNameToFieldMeta: KeyedMap[String, FieldMetaData]  = createForwardMap(fieldMetaData, t => t.fieldName)
  val fieldIdToFieldMeta: KeyedMap[BitVector, FieldMetaData] = createForwardMap(fieldMetaData, _.fieldId.packedBinary)

  private val done = log.info("Done With Hacky Eager XrpConstantsSetup")
  def touch()      = ()

  inline def createForwardMap[K, T](data: List[T], fn: T => K): KeyedMap[K, T] = KeyedMap.from(data, fn)

  def normalizedFieldEntries(
      fields: List[FieldEntryRaw],
      dataTypeMeta: IsoMorphicKeyMap[String, Int],
  ): List[FieldMetaData] = {
    log.info("Normalized field entries")

    fields.filter(v => v.metadata.nth > 0 && v.metadata.nth < 255).map { (raw: FieldEntryRaw) =>
      val dataCode: Int      = dataTypeMeta.getToUnsafe(raw.metadata.xrpType)
      FieldMetaData(
        fieldName      = raw.name,
        dataTypeName   = raw.metadata.xrpType,
        dataTypeCode   = dataCode,
        nth            = raw.metadata.nth,
        isVLEncoded    = raw.metadata.isVLEncoded,
        isSerialized   = raw.metadata.isSerialized,
        isSigningField = raw.metadata.isSigningField,
      )

    }
  }

  def loadFromResource(resourceName: String = "xrpl-constants.json"): ServerDefinitionsData = {
    log.debug(s"Loading XRPL constants config from reource $resourceName")
    val configE: Either[Exception, ServerDefinitionsData] = for {
      s      <- CirceUtils.loadResourceAsString(resourceName)
      json   <- CirceUtils.parseJson(s)
      config <- json.as[ServerDefinitionsData]
    } yield config
    // log.debug(s"LOading Configuring XRPL constants config: ${pprint.apply(configE)}")
    configE.getOrThrowEither
  }

  /** Filters to needed and makes IsoMap */
  def toIsoMap[A: ClassTag, B: ClassTag](vector: Vector[(A, B)]): IsoMorphicKeyMap[A, B] = {
    IsoMorphicKeyMap.from[A, B](vector)
  }

  /** Add this is needed to an ISO datastructure once we decide how to use */
  // def normalizeFields()
  private def filtered(data: Map[String, Int]): Vector[(String, Int)] =
    log.info(s"Concerting to ISO Structure: ${data.head}")
    data.filter((_, i) => i >= 0).map((n, i) => (n, i)).toVector
}

/**
  * The file `xrpl-constants.json` is a JSON document that describes all the
  * fixed data type and constants needed for XRPL operations. I am not sure
  * where I got this, or if I rolled it myself from the C++ code. Might be a
  * little out of date now.
  *
  * This is a mirror representation of the configuration file with no data
  * modifications or structural changes. Simple JSON parsing. Pre-Processing is
  * all done in Setup.
  */
