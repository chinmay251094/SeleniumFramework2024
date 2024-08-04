package com.chinmay.components;

public enum HomePageComponents {
    DASHBOARD("Dashboard"),
    INVOICE("Invoice"),
    TAG_MANAGEMENT("Tag Management"),
    PAYMENTS("Payments"),
    PETTY_EXPENSE("Petty Expense");

    private final String menu;

    HomePageComponents(String menu) {
        this.menu = menu;
    }

    @Override
    public String toString() {
        return menu.trim();
    }
}
