/*
 * @copyright defined in LICENSE.txt
 */

package hera.client;

import static com.google.common.util.concurrent.Futures.addCallback;
import static com.google.common.util.concurrent.MoreExecutors.directExecutor;
import static hera.api.model.BytesValue.of;
import static hera.api.tupleorerror.FunctionChain.of;
import static hera.util.TransportUtils.copyFrom;
import static org.slf4j.LoggerFactory.getLogger;
import static types.AergoRPCServiceGrpc.newFutureStub;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.protobuf.ByteString;
import hera.Context;
import hera.annotation.ApiAudience;
import hera.annotation.ApiStability;
import hera.api.TransactionAsyncOperation;
import hera.api.model.AccountAddress;
import hera.api.model.Transaction;
import hera.api.model.TxHash;
import hera.api.tupleorerror.ResultOrErrorFuture;
import hera.api.tupleorerror.ResultOrErrorFutureFactory;
import hera.exception.CommitException;
import hera.transport.ModelConverter;
import hera.transport.TransactionConverterFactory;
import hera.transport.TransactionInBlockConverterFactory;
import io.grpc.ManagedChannel;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import types.AergoRPCServiceGrpc.AergoRPCServiceFutureStub;
import types.Blockchain;
import types.Rpc;

@ApiAudience.Private
@ApiStability.Unstable
@SuppressWarnings("unchecked")
public class TransactionAsyncTemplate implements TransactionAsyncOperation, ChannelInjectable {

  protected final Logger logger = getLogger(getClass());

  protected final ModelConverter<Transaction, Blockchain.Tx> transactionConverter =
      new TransactionConverterFactory().create();

  protected final ModelConverter<Transaction, Blockchain.TxInBlock> transactionInBlockConverter =
      new TransactionInBlockConverterFactory().create();

  @Setter
  protected Context context;

  @Getter
  protected AergoRPCServiceFutureStub aergoService;

  @Override
  public void injectChannel(final ManagedChannel channel) {
    this.aergoService = newFutureStub(channel);
  }

  @Override
  public ResultOrErrorFuture<Transaction> getTransaction(final TxHash txHash) {
    ResultOrErrorFuture<Transaction> nextFuture = ResultOrErrorFutureFactory.supplyEmptyFuture();

    final ByteString byteString = copyFrom(txHash.getBytesValue());
    final Rpc.SingleBytes hashBytes = Rpc.SingleBytes.newBuilder().setValue(byteString).build();
    ListenableFuture<Blockchain.TxInBlock> listenableFuture = aergoService.getBlockTX(hashBytes);
    FutureChain<Blockchain.TxInBlock, Transaction> callback =
        new FutureChain<Blockchain.TxInBlock, Transaction>(nextFuture);
    callback.setSuccessHandler(t -> of(() -> transactionInBlockConverter.convertToDomainModel(t)));
    callback.setFailureHandler(e -> of(() -> {
      logger.debug("Transaction {} is not in a block. Check mempool", txHash);
      final Blockchain.Tx tx = aergoService.getTX(hashBytes).get();
      return transactionConverter.convertToDomainModel(tx);
    }));

    addCallback(listenableFuture, callback, directExecutor());

    return nextFuture;
  }

  @Override
  public ResultOrErrorFuture<TxHash> commit(final Transaction transaction) {
    ResultOrErrorFuture<TxHash> nextFuture = ResultOrErrorFutureFactory.supplyEmptyFuture();

    final Blockchain.Tx tx = transactionConverter.convertToRpcModel(transaction);
    final Blockchain.TxList txList = Blockchain.TxList.newBuilder().addTxs(tx).build();
    ListenableFuture<Rpc.CommitResultList> listenableFuture = aergoService.commitTX(txList);
    FutureChain<Rpc.CommitResultList, TxHash> callback =
        new FutureChain<Rpc.CommitResultList, TxHash>(nextFuture);
    callback.setSuccessHandler(commitResultList -> of(() -> {
      final Rpc.CommitResult commitResult = commitResultList.getResultsList().get(0);
      logger.debug("Commit result: {}", commitResult.getError());
      if (Rpc.CommitStatus.TX_OK == commitResult.getError()) {
        return new TxHash(of(commitResult.getHash().toByteArray()));
      } else {
        throw new CommitException(commitResult.getError());
      }
    }));
    addCallback(listenableFuture, callback, directExecutor());

    return nextFuture;
  }

  @Override
  public ResultOrErrorFuture<TxHash> send(final AccountAddress sender,
      final AccountAddress recipient, final long amount) {
    ResultOrErrorFuture<TxHash> nextFuture = ResultOrErrorFutureFactory.supplyEmptyFuture();

    final Transaction transaction = new Transaction();
    transaction.setSender(sender);
    transaction.setRecipient(recipient);
    transaction.setAmount(amount);

    final Blockchain.Tx tx = transactionConverter.convertToRpcModel(transaction);
    ListenableFuture<Rpc.CommitResult> listenableFuture = aergoService.sendTX(tx);
    FutureChain<Rpc.CommitResult, TxHash> callback = new FutureChain<>(nextFuture);
    callback.setSuccessHandler(c -> of(() -> {
      if (Rpc.CommitStatus.TX_OK == c.getError()) {
        return new TxHash(of(c.getHash().toByteArray()));
      } else {
        throw new CommitException(c.getError());
      }
    }));
    addCallback(listenableFuture, callback, directExecutor());

    return nextFuture;
  }

}
