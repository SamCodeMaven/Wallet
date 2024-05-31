package uz.xnarx.wallet.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import uz.xnarx.wallet.payload.Currency;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class CurrencyUtil {
    private static ArrayList<Currency> getCurrencies(){
        Gson gson=new Gson();
        Type type=new TypeToken<ArrayList<Currency>>(){}.getType();
        try {
            URL url=new URL("http://cbu.uz/oz/arkhiv-kursov-valyut/json/");
            URLConnection connection=url.openConnection();
            InputStreamReader inputStream=new InputStreamReader(connection.getInputStream());
            ArrayList<Currency> currencies=gson.fromJson(inputStream,type);
            return currencies;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Currency getCurrency(String code){
//        ArrayList<Currency> currencies=getCurrencies();
        for (Currency currency : getCurrencies()) {
            if (currency.getCode().equals(code)){
                return currency;
            }
        }
        return null;
    }
}
