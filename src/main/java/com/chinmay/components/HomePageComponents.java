package com.chinmay.components;

public enum HomePageComponents {
    ADMIN("Admin"),
    PIM("PIM"),
    LEAVE("Leave"),
    TIME("Time"),
    RECRUITMENT("Recruitment");

    private final String menu;

    HomePageComponents(String menu) {
        this.menu = menu;
    }

    @Override
    public String toString() {
        return menu.trim();
    }
}
