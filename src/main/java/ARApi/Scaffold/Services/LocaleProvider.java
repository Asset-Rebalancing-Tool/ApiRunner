package ARApi.Scaffold.Services;

import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class LocaleProvider {
    public Locale GetLocale(){
        return Locale.GERMAN;
    }
}
