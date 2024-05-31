package uz.xnarx.wallet.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uz.xnarx.wallet.payload.Currency;
import uz.xnarx.wallet.utils.CurrencyUtil;

@Configuration
public class CurrencyConfig {

    @Bean
    public Currency currency() {
        return CurrencyUtil.getCurrency("840");
    }
}