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


| Command         | Implemented | Tested |   |   | Comment                                |
|-----------------|-------------|--------|---|---|----------------------------------------|
| EnableAmendment | N           |        |   |   |                                        |
| SetFee          | N           |        |   |   |
| UNLModify       | N           |        |   |   |

## Functions Well Tested
- Memos and InvoiceId on all payments




## WebSocket Specific Functionality
- Subscriptions: Implement in communications model, using FS2 Streams and Topic
- 
