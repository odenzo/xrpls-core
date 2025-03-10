package com.odenzo.xrpl.models.data.models.keys

case class XrpKeyPair[PRIVATE, PUBLIC](privateKey: PRIVATE, publicKey: PUBLIC)
