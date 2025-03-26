# Bugs and Quirks


This was mostly an exercise of writing JSON and Binary Codecs and generally using strong types.


## Bugs
This is the running list of stuff I plan on fixing:

- Feature to list amendment status is OK, on stand-alone we can unveto. Normally you would add this in RPC startup anyway.




## Quirks

### Opaque Types and Givens
Modelling this started as en experiment to use opaque types. I regret that and while they are nice in concept we don't
really need the super high-performance and dealing with implicit `givens` is painful. The plan was also to see what I could
do in terms of macros but dead ended on that.

I am moving all the opaque types, e.g. `scala opaque type FooBar = BitVector` to be inside an object,
e.g.
```scala
object fooBar {
  opaque type FooBar = BitVector
  
  given io.circe.Codec[FooBar] = ???
  
  object FooBar {
    def builder() = ???
    def validator() = ???
    def fromXXX = ???
  }
  
  extension (v:FooBar)
    def asUHex:String = ???
    def asBin:BitVector = ???
}
```
So the usage externally is done via the import `import foorBar.{*,given}`

You will see some migration in progress for this,
and also to use regular case classes to wrap values based on the underlying XRPL primitive.

### ledger_current_index and ledger_index in responses
The current approach is the resulting command/txn wrapper has `ledger_index` sourced from either `ledger_current_index`
or `ledger_index`, max of  one of which is present.
These are including in the Command / Txn pipelines. 
