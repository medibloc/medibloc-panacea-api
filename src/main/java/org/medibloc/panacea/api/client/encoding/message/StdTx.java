package org.medibloc.panacea.api.client.encoding.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.medibloc.panacea.api.client.Wallet;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class StdTx {
    @JsonProperty("msg")
    private PanaceaTransactionMessage[] msgs;
    private StdFee fee;
    private List<StdSignature> signatures;
    private String memo;

    public void sign(Wallet wallet) throws IOException, NoSuchAlgorithmException {
        StdSignDoc sd = new StdSignDoc();
        sd.setAccountNumber(String.valueOf(wallet.getAccountNumber()));
        sd.setChainId(wallet.getChainId());
        sd.setFee(fee);
        sd.setMemo(memo);
        sd.setMsgs(msgs);
        sd.setSequence(String.valueOf(wallet.getSequence()));
        String sig = Base64.encodeBase64String(wallet.sign(sd));

        StdSignature stdsig = new StdSignature();
        stdsig.setPubkey(wallet.getPubKeyForSign());
        stdsig.setSignature(sig);

        if (signatures == null) {
            signatures = new ArrayList<>();
        }
        signatures.add(stdsig);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("msgs", msgs)
                .append("fee", fee)
                .append("signatures", signatures)
                .append("memo", memo)
                .toString();
    }
}
