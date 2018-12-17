/*
 * @copyright defined in LICENSE.txt
 */

package hera.wallet;

import static java.util.UUID.randomUUID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import hera.AbstractTestCase;
import hera.api.AccountOperation;
import hera.api.ContractOperation;
import hera.api.TransactionOperation;
import hera.api.model.Account;
import hera.api.model.AccountAddress;
import hera.api.model.AccountFactory;
import hera.api.model.AccountState;
import hera.api.model.Aer;
import hera.api.model.Aer.Unit;
import hera.api.model.Authentication;
import hera.api.model.BytesValue;
import hera.api.model.ContractAddress;
import hera.api.model.ContractDefinition;
import hera.api.model.ContractFunction;
import hera.api.model.ContractInvocation;
import hera.api.model.ContractTxHash;
import hera.api.model.EncryptedPrivateKey;
import hera.api.model.Fee;
import hera.api.model.RawTransaction;
import hera.api.model.Time;
import hera.api.model.Transaction;
import hera.api.model.TxHash;
import hera.api.model.internal.TryCountAndInterval;
import hera.client.AergoClient;
import hera.key.AergoKeyGenerator;
import org.junit.Test;
import org.mockito.Mockito;

public class InteractiveWalletTest extends AbstractTestCase {

  private final AccountAddress accountAddress =
      AccountAddress.of(() -> "AmLo9CGR3xFZPVKZ5moSVRNW1kyscY9rVkCvgrpwNJjRUPUWadC5");

  private final EncryptedPrivateKey encryptedPrivatekey = EncryptedPrivateKey
      .of(() -> "47RHxbUL3DhA1TMHksEPdVrhumcjdXLAB3Hkv61mqkC9M1Wncai5b91q7hpKydfFHKyyVvgKt");

  private final ContractAddress contractAddress =
      ContractAddress.of(() -> "AmLo9CGR3xFZPVKZ5moSVRNW1kyscY9rVkCvgrpwNJjRUPUWadC5");

  @Test
  public void testGetAccountState() {
    final InteractiveWallet wallet = mock(InteractiveWallet.class, Mockito.CALLS_REAL_METHODS);
    final AergoClient mockClient = mock(AergoClient.class);
    final AccountOperation mockOperation = mock(AccountOperation.class);
    when(mockOperation.getState(any(Account.class))).thenReturn(mock(AccountState.class));
    when(mockClient.getAccountOperation()).thenReturn(mockOperation);
    when(wallet.getAergoClient()).thenReturn(mockClient);
    wallet.account = mock(Account.class);

    assertNotNull(wallet.getCurrentAccountState());
  }

  @Test
  public void testSavekey() {
    InteractiveWallet wallet =
        mock(InteractiveWallet.class, Mockito.CALLS_REAL_METHODS);
    wallet.keyStore = mock(KeyStore.class);
    wallet.saveKey(new AergoKeyGenerator().create(), randomUUID().toString());
  }

  @Test
  public void testExportKey() {
    InteractiveWallet wallet =
        mock(InteractiveWallet.class, Mockito.CALLS_REAL_METHODS);
    final KeyStore keyStore = mock(KeyStore.class);
    when(keyStore.export(any())).thenReturn(encryptedPrivatekey);
    wallet.keyStore = keyStore;

    final Authentication authentication =
        Authentication.of(accountAddress, randomUUID().toString());
    final String exported = wallet.exportKey(authentication);
    assertNotNull(exported);
  }

  @Test
  public void testUnlockOnSuccess() {
    InteractiveWallet wallet =
        mock(InteractiveWallet.class, Mockito.CALLS_REAL_METHODS);

    final AergoClient mockClient = mock(AergoClient.class);
    final AccountOperation mockOperation = mock(AccountOperation.class);
    when(mockOperation.getState(any(Account.class))).thenReturn(mock(AccountState.class));
    when(mockClient.getAccountOperation()).thenReturn(mockOperation);
    when(wallet.getAergoClient()).thenReturn(mockClient);

    final KeyStore keyStore = mock(KeyStore.class);
    when(keyStore.unlock(any())).thenReturn(mock(Account.class));
    wallet.keyStore = keyStore;

    final Authentication authentication =
        Authentication.of(accountAddress, randomUUID().toString());
    final boolean unlockResult = wallet.unlock(authentication);
    assertTrue(unlockResult);
  }

  @Test
  public void testUnlockOnFailure() {
    InteractiveWallet wallet =
        mock(InteractiveWallet.class, Mockito.CALLS_REAL_METHODS);
    wallet.keyStore = mock(KeyStore.class);

    final Authentication authentication =
        Authentication.of(accountAddress, randomUUID().toString());
    final boolean unlockResult = wallet.unlock(authentication);
    assertFalse(unlockResult);
  }

  @Test
  public void testLock() {
    InteractiveWallet wallet =
        mock(InteractiveWallet.class, Mockito.CALLS_REAL_METHODS);
    final KeyStore keyStore = mock(KeyStore.class);
    when(keyStore.lock(any())).thenReturn(true);
    wallet.keyStore = keyStore;

    final Authentication authentication =
        Authentication.of(accountAddress, randomUUID().toString());
    final boolean lockResult = wallet.lock(authentication);
    assertTrue(lockResult);
  }

