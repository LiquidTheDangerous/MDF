package org.semenovao;

import java.util.function.Function;

@FunctionalInterface
public interface IDiscreteFunction extends Function<Boolean[],Boolean> {
    Boolean apply(Boolean... args);
}
