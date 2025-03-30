# XRPLS Model Classes

Most of this library is busy work making the models, with some experimentation.

The `models` module has the majority of data models you will need to use.
The base package is `com.odenzo.xrpl.models` and all packages describe below are relative to that.


## Top Level Command API Models


All the top-level XRPL `commands` and in the package `api.commands`.
These are automatically applied/reformated to WebSocket or RPC format
in the communication pipeline.

The models all have Circe Json Codecs defined.
These may differ a little bit from the canonical models, with helpers or subset.

There use should be pretty self-explanatory, exceptions noted in XXX


These have the general pattern:

```scala 3
object CMD externds XrpCommand[CMD.Rq,CMD.Rs] {

    case class Rq(foo:String, bar:String) externds XrpCommandRq {
        val command:Command = Command.CMD
    }

    case class Rs(back:String) extends XrpCommandRs
}
```

## Transaction API Models

The transactions top level models has a core definition, e.g. `OfferCreateTx` with the common variables seperated
out into `api.transactions.support.TxCommon`

The core models are found in `api.transactions` with the Circe Json Codecs,
and the SCODEC Codecs in `scodecs`.
The SCODEC codecs convert to-from XRPL Binary Format.

The communication pipeline merges the `TxCommon` and the `OfferCreateTx` as part 
of the signing and submission process.

Currently the Sign, SignFor, and Submit models are not explicitly exposed and 
generally are not directly used.
