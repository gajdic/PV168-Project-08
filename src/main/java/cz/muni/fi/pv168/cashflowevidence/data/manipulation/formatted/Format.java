package cz.muni.fi.pv168.cashflowevidence.data.manipulation.formatted;

import java.util.Collection;
import java.util.Collections;

/**
 * Base class representing a format.
 */
public final class Format {
    private final String name;
    private final Collection<String> extensions;

    /**
     * Creates an instance with {@param name} and a {@link Collection} of {@param extensions}.
     *
     * @param name The name of the format
     * @param extensions The {@link Collection} of extensions tightened to the format.
     */
    public Format(String name, Collection<String> extensions) {
        this.name = name;
        this.extensions = extensions;
    }

    /**
     * Gets the name of the format.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets a {@link Collection} of file extensions of the format.
     */
    public Collection<String> getExtensions() {
        return Collections.unmodifiableCollection(extensions);
    }
}
