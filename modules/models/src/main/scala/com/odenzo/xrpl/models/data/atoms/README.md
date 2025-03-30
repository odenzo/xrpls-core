# Lets try and define some models for the underlying types, all with a trait XrpType

Can we still use opaque values?
Can we match on XrpType for subtype?


case "UInt16"        => xrpuint16.encode(fromJson[Int](json))
//      case "Transaction"   => xrplTransactionType.encode(fromJson[String](json))
//      case "PathSet"       => xrplPathSet.encode(fromJson[XRPLPathSet](json))
//      case "Vector256"     => xrpvectorhash256.encode(fromJson[String](json)) // String
//      case "AccountIdNoVL" => xrplAccount.encode(fromJson[String](json))
//      case "AccountIdVL"   => variableSizeBytes(VL.xrpvl, xrplAccount).encode(fromJson[String](json))
//      case "UInt8"         => xrpuint8.encode(fromJson[Int](json))
//      case "UInt32"        => xrpuint32.encode(fromJson[Int](json))
//      case "Hash128"       => xrphash128.encode(fromJson[String](json))
//      case "Blob"          => xrpblob.encode(fromJson[String](json)) // String
//      case "Amount"        => xrplAmount.encode(fromJson[XRPLAmount](json)) // XRP or Fiat Amount
//      case "Hash256"       => xrphash256.encode(fromJson[String](json)) // String
//      case "Hash160"       => xrphash160.encode(fromJson[String](json)) // String
//      case "UInt64"        => xrpulong64.encode(fromJson[ULong](json)) // ULong
//      case "STObject"      => xrpstobject.encode(fromJson[JsonObject](json)) // List(Json->Json) not JSonObjectYet
//      // case "STArray"     => xrpstarray.encode(fromJson[Int](json)) // List (Json->Json)
//
//      // case "Validation"  => xrpError[Int]("Validation NIMP").encode(json2(json)) // Int
//      // case "LedgerEntry" => xrpError[Int]("LedgerEntry NIMP").encode(json2(json)) // Int
//      //  case "Done"        => xrpError[Int]("DONE datatype not understood").encode(json2(json))
//