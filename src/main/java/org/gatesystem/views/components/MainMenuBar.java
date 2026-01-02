package org.gatesystem.views.components;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class MainMenuBar extends MenuBar {
    public Menu fileMenu = new Menu("File");
    public Menu exportMenu = new Menu("Export");
    public MenuItem refreshItem = new MenuItem("Refresh");
    public MenuItem csvItem = new MenuItem("Export to CSV");
    public MenuItem exit = new MenuItem("Exit");
    public Menu accountMenu = new Menu("Account");
    public MenuItem resetPassword = new MenuItem("Reset Password");
    public MenuItem logOutItem = new MenuItem("Log Out");

    public MainMenuBar() {
        exportMenu.getItems().add(csvItem);
        fileMenu.getItems().addAll(refreshItem, exportMenu, exit);
        accountMenu.getItems().addAll(resetPassword, logOutItem);
        this.getMenus().addAll(fileMenu, accountMenu);
        this.getStyleClass().add("menu-bar");
    }
}
