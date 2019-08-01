package ladysnake.gaspunk.api.customization;

import java.util.Arrays;

public enum GrenadeSkins {
    NONE(0F, "Classic"),
    MODOFF(1F, "Steampunk"),
    LADYSNAKE(2F, "Serpent"),
    FTBWIKI(3F, "Encyclopedia");

    public static final GrenadeSkins[] VALUES = values();

    /**
     * The texture used by the item
     */
    public final float textureId;
    private final String displayName;

    GrenadeSkins(float textureId, String displayName) {
        this.textureId = textureId;
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return this.displayName;
    }
}
