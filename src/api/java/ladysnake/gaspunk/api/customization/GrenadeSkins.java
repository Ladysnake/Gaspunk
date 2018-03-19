package ladysnake.gaspunk.api.customization;

import java.util.Arrays;

public enum GrenadeSkins {
    NONE(0F, "Classic"),
    MODOFF(1F, "Steampunk"),
    LADYSNAKE(2F, "Ladysnake");

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

    public String getDisplayName() {
        return displayName;
    }

    public static GrenadeSkins fromDisplayName(String displayName) {
        return Arrays.stream(VALUES).filter(s -> s.getDisplayName().equalsIgnoreCase(displayName)).findAny().orElse(NONE);
    }
}
