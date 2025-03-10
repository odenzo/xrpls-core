# Binary Codecs

This was written a while ago and now revisiting, so some details...

1. Most importantly, when we process a transaction, e.g. sign and submit it, we need to convert the transaction payload
from JSON to a binary formatted listed of fields and objects. This is the tx_json which is a single field encoded
in Hex with the binary content of all the transaction fields and objects.
These convert between Ripple Ledger binary encoded objects and our associated model classes.


2. XRPL stores things in the `ledger` in binary format. To read this information via APIs we need to be able to decode it.
This is less necessary now, I guess, with the CLIO server. Haven't investigated.


Additionally, sometimes we get a TX Blob, from memory that is just for ledger calls.

So, the high level approach is to contruct a transaction object with all fields in it, as Json.

For all the fields in the JSON we have a list of the underlying XRPL data types. 

## High Level View

The whole shebang is wrapped in a module accessible from `XrpBinCodecAPI`.

First we will look at the `signingTxBlob` which creates the binary representation of all the fields in the 
given JSON **that are needed for signing**. `serializeTxBlob` is similar, however all the fields are converted 
to binary as opposed to only signing fields. For simplicity we just refer to both these are serde to binary.
Note that now that alot of the model objects for the API use Binary as base representation the job is a bit easier.

Where possible the serde stuff in the models are used.


## Json <-> Binary Serde

Since we start with a JsonObject, we expect this to be a `top level` object or extracted from its context without
an end marker. JsonObjects mapped to STObject, nested JsonObjects have an end marker but the highest top level one doesnt.
See STObjectScodec.
