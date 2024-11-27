package com.example.textprocessingtool;

import java.util.Objects;

public class DataEntry {

    private String name;
    private String value;

    public DataEntry(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataEntry dataEntry = (DataEntry) o;
        return Objects.equals(name, dataEntry.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
