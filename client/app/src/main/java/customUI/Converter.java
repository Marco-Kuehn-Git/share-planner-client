//Marc Beyer//
package customUI;

public class Converter {
    /*
    Ä, ä 		\u00c4, \u00e4
    Ö, ö 		\u00d6, \u00f6
    Ü, ü 		\u00dc, \u00fc
    ß 		    \u00df
     */
    @SuppressWarnings("all")
    public static String convertString(String str){
        return str
                .replace("ä", "\u00e4")
                .replace("Ä", "\u00c4")
                .replace("ö", "\u00f6")
                .replace("Ö", "\u00d6")
                .replace("ü", "\u00fc")
                .replace("Ü", "\u00dc")
                .replace("ß", "\u00df");
    }
}
