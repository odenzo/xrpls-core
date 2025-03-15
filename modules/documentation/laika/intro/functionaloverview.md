# Functional Overview

The majority of functions/Transactions, except NFT tokens, are implemented. Some are tested better than others.

## Transactional Overview


| Txn                    | Implemented | Tested |   |   | Comment                                |
|------------------------|-------------|--------|---|---|----------------------------------------|
| AccountDelete          | N           |        |   |   |                                        |
| AccountSet             |             |        |   |   |                                        |
| AMM*                   | N           |        |   |   |                                        |
| CredentialAccept       |             |        |   |   |                                        |
| CredentialCreate       |             |        |   |   |                                        |
| CredentialDelete       |             |        |   |   |                                        |
| CheckAccept            | N           |        |   |   |                                        |
| CheckCreate            | N           |        |   |   |                                        |
| CheckDelete            | N           |        |   |   |                                        |
| Clawback               | N           |        |   |   |                                        |
| DIDDelete              | N           |        |   |   |                                        |
| DIDSet                 | N           |        |   |   |                                        |
| EscrowCancel           |             |        |   |   |                                        |
| EscrowCreate           |             |        |   |   |                                        |
| EscrowFinish           |             |        |   |   |                                        |
| MPTokenAuthorize       |             |        |   |   |                                        |
| MPTokenIssuanceCreate  |             |        |   |   |                                        |
| MPTokenIssuanceDestroy |             |        |   |   |                                        |
| MPTokenIssuanceSet     |             |        |   |   |                                        |
| NFT*                   | N           |        |   |   | Will Not Implement                     |
| OfferCreate            | Y           |        |   |   |                                        |
| OfferCancel            | Y           |        |   |   |                                        |
| OracleDelete           | N           |        |   |   |                                        |
| OracleSet              | N           |        |   |   |                                        |
| PaymentChannelCreate   | N           |        |   |   | Low Priority                           |
| PaymentChannelFund     | N           |        |   |   | Low Priority                           |
| PaymentChannelClaim    | N           |        |   |   | Low Priority                           |
| PaymentTx              | Y           | Y      |   |   |                                        |
| SetRegularKey          | Y           | Y      |   |   |                                        |
| SingerListSet          | Y           | Y      |   |   |                                        |
| TicketCreate           | N           | N      |   |   | High Priority                          |
| TrustSet               | Y           | Y      |   |   |                                        |
| XChain*                | Y           | Y      |   |   | Medium for cross XRPL (private/public) |

## Psuedo Transactions


| PsuedoTxn       | Implemented | Tested |   |   | Comment                                |
|-----------------|-------------|--------|---|---|----------------------------------------|
| EnableAmendment | N           |        |   |   |                                        |
| SetFee          | N           |        |   |   |
| UNLModify       | N           |        |   |   |

## Commands


| Command           | Implemented | Tested  |   | Comment                         | 
|-------------------|-------------|---------|---|---------------------------------|
| AMMInfo           | No          |         |   | No AMM MoneyMarket Implemented  |
| AccountChannels   | Yes         |         |   |                                 |        
| AccountCurrencies | Yes         |         |   |                                 |
| AccountInfo       | Yes         |         |   |                                 |
| AccountLines      | Yes         |         |   |                                 |
| AccountNFTS       | No          |         |   | NFTs Not Supported              |
| AccountObjects    | Yes         | Yes     |   | Limited Response Decoding       |
| AccountOffers     | Yes         | Yes     |   | Limited Response Decoding       |
| AccountTx         | Yes         | Yes     |   | Txns not decoded                |
| BookChanges       | Yes         | Partial |   | Response Needs Work             |
| BookOffers        | Yes         | Partial |   | Response Needs Work             |
| ChannelAuthorize  | Pending     | Pending |   |                                 |
| ChannelVerify     | Pending     | Pending |   |                                 |
| DepositAuthorize  | Pending     |         |   |                                 |
| Feature           | Yes         | Yes     |   |                                 |
| Fee               | Yes         | Yes     |   |                                 |
| GatewayBalances   | Yes         | Partial |   | Limited Testing                 |
| GetAggregatePrice | Pending     |         |   |                                 |
| Ledger            | Pending     |         |   |                                 |
| LedgerAccept      | Yes         | Yes     |   |                                 |
| LedgerClosed      | Yes         | Yes     |   |                                 |
| LedgerCurrent     | Yes         | Yes     |   |                                 |
| LedgerData        | Yes         | Partial |   | Response Need Work - esp Binary |
| LedgerEntry       | Pending     |         |   |                                 |
| Manifest          | Pending     | Pending |   |                                 |
| NFTBuyOffers      | No          |         |   | No AMM MoneyMarket Implemented  |
| NFTSellOffers     | No          |         |   | No AMM MoneyMarket Implemented  |
| NoRippleCheck     | Yes         | Partial |   | Response Needs work on Txns     |
| PathFind          | Pending     | Pending |   | Credentials in Request          |
| Ping              | Yes         | Pending |   | 
| RipplePathFind    | Yes         | Pending |   | Needs more tests                |
| ServerDefinitions | Yes         | Yes     |   | Developer API Really            |
| ServerInfo        | Yes         | Yes     |   | Json Object Response, no models |
| ServerState       | Yes         | Pending |   |                                 |
| Subscribe         | Yes         | Yes     |   | Includes Helper Framework       |
| Unsubscribe       | Yes         | Yes     |   | Includes Helper Framework       |
| ValidationCreate  |             |         |   |                                 |
| Version           | Yes         | Yes     |   |                                 |
| WalletPropose     | Yes         | Yes     |   | Via Server and Standalone       |


## Functions Well Tested
- Memos and InvoiceId on all payments—Needs Testing
- 




## WebSocket Specific Functionality
- Subscriptions: Implement in communications model, using FS2 Streams and Topic
- 
