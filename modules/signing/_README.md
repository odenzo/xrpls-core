# XRPL Signing

This is full XRP signing locally, and reaches out to the signing
library too maybe.

The main point is to test generate the input for the signing library,
which is TxnSignature and the signing private key (ala Master Seed Hex)

We want this to run in a few phases:
1. Taking the SigningRq and send to XRPL for it to sign, to get the canonical response.
2. Generate TxnSignature
3. Outcall to to Signing Lib to Sign it and package.
4. Submit the signed Rs from (3) to ensure working
We also previously test the submission of a very similar transaction from (1)
