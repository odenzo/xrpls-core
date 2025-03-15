package com.odenzo.xrpl.models.api.commands

/** Enumeration of known commands (geared for RPC calls, but same for WS) */
enum Command(val label: String):
  case ACCOUNT_CHANNELS extends Command("account_channels")
  case ACCOUNT_CURRENCIES extends Command("account_currencies")
  case ACCOUNT_INFO extends Command("account_info")
  case ACCOUNT_LINES extends Command("account_lines")
  case ACCOUNT_OBJECTS extends Command("account_objects")
  case ACCOUNT_OFFERS extends Command("account_offers")
  case ACCOUNT_TX extends Command("account_tx")
  case AMM_INFO extends Command("amm_info")
  case BOOK_CHANGES extends Command("book_changes")
  case BOOK_OFFERS extends Command("book_offers")
  case CONNECT extends Command("connect")
  case CAN_DELETE extends Command("can_delete")
  case DEPOSIT_AUTHORIZED extends Command("deposit_authorized")
  case FEE extends Command("fee")
  case FEATURE extends Command("feature")
  case GATEWAY_BALANCES extends Command("gateway_balances")
  case LEDGER extends Command("ledger")
  case LEDGER_ACCEPT extends Command("ledger_accept")
  case LEDGER_CLOSED extends Command("ledger_closed")
  case LEDGER_CURRENT extends Command("ledger_current")
  case LEDGER_DATA extends Command("ledger_data")
  case LEDGER_ENTRY extends Command("ledger_entry")
  case LOG_LEVEL extends Command("log_level")
  case NORIPPLE_CHECK extends Command("noripple_check")
  case PATH_FIND extends Command("path_find")
  case PING extends Command("ping")
  case RIPPLE_PATH_FIND extends Command("ripple_path_find")
  case SERVER_DEFINITIONS extends Command("server_definitions")
  case SERVER_STATE extends Command("server_state")
  case SERVER_INFO extends Command("server_info")
  case SIGN extends Command("sign")
  case SUBMIT extends Command("submit")
  case TX extends Command("tx")
  case MANIFEST extends Command("manifest")
  case VALIDATION_CREATE extends Command("validation_create")
  case VERSION extends Command("version")
  case WALLET_PROPOSE extends Command("wallet_propose")
end Command
