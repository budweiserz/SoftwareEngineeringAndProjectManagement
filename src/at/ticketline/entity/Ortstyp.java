package at.ticketline.entity;

public enum Ortstyp {
    VERKAUFSSTELLE, KIOSK, KINO, THEATER, OPER, KABARETT, SAAL, LOCATION;
    
    public static String[] toStringArray() {
        return new String[] { "verkaufsstelle", "kiosk", "kino", "theater", "oper", "kabarett", "saal", "location" };
    }

    public static Ortstyp getValueOf(String value) {
        if (value == null) {
            return null;
        }
        if (value.trim().toUpperCase().equals("VERKAUFSSTELLE")) {
            return Ortstyp.VERKAUFSSTELLE;
        }
        if (value.trim().toUpperCase().equals("KIOSK")) {
        	return Ortstyp.KIOSK;
        }
        if (value.trim().toUpperCase().equals("KINO")) {
        	return Ortstyp.KINO;
        }
        if (value.trim().toUpperCase().equals("THEATER")) {
        	return Ortstyp.THEATER;
        }
        if (value.trim().toUpperCase().equals("OPER")) {
        	return Ortstyp.OPER;
        }
        if (value.trim().toUpperCase().equals("KABARETT")) {
        	return Ortstyp.KABARETT;
        }
        if (value.trim().toUpperCase().equals("SAAL")) {
        	return Ortstyp.SAAL;
        }
        if (value.trim().toUpperCase().equals("LOCATION")) {
        	return Ortstyp.LOCATION;
        }
        return null;
    }
}
