package com.odenzo.xrpl.models.data.models.atoms

import cats.implicits.*

import io.circe.Decoder.{ Result, decodeString }
import io.circe.Encoder.encodeString
import io.circe.generic.semiauto.deriveDecoder
import io.circe.{ Decoder, Encoder, Json, JsonObject }

import scala.collection.immutable

// TODO: Lowest Priority I actually am tired of the all caps for constants. FistCap enough I think.
enum RippleLogPartitions(val label: String) {

  case AMENDMENTS extends RippleLogPartitions("Amendments")

  case APPLICATION extends RippleLogPartitions("Application")

  case COLLECTOR extends RippleLogPartitions("Collector")

  case CONNECTION_IMPL extends RippleLogPartitions("ConnectionImpl")

  case CONSENSUS extends RippleLogPartitions("Consensus")

  case FEE_VOTE extends RippleLogPartitions("FeeVote")

  case FLOW extends RippleLogPartitions("Flow")

  case HANDLER_LOG extends RippleLogPartitions("HandlerLog")

  case INBOUND_LEDGER extends RippleLogPartitions("InboundLedger")

  case JOB_QUEUE extends RippleLogPartitions("JobQueue")

  case LEDGER extends RippleLogPartitions("Ledger")

  case LEDGER_CLEANER extends RippleLogPartitions("LedgerCleaner")

  case LEDGER_CONSENSUS extends RippleLogPartitions("LedgerConsensus")

  case LEDGER_HISTORY extends RippleLogPartitions("LedgerHistory")

  case LEDGER_MASTER extends RippleLogPartitions("LedgerMaster")

  case LEDGER_TIMING extends RippleLogPartitions("LedgerTiming")

  case LOAD_MANAGER extends RippleLogPartitions("LoadManager")

  case LOAD_MONITOR extends RippleLogPartitions("LoadMonitor")

  case MANIFEST_CACHE extends RippleLogPartitions("ManifestCache")

  case NETWORK_OPS extends RippleLogPartitions("NetworkOPs")

  case NODE_OBJECT extends RippleLogPartitions("NodeObject")

  case OPEN_LEDGER extends RippleLogPartitions("OpenLedger")

  case ORDER_BOOK_DB extends RippleLogPartitions("OrderBookDB")

  case OVERLAY extends RippleLogPartitions("Overlay")

  case PATH_REQUEST extends RippleLogPartitions("PathRequest")

  case PEER extends RippleLogPartitions("Peer")

  case PEER_FINDER extends RippleLogPartitions("PeerFinder")

  case PERF_LOG extends RippleLogPartitions("PerfLog")

  case PROTOCOL extends RippleLogPartitions("Protocol")

  case RPC_HANDLER extends RippleLogPartitions("RPCHandler")

  case RESOLVER extends RippleLogPartitions("Resolver")

  case RESOURCE extends RippleLogPartitions("Resource")

  case SHA_MAP extends RippleLogPartitions("SHAMap")

  case SHA_MAP_STORE extends RippleLogPartitions("SHAMapStore")

  case SERVER extends RippleLogPartitions("Server")

  case TAGGED_CACHE extends RippleLogPartitions("TaggedCache")

  case TIME_KEEPER extends RippleLogPartitions("TimeKeeper")

  case TRANSACTION_ACQUIRE extends RippleLogPartitions("TransactionAcquire")

  case TX_META extends RippleLogPartitions("TxMeta")

  case TXQ extends RippleLogPartitions("TxQ")

  case UNIQUE_NODE_LIST extends RippleLogPartitions("UniqueNodeList")

  case VALIDATIONS extends RippleLogPartitions("Validations")

  case VALIDATOR_LIST extends RippleLogPartitions("ValidatorList")

  case VALIDATOR_SITE extends RippleLogPartitions("ValidatorSite")

  case VIEW extends RippleLogPartitions("View")

  case WAL_CHECKPOINTER extends RippleLogPartitions("WALCheckpointer")

  case WEB_SOCKET extends RippleLogPartitions("WebSocket")

  case BASE extends RippleLogPartitions("base")
}

object RippleLogPartitions {
  given Encoder[RippleLogPartitions] = Encoder.encodeString.contramap(_.label)

  /**
    * Decoder takes the label not the enum value, I should just normalize the
    * enums.
    */
  given Decoder[RippleLogPartitions] = Decoder
    .decodeString
    .emap(v => Either.fromOption(RippleLogPartitions.values.find(_.label == v), s"$v doesn't match a partition"))
}
