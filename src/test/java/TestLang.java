import com.nduowang.ymind.common.Lang;
import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;

import java.util.Locale;

public class TestLang {


    @Test
    public void test01(){

        Locale locale = Locale.getDefault();
        System.out.println(locale.getCountry());
        System.out.println(Locale.getDefault());

        System.out.println(Lang.get("menu.open"));


    }
}