  @Test
  public void testSend() {
    final InteractiveWallet wallet = mock(InteractiveWallet.class, Mockito.CALLS_REAL_METHODS);
    wallet.account = new AccountFactory().create(accountAddress);

    final AergoClient mockClient = mock(AergoClient.class);
    final TransactionOperation mockTransactionOperation = mock(TransactionOperation.class);
    when(mockTransactionOperation.commit(any())).thenReturn(TxHash.of(BytesValue.EMPTY));
    when(mockClient.getTransactionOperation()).thenReturn(mockTransactionOperation);
    final AccountOperation mockAccountOperation = mock(AccountOperation.class);
    when(mockAccountOperation.sign(any(), any())).thenReturn(mock(Transaction.class));
    when(mockClient.getAccountOperation()).thenReturn(mockAccountOperation);

    when(wallet.getAergoClient()).thenReturn(mockClient);
    when(wallet.getNonceRefreshTryCountAndInterval())
        .thenReturn(TryCountAndInterval.of(1, Time.of(1000L)));

    assertNotNull(wallet.send(accountAddress, Aer.of("1000", Unit.AER), Fee.getDefaultFee()));
  }

  @Test
  public void testDeploy() {
    final InteractiveWallet wallet = mock(InteractiveWallet.class, Mockito.CALLS_REAL_METHODS);
    wallet.account = new AccountFactory().create(accountAddress);
    final AergoClient mockClient = mock(AergoClient.class);
    final ContractOperation mockOperation = mock(ContractOperation.class);
    when(mockOperation.deploy(any(), any(), anyLong(), any()))
        .thenReturn(ContractTxHash.of(BytesValue.EMPTY));
    when(mockClient.getContractOperation()).thenReturn(mockOperation);
    when(wallet.getAergoClient()).thenReturn(mockClient);
    when(wallet.getNonceRefreshTryCountAndInterval())
        .thenReturn(TryCountAndInterval.of(1, Time.of(1000L)));

    assertNotNull(wallet.deploy(new ContractDefinition(""), Fee.getDefaultFee()));
  }

  @Test
  public void testExecute() {
    final InteractiveWallet wallet = mock(InteractiveWallet.class, Mockito.CALLS_REAL_METHODS);
    wallet.account = new AccountFactory().create(accountAddress);
    final AergoClient mockClient = mock(AergoClient.class);
    final ContractOperation mockOperation = mock(ContractOperation.class);
    when(mockOperation.execute(any(), any(), anyLong(), any()))
        .thenReturn(ContractTxHash.of(BytesValue.EMPTY));
    when(mockClient.getContractOperation()).thenReturn(mockOperation);
    when(wallet.getAergoClient()).thenReturn(mockClient);
    when(wallet.getNonceRefreshTryCountAndInterval())
        .thenReturn(TryCountAndInterval.of(1, Time.of(1000L)));

    assertNotNull(wallet.execute(new ContractInvocation(contractAddress, new ContractFunction("")),
        Fee.getDefaultFee()));
  }

  @Test
  public void testSign() {
    InteractiveWallet wallet =
        mock(InteractiveWallet.class, Mockito.CALLS_REAL_METHODS);
    final AergoClient mockClient = mock(AergoClient.class);
    final AccountOperation mockOperation = mock(AccountOperation.class);
    when(mockOperation.sign(any(), any())).thenReturn(mock(Transaction.class));
    when(mockClient.getAccountOperation()).thenReturn(mockOperation);
    when(wallet.getAergoClient()).thenReturn(mockClient);

    wallet.account = new AccountFactory().create(accountAddress);

    final Transaction signed = wallet.sign(mock(RawTransaction.class));
    assertNotNull(signed);
  }

  @Test
  public void testVerify() {
    InteractiveWallet wallet =
        mock(InteractiveWallet.class, Mockito.CALLS_REAL_METHODS);
    final AergoClient mockClient = mock(AergoClient.class);
    final AccountOperation mockOperation = mock(AccountOperation.class);
    when(mockOperation.verify(any(), any())).thenReturn(true);
    when(mockClient.getAccountOperation()).thenReturn(mockOperation);
    when(wallet.getAergoClient()).thenReturn(mockClient);

    wallet.account = new AccountFactory().create(accountAddress);

    final boolean verifyResult = wallet.verify(mock(Transaction.class));
    assertTrue(verifyResult);
  }

  @Test
  public void testGetAccount() {
    final InteractiveWallet wallet = mock(InteractiveWallet.class, Mockito.CALLS_REAL_METHODS);
    wallet.account = mock(Account.class);
    assertNotNull(wallet.getCurrentAccount());
  }

  @Test
  public void testGetAccountOnNoAccount() {
    final InteractiveWallet wallet = mock(InteractiveWallet.class, Mockito.CALLS_REAL_METHODS);
    try {
      wallet.getCurrentAccount();
      fail();
    } catch (Exception e) {
      // good we expected this
    }
  }

  @Test
  public void testGetRecentlyUsedNonce() {
    final InteractiveWallet wallet = mock(InteractiveWallet.class, Mockito.CALLS_REAL_METHODS);
    wallet.account = new AccountFactory().create(accountAddress);

    assertNotNull(wallet.getRecentlyUsedNonce());
  }

  @Test
  public void testIncrementAndGetNonce() {
    final InteractiveWallet wallet = mock(InteractiveWallet.class, Mockito.CALLS_REAL_METHODS);
    wallet.account = new AccountFactory().create(accountAddress);

    final long preNonce = wallet.getRecentlyUsedNonce();
    assertEquals(preNonce + 1, wallet.incrementAndGetNonce());
    assertEquals(preNonce + 1, wallet.getRecentlyUsedNonce());
  }

}