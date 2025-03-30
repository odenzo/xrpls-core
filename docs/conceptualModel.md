# Conceptual Model of Library

## Commands
It aims to provide models for command requests and responses, eliminating boilerplate, particularly on Responses.

The commands are modeled in the package `com.odenzo.xrpl.models.api.commands` with other supporting models.
You create the command rq, and send off to an XRPLEngine (either RPC or WebSocket):

```scala 3
  def listAccountCurrencies(
                             address: AccountAddress
                           )(using engine: XrplEngine): IO[AccountCurrencies.Rs] = {
  val rq                                                        = AccountCurrencies.Rq(address, validated)
  val result: IO[XrplEngineCommandResult[AccountCurrencies.Rs]] =
    engine.send[AccountCurrencies.Rq, AccountCurrencies.Rs](rq)
  val warnings                                                  = result.map(_.warnings)
  val ledgerInfo: IO[Option[ResponseLedgerInfo]]                = result.map(_.ledgerInfo)
  val rs: IO[AccountCurrencies.Rs]                              = result.map(_.rs)
  rs
}
```
On an error status in the result, or trouble executing the command an Exception is raised within the IO.

You will see some duplicated information, especially in this example.
See the differences between the WebSocket and RPC based engines here.


## Transactions

Transactions go through a few more phases per the XRPL spec.
The canonical example is transferring XRP between two accounts.

```scala 3
 def transferXrp(
      from: Wallet,
      to: AccountAddress,
      drops: CurrencyAmount.Drops,
      txCommon: TxCommon = txCommomDefault,
  )(using rpc: XrplEngine): IO[Submit.Rs] = {
    val paymentRq = PaymentTx(
      account     = from.accountAddress,
      amount      = drops,
      destination = to,
      invoiceID   = None,
      paths       = None,
      sendMax     = None,
      deliverMin  = None,
    )

  val response: IO[XrplEngineTxnResult] = rpc.sendTxn(txCommon, paymentRq, from)
  val submitted: IO[Submit.Rs]          = response.map(_.submitted)
  submitted
  }
```

Each transaction API model is in the package `com.odenzo.xrpl.models.api.transactions`

Common transaction fields are seperated out into TxCommon, things like memos and time dependant information.
While some fields are auto-fillable, its best practice to fill them in.
The TxCommon is enriched (or status) and merged with the Transaction (e.g. PaymentTx).

The `sendTxn` function will then use the Sign and Submit commands. If errors occur they are raised in IO context.
After a succesful return, the txn has been `applied` to the ledger, but it has not been validated and finalized.
The basic sendTxn function (in WebSocket, check RPC) will then block on a low-cost fiber until the validation result
has occured.
If validation fails, then an erorr is raised. On success it (currently) doesn't return validation details, and it
expected you just use a command to lookup the transaction in the validated ledger.

More advanced functionality, including Tickets are in the XRPLToolkit module (not merged yet), the communications
module has some basic functionality to check validation of pending transactions each time a ledger is validated etc.
