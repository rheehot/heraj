/*
 * @copyright defined in LICENSE.txt
 */

package hera.api.encode;

import static hera.util.IoUtils.from;

import hera.util.HexUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

public interface Encoder {
  Encoder defaultEncoder = new Encoder() {
    @Override
    public Reader encode(InputStream in) throws IOException {
      return new StringReader(HexUtils.encode(from(in)));
    }
  };

  Reader encode(InputStream in) throws IOException;

}
