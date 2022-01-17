package com.project.programmers_order.orders;



public enum State {
    REQUESTED("REQUESTED"),
    ACCEPTED("ACCEPTED"),
    SHIPPING("SHIPPING"),
    COMPLETED("COMPLETED"),
    REJECTED("REJECTED");

    private final String value;

    State(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static State of(String name) {
        for (State state : State.values()) {
            if (state.name().equalsIgnoreCase(name)) {
                return state;
            }
        }
        return null;
    }
}
