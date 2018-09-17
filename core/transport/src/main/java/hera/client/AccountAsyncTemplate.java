/*
 * @copyright defined in LICENSE.txt
 */

package hera.client;

import static com.google.protobuf.ByteString.copyFrom;
import static java.util.stream.Collectors.toList;
import static types.AergoRPCServiceGrpc.newFutureStub;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.protobuf.ByteString;
import hera.FutureChainer;
import hera.api.AccountAsyncOperation;
import hera.api.model.Account;
import hera.api.model.AccountAddress;
import hera.api.model.Authentication;
import hera.api.tupleorerror.ResultOrErrorFuture;
import hera.api.tupleorerror.ResultOrErrorFutureFactory;
import hera.transport.AccountConverterFactory;
import hera.transport.AccountStateConverterFactory;
import hera.transport.ModelConverter;
import io.grpc.ManagedChannel;
import java.util.List;
import lombok.RequiredArgsConstructor;
import types.AccountOuterClass;
import types.AccountOuterClass.AccountList;
import types.AergoRPCServiceGrpc.AergoRPCServiceFutureStub;
import types.Blockchain;
import types.Rpc.Empty;
import types.Rpc.Personal;
import types.Rpc.SingleBytes;

@SuppressWarnings("unchecked")
@RequiredArgsConstructor
public class AccountAsyncTemplate implements AccountAsyncOperation {

  protected final AergoRPCServiceFutureStub aergoService;

  protected final ModelConverter<Account, AccountOuterClass.Account> accountConverter;

  protected final ModelConverter<Account, Blockchain.State> accountStateConverter;

  public AccountAsyncTemplate(final ManagedChannel channel) {
    this(newFutureStub(channel));
  }

  public AccountAsyncTemplate(final AergoRPCServiceFutureStub aergoService) {
    this(aergoService, new AccountConverterFactory().create(),
        new AccountStateConverterFactory().create());
  }

  @Override
  public ResultOrErrorFuture<List<Account>> list() {
    ResultOrErrorFuture<List<Account>> nextFuture = ResultOrErrorFutureFactory.supplyEmptyFuture();

    ListenableFuture<AccountList> listenableFuture = aergoService
        .getAccounts(Empty.newBuilder().build());
    FutureChainer<AccountList, List<Account>> callback = new FutureChainer<>(nextFuture,
        accountList -> accountList.getAccountsList().stream()
            .map(accountConverter::convertToDomainModel)
            .collect(toList()));
    Futures.addCallback(listenableFuture, callback, MoreExecutors.directExecutor());

    return nextFuture;
  }

  @Override
  public ResultOrErrorFuture<Account> create(String password) {
    ResultOrErrorFuture<Account> nextFuture = ResultOrErrorFutureFactory.supplyEmptyFuture();

    final Personal personal = Personal.newBuilder().setPassphrase(password).build();
    ListenableFuture<AccountOuterClass.Account> listenableFuture = aergoService
        .createAccount(personal);
    FutureChainer<AccountOuterClass.Account, Account> callback = new FutureChainer<>(nextFuture,
        account -> {
          Account domainAccount = accountConverter.convertToDomainModel(account);
          domainAccount.setPassword(password);
          return domainAccount;
        });
    Futures.addCallback(listenableFuture, callback, MoreExecutors.directExecutor());

    return nextFuture;
  }

  @Override
  public ResultOrErrorFuture<Account> get(AccountAddress address) {
    ResultOrErrorFuture<Account> nextFuture = ResultOrErrorFutureFactory.supplyEmptyFuture();

    final ByteString byteString = copyFrom(address.getValue());
    final SingleBytes bytes = SingleBytes.newBuilder().setValue(byteString).build();
    ListenableFuture<Blockchain.State> listenableFuture = aergoService.getState(bytes);
    FutureChainer<Blockchain.State, Account> callback = new FutureChainer<>(nextFuture, state -> {
      final Account account = accountStateConverter.convertToDomainModel(state);
      account.setAddress(address);
      return account;
    });
    Futures.addCallback(listenableFuture, callback, MoreExecutors.directExecutor());

    return nextFuture;
  }

  @Override
  public ResultOrErrorFuture<Boolean> unlock(final Authentication authentication) {
    ResultOrErrorFuture<Boolean> nextFuture = ResultOrErrorFutureFactory.supplyEmptyFuture();

    final Account domainAccount =
        Account.of(authentication.getAddress(), authentication.getPassword());
    final Personal rpcPersonal = Personal.newBuilder()
        .setAccount(accountConverter.convertToRpcModel(domainAccount))
        .setPassphrase(domainAccount.getPassword()).build();
    ListenableFuture<AccountOuterClass.Account> listenableFuture = aergoService
        .unlockAccount(rpcPersonal);
    FutureChainer<AccountOuterClass.Account, Boolean> callback = new FutureChainer<>(nextFuture,
        account -> null != account.getAddress());
    Futures.addCallback(listenableFuture, callback, MoreExecutors.directExecutor());

    return nextFuture;

  }

  @Override
  public ResultOrErrorFuture<Boolean> lock(final Authentication authentication) {
    ResultOrErrorFuture<Boolean> nextFuture = ResultOrErrorFutureFactory.supplyEmptyFuture();

    final Account domainAccount =
        Account.of(authentication.getAddress(), authentication.getPassword());
    final Personal rpcPersonal = Personal.newBuilder()
        .setAccount(accountConverter.convertToRpcModel(domainAccount))
        .setPassphrase(domainAccount.getPassword()).build();
    ListenableFuture<AccountOuterClass.Account> listenableFuture = aergoService
        .lockAccount(rpcPersonal);
    FutureChainer<AccountOuterClass.Account, Boolean> callback = new FutureChainer<>(nextFuture,
        account -> null != account.getAddress());
    Futures.addCallback(listenableFuture, callback, MoreExecutors.directExecutor());

    return nextFuture;
  }

}
