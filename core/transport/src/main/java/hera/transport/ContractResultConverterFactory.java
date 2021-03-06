/*
 * @copyright defined in LICENSE.txt
 */

package hera.transport;

import static hera.api.model.BytesValue.of;
import static org.slf4j.LoggerFactory.getLogger;

import hera.annotation.ApiAudience;
import hera.annotation.ApiStability;
import hera.api.function.Function1;
import hera.api.model.ContractResult;
import org.slf4j.Logger;
import types.Rpc;

@ApiAudience.Private
@ApiStability.Unstable
public class ContractResultConverterFactory {

  protected final transient Logger logger = getLogger(getClass());

  protected final Function1<ContractResult, Rpc.SingleBytes> domainConverter =
      new Function1<ContractResult, Rpc.SingleBytes>() {

        @Override
        public Rpc.SingleBytes apply(final ContractResult domainContractResult) {
          throw new UnsupportedOperationException();
        }
      };

  protected final Function1<Rpc.SingleBytes, ContractResult> rpcConverter =
      new Function1<Rpc.SingleBytes, ContractResult>() {

        @Override
        public ContractResult apply(final Rpc.SingleBytes rpcContractResult) {
          logger.trace("Rpc contract result to convert: {}", rpcContractResult);
          final ContractResult domainContractResult = ContractResult.of(
              of(rpcContractResult.getValue().toByteArray()));
          logger.trace("Domain contract result converted: {}", domainContractResult);
          return domainContractResult;
        }
      };

  public ModelConverter<ContractResult, Rpc.SingleBytes> create() {
    return new ModelConverter<>(domainConverter, rpcConverter);
  }

}
