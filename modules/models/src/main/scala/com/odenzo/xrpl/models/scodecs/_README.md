# Notes on Codec for Serialization and Deserialization

https://xrpl.org/serialization.html


- Basic function is encoding to XRPL Binary former from JSON
- JSON Encoders and Decoders are defined in the main models
- It is assumed all JSON values used for encoding are pre-validated.
- Decoding validates, often using validators from main Models (which are needed for JSON Decoders anyway)
- The main models often implement something similar to "asBinary", esp when data is stored internally as Binary or the
    JSON codecs need to read or write hex/base58 binary field contents

# Objects and PathSets
- Objects require filtering and sorting of fields
= PathSets require sorting the encoding of the atoms in a PathStep

# Mappings

The full bi-directional path is left <-> right but sometimes I will skip the model


JSON || MODEL || ENCODER || DECODER | MODEL | JSON


VL Encoding location still in question. At the field level we know, and when we implement at the nested level we know.
The question is aside from Account Address are there any times these do not match ?
(Account Address always VL encoded at top level)


# Unusual Things

Type Name	Type Code	Length-prefixed?	Description

Transaction	10001	No	A "high-level" type containing an entire transaction. (Handled)

LedgerEntry	10002	No	A "high-level" type containing an entire ledger object. (Handled)

Validation	10003	No	A "high-level" type used in peer-to-peer communications to represent a validation vote in the consensus process.

Metadata	10004	No	A "high-level" type containing metadata for one transaction.

UInt64	3	No	A 64-bit unsigned integer. This type does not appear in transaction instructions, but several ledger objects use fields of this type.

Vector256	19	Yes	This type does not appear in transaction instructions, but the Amendments ledger object's Amendments field uses this to represent which amendments are currently enabled.


# Notes on VL Encoding

The following fields/data types  are VL Encoded:

* Blob
* Type: Vector256
* Account (VL Encoded if a field value, most/all other times no VL Encoded) 
