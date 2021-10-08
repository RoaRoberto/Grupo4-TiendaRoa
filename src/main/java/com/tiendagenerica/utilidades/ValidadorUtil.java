package com.tiendagenerica.utilidades;

import org.hibernate.annotations.SourceType;
import org.hibernate.boot.model.source.spi.SizeSource;

public class ValidadorUtil {

    public static Float priceConvert(String price) {
        try {

            return Float.parseFloat(price.replace("$", "").trim().replace(".", ""));
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(price + "  \t " + e.getMessage());
            throw e;
        }
    }
}
