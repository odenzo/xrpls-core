# Core Communication Functions

Communication in this respect means sending requests to XRPL server,
and basic processing of result messages, detecting errors/warnings etc.



## RPC Communications

Nice and simple synchronous operations (via HTTP4S). Send a XrpCommnd
and get its result back, with the Json decoded into the CMD.Rs model object.

The response  is wrapped with an `XrplCommandRs` model object, that includes
the ledger information, i.e. `ledger_index, ledger_current_index, ledger_hash` fields.

Note these are specific in the request and mostly default to `validated` so are not used too
much.
Not all commands return this information, about 90% do.

```scala 3 mdoc
    val x : XrplCommandResult[Fee.Rq,Fee.Rs] = ???
```
