/*
 * @copyright defined in LICENSE.txt
 */

package hera.client;

import static java.util.Collections.emptyList;
import static java.util.UUID.randomUUID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import hera.AbstractTestCase;
import hera.Context;
import hera.ContextStorage;
import hera.EmptyContext;
import hera.Invocation;
import hera.Requester;
import hera.WriteSynchronizedContextStorage;
import hera.api.model.Account;
import hera.api.model.AccountAddress;
import hera.api.model.AccountFactory;
import hera.api.model.AccountState;
import hera.api.model.AccountTotalVote;
import hera.api.model.Aer;
import hera.api.model.BytesValue;
import hera.api.model.ChainIdHash;
import hera.api.model.ElectedCandidate;
import hera.api.model.RawTransaction;
import hera.api.model.StakeInfo;
import hera.api.model.Transaction;
import hera.api.model.TxHash;
import hera.key.AergoKey;
import hera.key.AergoKeyGenerator;
import hera.key.Signer;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

public class AccountTemplateTest extends AbstractTestCase {

  protected final ContextStorage<Context> contextStorage = new WriteSynchronizedContextStorage<>();

  {
    contextStorage.put(EmptyContext.getInstance());
  }

  @Test
  public void testGetState() throws Exception {
    // given
    final AccountTemplate accountTemplate = new AccountTemplate(contextStorage);
    final Requester mockRequester = mock(Requester.class);
    final AccountState expected = AccountState.newBuilder().build();
    when(mockRequester.request(ArgumentMatchers.<Invocation<?>>any()))
        .thenReturn(expected);
    accountTemplate.requester = mockRequester;

    // then
    final AccountState actual = accountTemplate.getState(AccountAddress.EMPTY);
    assertEquals(expected, actual);
  }

  @Test
  public void testCreateNameTx() throws Exception {
    // given
    final AccountTemplate accountTemplate = new AccountTemplate(contextStorage);
    final Requester mockRequester = mock(Requester.class);
    final TxHash expected = TxHash.of(BytesValue.EMPTY);
    when(mockRequester.request(ArgumentMatchers.<Invocation<?>>any()))
        .thenReturn(expected);
    accountTemplate.requester = mockRequester;

    // then
    final Signer signer = new AergoKeyGenerator().create();
    final TxHash actual = accountTemplate.createNameTx(signer, randomUUID().toString(), 1L);
    assertEquals(expected, actual);
  }

  @Test
  public void testUpdateNameTx() throws Exception {
    // given
    final AccountTemplate accountTemplate = new AccountTemplate(contextStorage);
    final Requester mockRequester = mock(Requester.class);
    final TxHash expected = TxHash.of(BytesValue.EMPTY);
    when(mockRequester.request(ArgumentMatchers.<Invocation<?>>any()))
        .thenReturn(expected);
    accountTemplate.requester = mockRequester;

    // then
    final Signer signer = new AergoKeyGenerator().create();
    final TxHash actual = accountTemplate
        .updateNameTx(signer, randomUUID().toString(), AccountAddress.EMPTY, 1L);
    assertEquals(expected, actual);
  }

  @Test
  public void testGetNameOwner() throws Exception {
    // given
    final AccountTemplate accountTemplate = new AccountTemplate(contextStorage);
    final Requester mockRequester = mock(Requester.class);
    final AccountAddress expected = AccountAddress.EMPTY;
    when(mockRequester.request(ArgumentMatchers.<Invocation<?>>any()))
        .thenReturn(expected);
    accountTemplate.requester = mockRequester;

    // then
    final AccountAddress actual = accountTemplate.getNameOwner(randomUUID().toString());
    assertEquals(expected, actual);
  }

  @Test
  public void testStakeTx() throws Exception {
    // given
    final AccountTemplate accountTemplate = new AccountTemplate(contextStorage);
    final Requester mockRequester = mock(Requester.class);
    final TxHash expected = TxHash.of(BytesValue.EMPTY);
    when(mockRequester.request(ArgumentMatchers.<Invocation<?>>any()))
        .thenReturn(expected);
    accountTemplate.requester = mockRequester;

    // then
    final Signer signer = new AergoKeyGenerator().create();
    final TxHash actual = accountTemplate.stakeTx(signer, Aer.AERGO_ONE, 1L);
    assertEquals(expected, actual);
  }

