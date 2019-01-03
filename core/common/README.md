# Common

Common module contains basic model, encoding/decoding, strategies, key, exceptions.


## Model

Describe basic models of blockchain.

#### Node related
* NodeStatus
* ModuleStatus
* Peer
* PeerMetric

#### Account related
* Account
* AccountAddress
* Authentication
* AccountState
* EncryptedPrivateKey
* AergoKey
* Aer
* Fee

#### Block related
* Block
* BlockHash
* BlockHeader
* BlockChainStatus

#### Transaction related
* RawTransaction
* Transaction
* TxHash
* Signature

#### Contract related
* ContractAddress
* ContractTxHash
* ContractTxReceipt
* ContractInterface
* ContractDefinition
* ContractFunction
* ContractInvocation
* ContractResult

### Internal
Internal models are used inside of api.


## Strategy

Strategy is used for any customizable operation. It is binded with context and any operation holding context can use strategy.
* ConnectStrategy
* FailoverStrategy
* TimeoutStrategy


## TupleOrError

We provide functional-style interface named TupleOrError. It treats exception as the way other functional languages handles. A concept is derived from [either of scala](https://github.com/scala/scala/blob/2.13.x/src/library/scala/util/Either.scala). At this time, there are 4 type of TupleOrError:
* ResultOrError (Tuple1OrError)
* Tuple2OrError
* Tuple3OrError
* Tuple4OrError


## TupleOrErrorFuture

We also provide functional-style async interface named TupleOrErrorFuture. It operates like TupleOrError. A difference is that it handles the future object. Compared to other java future, `TupleOrErrorFuture.get()` doesn't throw `TimeoutException`. It just holds `TimeoutException` in it. At this time, there are 4 type of TupleOrErrorFuture:
* ResultOrErrorFuture (Tuple1OrErrorFuture)
* Tuple2OrErrorFuture
* Tuple3OrErrorFuture
* Tuple4OrErrorFuture