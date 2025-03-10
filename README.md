
# ripple-core-libs V2

Scala 3 for latest JSON-RPC and Websocket support Communication with XRPLD servers iterative.

- Most commands and transactions are implemented
- Supports FIAT Currencies and XRP
- DOES NOT SUPPORT:
  - Payment Channels (May eventually implement)
  - NFT Stuff (No Plans)
  - PeerPorts stuff, this is simple enough to just write by hand. I may do a little HTTP4S object to implement
- Subcribe and Unsubscribe are handled under Communication within a Framework, particularly ValidatorList

- Optimizing Ergonomics.
  - ledger_hash, ledger_current_index, ledger_index in Response are moved to ResponseMetaData
    and in requests we expect only ledger_index XOR ledger_hash.
    ledgerIndex type supports `validated`, `current`, `closed` as well as numbers.
    Will add some helpers, either via trait, or through a derives that enhances the Rq object???

## Transaction Support Breakdown
