package cz.muni.fi.pv168.cashflowevidence.data.validation.common;

import cz.muni.fi.pv168.cashflowevidence.data.validation.Validator;

public abstract class PropertyValidator<T> implements Validator<T> {
    
    public final String name;

    public PropertyValidator(String name) {
        this.name = name;
    }

    protected String getName() {
        return name;
    }
    protected String getName(Object defaultName) {
        return(name != null) ? getName() : String.valueOf(defaultName);
    }
}
