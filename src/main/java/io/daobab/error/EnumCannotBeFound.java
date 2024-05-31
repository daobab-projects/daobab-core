package io.daobab.error;

/**
 * @author Klaudiusz Wojtkowiak, (C) Elephant Software
 */
public class EnumCannotBeFound extends DaobabException {

    private static final long serialVersionUID = 1L;

    public EnumCannotBeFound() {
        super("Enum cannot be found");
    }

}
