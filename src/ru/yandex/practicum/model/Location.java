package ru.yandex.practicum.model;

public class Location {
    private final LocationType type;
    private final String detail;

    public Location(LocationType type) {
        this(type, null);
    }

    public Location(LocationType type, String detail) {
        this.type = type;
        this.detail = detail;
    }

    public LocationType getType() {
        return type;
    }

    public String getDetail() {
        return detail;
    }

    @Override
    public String toString() {
        if (detail == null) return type.name();
        return type.name() + " (" + detail + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;
        Location loc = (Location) o;
        return type == loc.type &&
                (detail == null ? loc.detail == null : detail.equals(loc.detail));
    }

    @Override
    public int hashCode() {
        return type.hashCode() * 31 + (detail != null ? detail.hashCode() : 0);
    }
}