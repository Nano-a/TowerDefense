package outils;

public enum NivJournal {
    WARNING("Warning"), ERROR("Error"), STATUS("Status");

    private String name;

    public String getName() {
        return name;
    }

    NivJournal(String s) {
        name = s;
    }
}