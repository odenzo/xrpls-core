# XRPL Scala Library




## Model Class Notes


### XrplPublicKey

There are a few models that appear in JSON messages as both XRP Base58 and Hex. `public_key` and `public_key_hex`
are a good example.
In this case there is only one `given` Circe Codec. Because many models are (currently) opaque types, either Scala or IJ
gets confused with the `extensions` and given codec.
Therefor, we see here that we import the `given` from XrpPublicKey and again the Codecs in a XrpPublicKey sub-object.
It seems like opaque types don't automatically resolve `given`s in the companian object.
Putting them at the same level as the opaque type causes even more confusion for me. 
The examples all have object types within an encompassing object and put the extensions and codecs in there.
Maybe I will go that route.

```scala mdoc
import com.odenzo.xrpl.models.data.keys.XrpPublicKey
import XrpPublicKey.given

import io.circe.syntax.given

val publicKey = XrpPublicKey.fromPublicKeyHex("0365ADFD889C8406105B16B03890F47C8CBB4130B5B9E13C787E31841E0AEA2E")
val jsonEncoded = publicKey.asJson.spaces4
println(jsonEncoded)
```
