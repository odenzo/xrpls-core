# Subscription

The subscriptions can be done via an admin RPC-Callback mechanism or via web socket.

This implements the WebSocket style (no plans on RPC callbacks), and it intended to be a somewhat long running
procross, where each "Engine" will do one subscribe command.
This may be to multiple topics (eg order and transactions).

At this layer it will just ensure that the envelope and communication is correct.
The results will be send as response json to the specified FS2 stream I guess.
The FS2 stream can then enqueue it, process it, or route however it wants.

We also need to have the ability to:
1. Cancel the subscription as a whole (close the websocket)
2. Unsubscribe from a specific topic (keeping the websocket open)
3. Add additional Subscription topics (keeping the websocket open)
4. Some fault tolerance/retry in case of network connectivity.


For Fault-Tolerance this implies it keeps state with the current subscriptions and deal with additional subscribe/unsubscribe that fail?

Would be simpler just to have one-shot subcription,
and if you want to add/remove topics then just closed this and add a new one.


## Progress

First cut will just be sending the original subscribe message with no updates and have a "finish()" that cancels the connection.
