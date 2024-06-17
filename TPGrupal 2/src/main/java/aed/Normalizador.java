package aed;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class Normalizador {
    private static final Pattern DIACRITICOS = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

    public static String normalizar(String texto) {
        String textoNormalizado = Normalizer.normalize(texto, Normalizer.Form.NFD);
        return DIACRITICOS.matcher(textoNormalizado).replaceAll("");
    }
}
