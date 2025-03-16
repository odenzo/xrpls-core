# Functional Overview

The majority of functions/Transactions, except NFT tokens, are implemented. Some are tested better than others.

## Transactional Overview


| Txn                         | Implemented | Tested |   |   | Comment                                |
|-----------------------------|-------------|--------|---|---|----------------------------------------|
| AccountDelete               | N           |        |   |   |                                        |
| AccountSet                  |             |        |   |   |                                        |
| AMM*                        | N           |        |   |   |                                        |
| CredentialAccept            | Y           | N      |   |   |                                        |
| CredentialCreate            | Y           | N      |   |   |                                        |
| CredentialDelete            | Y           | N      |   |   |                                        |
| CheckCash                   | Y           | N      |   |   |                                        |
| CheckCreate                 | Y           | N      |   |   |                                        |
| CheckCancel                 | Y           | N      |   |   |                                        |
| Clawback                    | Y           | N      |   |   |                                        |
| DIDDelete                   | Y           | N      |   |   |                                        |
| DIDSet                      | Y           | N      |   |   |                                        |
| EscrowCancel                | Y           | N      |   |   |                                        |
| EscrowCreate                | Y           | N      |   |   |                                        |
| EscrowFinish                | Y           | N      |   |   |                                        |
| MPTokenAuthorize            | Pending     |        |   |   |                                        |
| MPTokenIssuanceCreate       | Pending     |        |   |   |                                        |
| MPTokenIssuanceDestroy      | Pending     |        |   |   |                                        |
| MPTokenIssuanceSet          | Pending     |        |   |   |                                        |
| NFT*                        | N           |        |   |   | Will Not Implement                     |
| OfferCreate                 | Y           |        |   |   |                                        |
| OfferCancel                 | Y           |        |   |   |                                        |
| OracleDelete                | N           |        |   |   | Low Priority                           |
| OracleSet                   | N           |        |   |   | Low Priority                           |
| PaymentChannelCreate        | N           |        |   |   | Low Priority                           |
| PaymentChannelFund          | N           |        |   |   | Low Priority                           |
| PaymentChannelClaim         | N           |        |   |   | Low Priority                           |
| PaymentTx                   | Y           | Y      |   |   |                                        |
| PermissionedDomainSetTx     | N           |        |   |   |                                        |
| PermissionedDomainDeleteTx  | N           |        |   |   |                                        |
| SetRegularKey               | Y           | Y      |   |   |                                        |
| SingerListSet               | N           |        |   |   |                                        |
| TicketCreate                | N           | N      |   |   | High Priority                          |
| TrustSet                    | Y           | Y      |   |   |                                        |
| XChain*                     | Y           | Y      |   |   | Medium for cross XRPL (private/public) |

## Psuedo Transactions


| PsuedoTxn       | Implemented | Tested |   |   | Comment                                |
|-----------------|-------------|--------|---|---|----------------------------------------|
| EnableAmendment | N           |        |   |   |                                        |
| SetFee          | N           |        |   |   |
| UNLModify       | N           |        |   |   |

## Commands


| Command            | Implemented | Tested  |   | Comment                                  | 
|--------------------|-------------|---------|---|------------------------------------------|
| AMMInfo            | No          |         |   | No AMM MoneyMarket Implemented           |
| AccountChannels    | Yes         |         |   |                                          |        
| AccountCurrencies  | Yes         |         |   |                                          |
| AccountInfo        | Yes         |         |   |                                          |
| AccountLines       | Yes         |         |   |                                          |
| AccountNFTS        | No          |         |   | NFTs Not Supported                       |
| AccountObjects     | Yes         | Yes     |   | Limited Response Decoding                |
| AccountOffers      | Yes         | Yes     |   | Limited Response Decoding                |
| AccountTx          | Yes         | Yes     |   | Txns not decoded                         |
| BookChanges        | Yes         | Partial |   | Response Needs Work                      |
| BookOffers         | Yes         | Partial |   | Response Needs Work                      |
| ChannelAuthorize   | Pending     | Pending |   |                                          |
| ChannelVerify      | Pending     | Pending |   |                                          |
| DepositAuthorize   | Pending     |         |   |                                          |
| Feature            | Yes         | Yes     |   |                                          |
| Fee                | Yes         | Yes     |   |                                          |
| GatewayBalances    | Yes         | Partial |   | Limited Testing                          |
| GetAggregatePrice  | Pending     |         |   |                                          |
| Ledger             | Pending     |         |   |                                          |
| LedgerAccept       | Yes         | Yes     |   |                                          |
| LedgerClosed       | Yes         | Yes     |   |                                          |
| LedgerCurrent      | Yes         | Yes     |   |                                          |
| LedgerData         | Yes         | Partial |   | Response Need Work - esp Binary          |
| LedgerEntry        | Pending     |         |   |                                          |
| Manifest           | Pending     | Pending |   |                                          |
| NFTBuyOffers       | No          |         |   | No AMM MoneyMarket Implemented           |
| NFTSellOffers      | No          |         |   | No AMM MoneyMarket Implemented           |
| NoRippleCheck      | Yes         | Partial |   | Response Needs work on Txns              |
| PathFind           | Pending     | Pending |   | Credentials in Request                   |
| Ping               | Yes         | Pending |   | 
| RipplePathFind     | Yes         | Pending |   | Needs more tests                         |
| ServerDefinitions  | Yes         | Yes     |   | Developer API Really                     |
| ServerInfo         | Yes         | Yes     |   | Json Object Response, no models          |
| ServerState        | Yes         | Pending |   |                                          |
| Sign               | Yes         | Yes     |   | Included in Communication Framework      |
| SignFor            | Pending     |         |   | TOBE:Included in Communication Framework |
| Submit             | Yes         | Yes     |   | Included in Communication Framework      |
| Submit_MultiSigned | Pending     |         |   |                                          |
| Subscribe          | Yes         | Yes     |   | Includes Helper Framework                |
| Unsubscribe        | Yes         | Yes     |   | Includes Helper Framework                |
| ValidationCreate   |             |         |   |                                          |
| Version            | Yes         | Yes     |   |                                          |
| WalletPropose      | Yes         | Yes     |   | Via Server and Standalone                |


## Submission of Commands and Transactions via WebSocket and RPC
- Allows submission of Commands and Transactions
- Provides `common fields` for requests, partial "auto-fill" fields population
- Extracts `common fields` and errors from Responses
- Enables monadic style WebSocket requests using correlation id matching and Fibers
- Memos and InvoiceId on all payments (TODO: Needs Testing)

## Higher-Level Abstractions

- Subscription facilities that expose a FS2 Pipe hook for events/messages
- Subscription facilities that expose a FS2 Topic for events/messages





## WebSocket Specific Functionality
- Subscriptions: Implemented in a communications model, using FS2 Streams and Topic
- 