  @Test
  public void testUnstakeTx() throws Exception {
    // given
    final AccountTemplate accountTemplate = new AccountTemplate(contextStorage);
    final Requester mockRequester = mock(Requester.class);
    final TxHash expected = TxHash.of(BytesValue.EMPTY);
    when(mockRequester.request(ArgumentMatchers.<Invocation<?>>any()))
        .thenReturn(expected);
    accountTemplate.requester = mockRequester;

    // then
    final Signer signer = new AergoKeyGenerator().create();
    final TxHash actual = accountTemplate.unstakeTx(signer, Aer.AERGO_ONE, 1L);
    assertEquals(expected, actual);
  }

  @Test
  public void testGetStakingInfo() throws Exception {
    // given
    final AccountTemplate accountTemplate = new AccountTemplate(contextStorage);
    final Requester mockRequester = mock(Requester.class);
    final StakeInfo expected = StakeInfo.newBuilder().build();
    when(mockRequester.request(ArgumentMatchers.<Invocation<?>>any()))
        .thenReturn(expected);
    accountTemplate.requester = mockRequester;

    // then
    final StakeInfo actual = accountTemplate.getStakingInfo(AccountAddress.EMPTY);
    assertEquals(expected, actual);
  }

  @Test
  public void testVoteTx() throws Exception {
    // given
    final AccountTemplate accountTemplate = new AccountTemplate(contextStorage);
    final Requester mockRequester = mock(Requester.class);
    final TxHash expected = TxHash.of(BytesValue.EMPTY);
    when(mockRequester.request(ArgumentMatchers.<Invocation<?>>any()))
        .thenReturn(expected);
    accountTemplate.requester = mockRequester;

    // then
    final Signer signer = new AergoKeyGenerator().create();
    final TxHash actual = accountTemplate
        .voteTx(signer, randomUUID().toString(), Collections.<String>emptyList(), 1L);
    assertEquals(expected, actual);
  }

  @Test
  public void testGetVotesOf() throws Exception {
    // given
    final AccountTemplate accountTemplate = new AccountTemplate(contextStorage);
    final Requester mockRequester = mock(Requester.class);
    final AccountTotalVote expected = AccountTotalVote.newBuilder().build();
    when(mockRequester.request(ArgumentMatchers.<Invocation<?>>any()))
        .thenReturn(expected);
    accountTemplate.requester = mockRequester;

    // then
    final AccountTotalVote actual = accountTemplate.getVotesOf(AccountAddress.EMPTY);
    assertEquals(expected, actual);
  }

  @Test
  public void testListElected() throws Exception {
    // given
    final AccountTemplate accountTemplate = new AccountTemplate(contextStorage);
    final Requester mockRequester = mock(Requester.class);
    final List<ElectedCandidate> expected = emptyList();
    when(mockRequester.request(ArgumentMatchers.<Invocation<?>>any()))
        .thenReturn(expected);
    accountTemplate.requester = mockRequester;

    // then
    final List<ElectedCandidate> actual = accountTemplate.listElected(randomUUID().toString(), 20);
    assertEquals(expected, actual);
  }

  @Test
  public void testSignAndVerify() {
    final AccountTemplate accountTemplate = new AccountTemplate(contextStorage);
    final AergoKey aergoKey = new AergoKeyGenerator().create();
    final Account account = new AccountFactory().create(aergoKey);
    final RawTransaction rawTransaction = RawTransaction
        .newBuilder(ChainIdHash.of(BytesValue.EMPTY))
        .from(aergoKey.getAddress())
        .to(aergoKey.getAddress())
        .amount(Aer.ZERO)
        .nonce(1L)
        .build();
    final Transaction signed = accountTemplate.sign(account, rawTransaction);
    final boolean result = accountTemplate.verify(account, signed);
    assertTrue(result);
  }

}
