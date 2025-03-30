package com.odenzo.xrpl.models.data.atoms

/** This will go away I think */
case class AccountAlias(named: String)

///**
//  * This is like ~odenzo, also known as NickName. Cannot send in request like
//  * AccountLine anymore. Google says these aren't actually stored in the ledger
//  * but in an external database but bitchomp has them. Leave as read only type
//  * thing and not pass in requests.
//  */
//final case class AccountAlias(account: String) extends Account {
//  require(account.startsWith("~"), "Ripple Aliases must start with ~ character")
//
//}
//
///**
//  * Use for dt:string addressing of an account mapped to a single ripple address
//  * (e.g. a gateway account) Might as well just make this 4-byte ByteVector huh?
//  * @param tag
//  */
//case class AccountTag(tag: UInt32)
//
//object AccountAlias {
//  implicit val encoder: Encoder[AccountAlias] = Encoder.encodeString.contramap[AccountAlias](_.account.toString)
//  implicit val decoder: Decoder[AccountAlias] = Decoder.decodeString.map(AccountAlias(_))
//}
//
///**
//  * Note this is not valid for most of the WebSocket Requests And I guess
//  * technically it is (acc: Account, dt:String) since can use aliases JSON
//  * Formats quite different normally. FIXME: Broken migrate to AccountTag
//  * product encoding/decoding
//  */
//case class DTAccountAddr(address: AccountAddr, dt: AccountTag) {
//  def toValue = s"$address:$dt" // FIXME: Confirm Use-Case
//}
//
//object DTAccountAddr {
//  implicit val encoder: Encoder[DTAccountAddress] = Encoder[String].contramap[DTAccountAddress](v => s"${v.address}:${v.dt}")
//
//}
//
//object AccountTag {
//
//  def apply(dt: Long): AccountTag = AccountTag(UInt32(dt))
//
//  implicit val decoder: Decoder[AccountTag] = Decoder[UInt32].map(i => AccountTag(i))
//  implicit val encoder: Encoder[AccountTag] = Encoder[UInt32].contramap[AccountTag](_.tag)
//}
