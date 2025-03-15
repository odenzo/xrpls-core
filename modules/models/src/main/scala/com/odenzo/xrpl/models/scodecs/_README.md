# Notes on Codec for Serialization and Deserialization

https://xrpl.org/serialization.html


- Basic function is encoding to JSON of object models to XRPL Binary 
- It is assumed all JSON values used for encoding are pre-validated.
- Decoding validates,  using validators from main models 
- The encoding is Model -> ToJson -> ToXRPLBinary 
- The decoding is XrplBinary ->ToModel  using the binary constructors in the model mostly

This used to support trawling through the raw ledger information and decoding it, but didn't update that as CLIO
will probably be enough.

## Objects and PathSets
Because JSON doesn't preserve ordering, the fields are sorted based on XRPL Reference data for each Field and FieldType
In addition, when converting to XRPLBinary to sign a transaction, only "signing fields" are encoded, the rest are filtered.

- **PathSets require sorting the encoding of the atoms in a PathStep **(TODO: Still not done?)**



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
