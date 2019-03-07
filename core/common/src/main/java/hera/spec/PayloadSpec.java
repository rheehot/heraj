/*
 * @copyright defined in LICENSE.txt
 */

package hera.spec;

import hera.api.model.AccountAddress;
import hera.api.model.ContractDefinition;
import hera.api.model.ContractInvocation;
import hera.api.model.PeerId;
import lombok.Getter;

public class PayloadSpec {

  public static final String VERSION = "v1";

  public enum Type {
    ContractDefinition("", ContractDefinition.class),
    ContractInvocation("", ContractInvocation.class),
    Vote("voteBP", PeerId.class),
    Stake("stake"),
    Unstake("unstake"),
    CreateName("createName", String.class),
    UpdateName("updateName", String.class, AccountAddress.class);

    @Getter
    protected final String name;
    @Getter
    protected final Class<?>[] targets;

    private Type(final String name, final Class<?>... targets) {
      this.name = VERSION + name;
      this.targets = targets;
    }
  }


}